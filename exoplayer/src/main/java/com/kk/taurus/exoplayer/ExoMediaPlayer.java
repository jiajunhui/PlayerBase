/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.kk.taurus.exoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kk.taurus.playerbase.config.AppContextAttach;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.BaseInternalPlayer;
import com.kk.taurus.playerbase.player.IPlayer;

public class ExoMediaPlayer extends BaseInternalPlayer {

    private final String TAG = "ExoMediaPlayer";

    private final Context mAppContext;
    private SimpleExoPlayer mInternalPlayer;

    private int mVideoWidth, mVideoHeight;

    private int mStartPos = -1;

    private boolean isPreparing = true;
    private boolean isBuffering = false;
    private boolean isPendingSeek = false;

    private final DefaultBandwidthMeter mBandwidthMeter;

    public ExoMediaPlayer(){
        mAppContext = AppContextAttach.getApplicationContext();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(mAppContext);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector();
        mInternalPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);

        // Measures bandwidth during playback. Can be null if not required.
        mBandwidthMeter = new DefaultBandwidthMeter();

        mInternalPlayer.addListener(mEventListener);

    }

    @Override
    public void setDataSource(DataSource dataSource) {
        mInternalPlayer.setVideoListener(mVideoListener);
        String data = dataSource.getData();
        Uri uri = dataSource.getUri();

        Uri videoUri = null;

        if(!TextUtils.isEmpty(data)){
            videoUri = Uri.parse(data);
        }else if(uri!=null){
            videoUri = uri;
        }

        if(videoUri==null){
            Bundle bundle = BundlePool.obtain();
            bundle.putString(EventKey.STRING_DATA, "Incorrect setting of playback data!");
            submitErrorEvent(OnErrorEventListener.ERROR_EVENT_IO, bundle);
            return;
        }

        // Prepare the player with the source.
        isPreparing = true;
        mInternalPlayer.prepare(getMediaSource(videoUri));
        mInternalPlayer.setPlayWhenReady(false);

        Bundle sourceBundle = BundlePool.obtain();
        sourceBundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET,sourceBundle);

    }

    private MediaSource getMediaSource(Uri uri){
        int contentType = Util.inferContentType(uri);
        DefaultDataSourceFactory dataSourceFactory =
                new DefaultDataSourceFactory(mAppContext,
                        Util.getUserAgent(mAppContext, mAppContext.getPackageName()), mBandwidthMeter);
        switch (contentType) {
            case C.TYPE_DASH:
                DefaultDashChunkSource.Factory factory = new DefaultDashChunkSource.Factory(dataSourceFactory);
                return new DashMediaSource(uri, dataSourceFactory, factory, null, null);
            case C.TYPE_SS:
                DefaultSsChunkSource.Factory ssFactory = new DefaultSsChunkSource.Factory(dataSourceFactory);
                return new SsMediaSource(uri, dataSourceFactory, ssFactory, null, null);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, dataSourceFactory, null, null);

            case C.TYPE_OTHER:
            default:
                // This is the MediaSource representing the media to be played.
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                return new ExtractorMediaSource(uri,
                        dataSourceFactory, extractorsFactory, null, null);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        mInternalPlayer.setVideoSurfaceHolder(surfaceHolder);
        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SURFACE_HOLDER_UPDATE, null);
    }

    @Override
    public void setSurface(Surface surface) {
        mInternalPlayer.setVideoSurface(surface);
        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SURFACE_UPDATE, null);
    }

    @Override
    public void setVolume(float left, float right) {
        mInternalPlayer.setVolume(left);
    }

    @Override
    public void setSpeed(float speed) {
        PlaybackParameters parameters = new PlaybackParameters(speed, 1f);
        mInternalPlayer.setPlaybackParameters(parameters);
    }

    @Override
    public boolean isPlaying() {
        if (mInternalPlayer == null)
            return false;
        int state = mInternalPlayer.getPlaybackState();
        switch (state) {
            case Player.STATE_BUFFERING:
            case Player.STATE_READY:
                return mInternalPlayer.getPlayWhenReady();
            case Player.STATE_IDLE:
            case Player.STATE_ENDED:
            default:
                return false;
        }
    }

    @Override
    public int getCurrentPosition() {
        return (int) mInternalPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return (int) mInternalPlayer.getDuration();
    }

    @Override
    public int getAudioSessionId() {
        return mInternalPlayer.getAudioSessionId();
    }

    @Override
    public int getVideoWidth() {
        return mVideoWidth;
    }

    @Override
    public int getVideoHeight() {
        return mVideoHeight;
    }

    @Override
    public void start() {
        mInternalPlayer.setPlayWhenReady(true);
    }

    @Override
    public void start(int msc) {
        mStartPos = msc;
        start();
    }

    @Override
    public void pause() {
        if(isInPlaybackState())
            mInternalPlayer.setPlayWhenReady(false);
    }

    @Override
    public void resume() {
        if(isInPlaybackState())
            mInternalPlayer.setPlayWhenReady(true);
    }

    @Override
    public void seekTo(int msc) {
        if(isInPlaybackState()){
            isPendingSeek = true;
        }
        mInternalPlayer.seekTo(msc);
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_DATA, msc);
        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_TO, bundle);
    }

    @Override
    public void stop() {
        isPreparing = true;
        isBuffering = false;
        updateStatus(IPlayer.STATE_STOPPED);
        mInternalPlayer.stop();
    }

    @Override
    public void reset() {
        stop();
    }

    @Override
    public void destroy() {
        isPreparing = true;
        isBuffering = false;
        updateStatus(IPlayer.STATE_END);
        mInternalPlayer.removeListener(mEventListener);
        mInternalPlayer.clearVideoListener(mVideoListener);
        mInternalPlayer.release();
    }

    private boolean isInPlaybackState(){
        int state = getState();
        return state!=STATE_END
                && state!=STATE_ERROR
                && state!=STATE_INITIALIZED
                && state!=STATE_STOPPED;
    }

    private SimpleExoPlayer.VideoListener mVideoListener = new SimpleExoPlayer.VideoListener() {
        @Override
        public void onVideoSizeChanged(int width, int height,
                                       int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            mVideoWidth = width;
            mVideoHeight = height;
            Bundle bundle = BundlePool.obtain();
            bundle.putInt(EventKey.INT_ARG1, mVideoWidth);
            bundle.putInt(EventKey.INT_ARG2, mVideoHeight);
            bundle.putInt(EventKey.INT_ARG3, 0);
            bundle.putInt(EventKey.INT_ARG4, 0);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE,bundle);
        }

        @Override
        public void onRenderedFirstFrame() {
            PLog.d(TAG,"onRenderedFirstFrame");
            updateStatus(IPlayer.STATE_STARTED);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null);
        }
    };

    private Player.EventListener mEventListener = new Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            PLog.d(TAG,"onTimelineChanged...");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            int bufferPercentage = mInternalPlayer.getBufferedPercentage();
            if(!isLoading){
                Bundle bundle = BundlePool.obtain();
                bundle.putInt(EventKey.INT_DATA, bufferPercentage);
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_UPDATE,bundle);
            }
            PLog.d(TAG,"onLoadingChanged : "+ isLoading + ", bufferPercentage = " + bufferPercentage);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            PLog.d(TAG,"onPlayerStateChanged : playWhenReady = "+ playWhenReady
                    + ", playbackState = " + playbackState);

            if(!isPreparing){
                if(playWhenReady){
                    updateStatus(IPlayer.STATE_STARTED);
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESUME, null);
                }else{
                    updateStatus(IPlayer.STATE_PAUSED);
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE, null);
                }
            }

            if(isPreparing){
                switch (playbackState){
                    case Player.STATE_READY:
                        isPreparing = false;
                        updateStatus(IPlayer.STATE_PREPARED);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED, null);
                        if(mStartPos > 0){
                            mInternalPlayer.seekTo(mStartPos);
                            mStartPos = -1;
                        }
                        break;
                }
            }

            if(isBuffering){
                switch (playbackState){
                    case Player.STATE_READY:
                    case Player.STATE_ENDED:
                        long bitrateEstimate = mBandwidthMeter.getBitrateEstimate();
                        PLog.d(TAG,"buffer_end, BandWidth : " + bitrateEstimate);
                        isBuffering = false;
                        Bundle bundle = BundlePool.obtain();
                        bundle.putLong(EventKey.LONG_DATA, bitrateEstimate);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END, bundle);
                        break;
                }
            }

            if(isPendingSeek){
                switch (playbackState){
                    case Player.STATE_READY:
                        isPendingSeek = false;
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE, null);
                        break;
                }
            }

            if(!isPreparing){
                switch (playbackState){
                    case Player.STATE_BUFFERING:
                        long bitrateEstimate = mBandwidthMeter.getBitrateEstimate();
                        PLog.d(TAG,"buffer_start, BandWidth : " + bitrateEstimate);
                        isBuffering = true;
                        Bundle bundle = BundlePool.obtain();
                        bundle.putLong(EventKey.LONG_DATA, bitrateEstimate);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START, bundle);
                        break;
                    case Player.STATE_ENDED:
                        updateStatus(IPlayer.STATE_PLAYBACK_COMPLETE);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE, null);
                        break;
                }
            }

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            PLog.e(TAG,error.getMessage());
            int type = error.type;
            switch (type){
                case ExoPlaybackException.TYPE_SOURCE:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_IO, null);
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_COMMON, null);
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, null);
                    break;
            }
        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            PLog.d(TAG,"onPlaybackParametersChanged : " + playbackParameters.toString());
        }
    };

}

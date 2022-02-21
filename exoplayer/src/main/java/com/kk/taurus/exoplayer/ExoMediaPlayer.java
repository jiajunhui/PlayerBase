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
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.TracksInfo;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoSize;
import com.kk.taurus.playerbase.config.AppContextAttach;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.kk.taurus.playerbase.entity.TimedTextSource;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.BaseInternalPlayer;
import com.kk.taurus.playerbase.player.IPlayer;

import java.util.HashMap;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;

public class ExoMediaPlayer extends BaseInternalPlayer {

    private final String TAG = "ExoMediaPlayer";

    public static final int PLAN_ID = 200;

    private final Context mAppContext;
    private ExoPlayer mInternalPlayer;

    private int mVideoWidth, mVideoHeight;

    private int mStartPos = -1;

    private boolean isPreparing = true;
    private boolean isBuffering = false;
    private boolean isPendingSeek = false;

    private final DefaultBandwidthMeter mBandwidthMeter;

    public static void init(Context context){
        PlayerConfig.addDecoderPlan(new DecoderPlan(
                PLAN_ID,
                ExoMediaPlayer.class.getName(),
                "exoplayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID);
        PlayerLibrary.init(context);
    }

    public ExoMediaPlayer(){
        mAppContext = AppContextAttach.getApplicationContext();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(mAppContext);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(mAppContext);
        mInternalPlayer = new ExoPlayer.Builder(mAppContext, renderersFactory)
                .setTrackSelector(trackSelector)
                .build();

        // Measures bandwidth during playback. Can be null if not required.
        mBandwidthMeter = new DefaultBandwidthMeter.Builder(mAppContext).build();

        mInternalPlayer.addListener(mPlayerListener);
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        updateStatus(STATE_INITIALIZED);

        String data = dataSource.getData();
        Uri uri = dataSource.getUri();
        String assetsPath = dataSource.getAssetsPath();
        int rawId = dataSource.getRawId();

        Uri videoUri = null;

        if(!TextUtils.isEmpty(data)){
            videoUri = Uri.parse(data);
        }else if(uri!=null){
            videoUri = uri;
        }else if(!TextUtils.isEmpty(assetsPath)){
            try {
                DataSpec dataSpec = new DataSpec(DataSource.buildAssetsUri(assetsPath));
                AssetDataSource assetDataSource = new AssetDataSource(mAppContext);
                assetDataSource.open(dataSpec);
                videoUri = assetDataSource.getUri();
            } catch (AssetDataSource.AssetDataSourceException e) {
                e.printStackTrace();
            }
        }else if(rawId > 0){
            try {
                DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(dataSource.getRawId()));
                RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(mAppContext);
                rawResourceDataSource.open(dataSpec);
                videoUri = rawResourceDataSource.getUri();
            } catch (RawResourceDataSource.RawResourceDataSourceException e) {
                e.printStackTrace();
            }
        }

        if(videoUri==null){
            Bundle bundle = BundlePool.obtain();
            bundle.putString(EventKey.STRING_DATA, "Incorrect setting of playback data!");
            submitErrorEvent(OnErrorEventListener.ERROR_EVENT_IO, bundle);
            return;
        }

        //if scheme is http or https and DataSource contain extra data, use DefaultHttpDataSourceFactory.
        String scheme = videoUri.getScheme();
        HashMap<String, String> extra = dataSource.getExtra();
        //setting user-agent from extra data
        String settingUserAgent = extra!=null?extra.get("User-Agent"):"";
        //if not setting, use default user-agent
        String userAgent = !TextUtils.isEmpty(settingUserAgent)?settingUserAgent:Util.getUserAgent(mAppContext, mAppContext.getPackageName());
        //create DefaultDataSourceFactory
        com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(mAppContext,
                        userAgent, mBandwidthMeter);
        if(extra!=null && extra.size()>0 &&
                ("http".equalsIgnoreCase(scheme)||"https".equalsIgnoreCase(scheme))){
            dataSourceFactory = new DefaultHttpDataSource.Factory()
                    .setUserAgent(userAgent)
                    .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
                    .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)
                    .setAllowCrossProtocolRedirects(true)
                    .setDefaultRequestProperties(extra);
        }

        // Prepare the player with the source.
        isPreparing = true;

        //create MediaSource
        MediaSource mediaSource = getMediaSource(videoUri, dataSourceFactory);

        //handle timed text source
        TimedTextSource timedTextSource = dataSource.getTimedTextSource();
        if(timedTextSource!=null){
            Format format = new Format.Builder().setSampleMimeType(timedTextSource.getMimeType()).setSelectionFlags(timedTextSource.getFlag()).build();
            MediaSource timedTextMediaSource = new SingleSampleMediaSource.Factory(new DefaultDataSourceFactory(mAppContext,
                    userAgent)).createMediaSource(new MediaItem.Subtitle(
                    Uri.parse(timedTextSource.getPath()),
                    checkNotNull(format.sampleMimeType), format.language, format.selectionFlags), C.TIME_UNSET);
            //merge MediaSource and timedTextMediaSource.
            mediaSource = new MergingMediaSource(mediaSource, timedTextMediaSource);
        }

        mInternalPlayer.setMediaSource(mediaSource);
        mInternalPlayer.prepare();
        mInternalPlayer.setPlayWhenReady(false);

        Bundle sourceBundle = BundlePool.obtain();
        sourceBundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET,sourceBundle);

    }

    private MediaSource getMediaSource(Uri uri, com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory){
        int contentType = Util.inferContentType(uri);
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(uri)
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build();
        switch (contentType) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
            case C.TYPE_OTHER:
            default:
                // This is the MediaSource representing the media to be played.
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
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
    public void setLooping(boolean looping) {
        mInternalPlayer.setRepeatMode(looping?Player.REPEAT_MODE_ALL:Player.REPEAT_MODE_OFF);
    }

    @Override
    public boolean isPlaying() {
        if (mInternalPlayer == null) {
            return false;
        }
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
        if(getState() == STATE_PREPARED && msc > 0){
            start();
            seekTo(msc);
        }else{
            mStartPos = msc;
            start();
        }
    }

    @Override
    public void pause() {
        int state = getState();
        if (isInPlaybackState()
                && state != STATE_END
                && state != STATE_ERROR
                && state != STATE_IDLE
                && state != STATE_INITIALIZED
                && state != STATE_PAUSED
                && state != STATE_STOPPED) {
            mInternalPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void resume() {
        if (isInPlaybackState() && getState() == STATE_PAUSED) {
            mInternalPlayer.setPlayWhenReady(true);
        }
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
        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_STOP, null);
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
        mInternalPlayer.removeListener(mPlayerListener);
        mInternalPlayer.release();
        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY, null);
    }

    private boolean isInPlaybackState(){
        int state = getState();
        return state!=STATE_END
                && state!=STATE_ERROR
                && state!=STATE_INITIALIZED
                && state!=STATE_STOPPED;
    }

    private final Player.Listener mPlayerListener = new Player.Listener() {
        @Override
        public void onVideoSizeChanged(VideoSize videoSize) {
            PLog.d(TAG, "onVideoSizeChanged : width = " + videoSize.width + ", height = " + videoSize.height + ", rotation = " + videoSize.unappliedRotationDegrees);
            mVideoWidth = videoSize.width;
            mVideoHeight = videoSize.height;
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

        @Override
        public void onTracksInfoChanged(TracksInfo tracksInfo) {
        }

        @Override
        public void onIsLoadingChanged(boolean isLoading) {
            int bufferPercentage = mInternalPlayer.getBufferedPercentage();
            if(!isLoading){
                submitBufferingUpdate(bufferPercentage, null);
            }
            PLog.d(TAG,"onLoadingChanged : "+ isLoading + ", bufferPercentage = " + bufferPercentage);
        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {
            PLog.d(TAG,"onPlayerStateChanged : playbackState = " + playbackState);
            if(isPreparing){
                switch (playbackState){
                    case Player.STATE_READY:
                        isPreparing = false;
                        updateStatus(IPlayer.STATE_PREPARED);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED, null);

                        if(mStartPos > 0 && mInternalPlayer.getDuration() > 0){
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
        public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
            PLog.d(TAG,"onPlayerStateChanged : playWhenReady = "+ playWhenReady + ", reason = " + reason);
            if(!isPreparing){
                if(playWhenReady){
                    if(getState()==STATE_PREPARED){
                        updateStatus(IPlayer.STATE_STARTED);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_RENDER_START, null);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_START, null);
                    }else{
                        updateStatus(IPlayer.STATE_STARTED);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESUME, null);
                    }
                }else{
                    updateStatus(IPlayer.STATE_PAUSED);
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE, null);
                }
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
        }

        @Override
        public void onPlayerError(PlaybackException error) {
            updateStatus(IPlayer.STATE_ERROR);
            if (error == null) {
                submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, null);
                return;
            }

            String errorMessage = error.getMessage() == null ? "" : error.getMessage();
            Throwable cause = error.getCause();
            String causeMessage = cause != null ? cause.getMessage() : "";
            PLog.e(TAG, errorMessage + ", causeMessage = " + causeMessage);

            Bundle bundle = BundlePool.obtain();
            bundle.putString("errorMessage", errorMessage);
            bundle.putString("causeMessage", causeMessage);

            if (!(error instanceof ExoPlaybackException)) {
                submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, bundle);
                return;
            }

            int type = ((ExoPlaybackException) error).type;
            switch (type) {
                case ExoPlaybackException.TYPE_SOURCE:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_IO, bundle);
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_RENDER, bundle);
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, bundle);
                    break;
                case ExoPlaybackException.TYPE_REMOTE:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_REMOTE, bundle);
                    break;
                // Deprecated from exo core 2.13.0
                // case ExoPlaybackException.TYPE_OUT_OF_MEMORY:
                //     submitErrorEvent(OnErrorEventListener.ERROR_EVENT_OUT_OF_MEMORY, bundle);
                //     break;
                // case ExoPlaybackException.TYPE_TIMEOUT:
                //     submitErrorEvent(OnErrorEventListener.ERROR_EVENT_TIMED_OUT, bundle);
                //     break;
                default:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_COMMON, bundle);
                    break;
            }
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            PLog.d(TAG,"onPlaybackParametersChanged : " + playbackParameters.toString());
        }
    };

}

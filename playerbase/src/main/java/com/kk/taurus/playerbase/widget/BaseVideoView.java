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

package com.kk.taurus.playerbase.widget;

import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.assist.InterEvent;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.extension.NetworkEventProducer;
import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.AVPlayer;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.provider.IDataProvider;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.IReceiverGroup;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.receiver.PlayerStateGetter;
import com.kk.taurus.playerbase.receiver.StateGetter;
import com.kk.taurus.playerbase.render.AspectRatio;
import com.kk.taurus.playerbase.render.IRender;
import com.kk.taurus.playerbase.render.RenderSurfaceView;
import com.kk.taurus.playerbase.render.RenderTextureView;
import com.kk.taurus.playerbase.style.IStyleSetter;
import com.kk.taurus.playerbase.style.StyleSetter;


/**
 * Created by Taurus on 2018/3/17.
 */

public class BaseVideoView extends FrameLayout implements IVideoView, IStyleSetter {

    final String TAG = "BaseVideoView";

    private int mRenderType = IRender.RENDER_TYPE_TEXTURE_VIEW;
    private AVPlayer mPlayer;

    //the container for all play view, contain all covers.
    private SuperContainer mSuperContainer;

    private OnPlayerEventListener mOnPlayerEventListener;
    private OnErrorEventListener mOnErrorEventListener;
    private OnReceiverEventListener mOnReceiverEventListener;

    //style setter for round rect or oval rect.
    private IStyleSetter mStyleSetter;

    //render view, such as TextureView or SurfaceView.
    private IRender mRender;
    private AspectRatio mAspectRatio = AspectRatio.AspectRatio_FIT_PARENT;

    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoSarNum;
    private int mVideoSarDen;
    private int mVideoRotation;

    private IRender.IRenderHolder mRenderHolder;

    private boolean isBuffering;

    private OnVideoViewEventHandler mEventAssistHandler;

    public BaseVideoView(Context context){
        this(context, null);
    }

    public BaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPlayer = createPlayer();
        //attach listener
        mPlayer.setOnPlayerEventListener(mInternalPlayerEventListener);
        mPlayer.setOnErrorEventListener(mInternalErrorEventListener);
        //init style setter.
        mStyleSetter = new StyleSetter(this);
        mSuperContainer = onCreateSuperContainer(context);
        mSuperContainer.setStateGetter(mInternalStateGetter);
        mSuperContainer.setOnReceiverEventListener(mInternalReceiverEventListener);
        addView(mSuperContainer,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * you can get the SuperContainer instance ,and dispatch your custom event.
     *
     * see also
     * {@link SuperContainer#dispatchPlayEvent(int, Bundle)}
     * {@link SuperContainer#dispatchErrorEvent(int, Bundle)}
     *
     * @return
     */
    public final SuperContainer getSuperContainer(){
        return mSuperContainer;
    }

    //default return a SuperContainer for frame default.
    //default add NetworkEventProducer.
    //if you want custom you container ,
    //you can return a custom container extends SuperContainer.
    protected SuperContainer onCreateSuperContainer(Context context){
        SuperContainer superContainer = new SuperContainer(context);
        if(PlayerConfig.isUseDefaultNetworkEventProducer())
            superContainer.addEventProducer(new NetworkEventProducer(context));
        return superContainer;
    }

    //create player instance.
    private AVPlayer createPlayer(){
        return new AVPlayer();
    }

    /**
     * see {@link IPlayer#option(int, Bundle)}
     * @param code the code value custom yourself.
     * @param bundle deliver some data if you need.
     */
    public void option(int code, Bundle bundle){
        mPlayer.option(code, bundle);
    }

    /**
     * see {@link AVPlayer#switchDecoder(int)}
     *
     * @param decoderPlanId the planId is your configuration ids or default id.
     * @return Whether or not to switch to success.
     *
     */
    @Override
    public final boolean switchDecoder(int decoderPlanId){
        boolean switchDecoder = mPlayer.switchDecoder(decoderPlanId);
        if(switchDecoder){
            releaseRender();
        }
        return switchDecoder;
    }

    /**
     * if you need , you can set a data provider.{@link IDataProvider}
     * you need call this method before {@link this#setDataSource(DataSource)}.
     * @param dataProvider
     */
    public void setDataProvider(IDataProvider dataProvider) {
        mPlayer.setDataProvider(dataProvider);
    }

    /**
     * set a listener for DataProvider handle data source.
     * @param onProviderListener
     */
    public void setOnProviderListener(IDataProvider.OnProviderListener onProviderListener){
        mPlayer.setOnProviderListener(onProviderListener);
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        //init AudioManager
        requestAudioFocus();
        //release render on data change.
        releaseRender();
        //Reconfigure the rendering view each time the resource is switched
        setRenderType(mRenderType);
        //set data to player
        mPlayer.setDataSource(dataSource);
    }

    private void requestAudioFocus(){
        PLog.d(TAG,">>requestAudioFocus<<");
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if(am!=null){
            am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    private void releaseAudioFocus(){
        PLog.d(TAG,"<<releaseAudioFocus>>");
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if(am!=null){
            am.abandonAudioFocus(null);
        }
    }

    /**
     * if you set receiver group , you need self handle receivers event ,
     * example if you set a controller cover ,you need self handle seek event etc.
     * @param receiverGroup
     */
    public void setReceiverGroup(IReceiverGroup receiverGroup){
        mSuperContainer.setReceiverGroup(receiverGroup);
    }

    public void setEventHandler(OnVideoViewEventHandler eventHandler){
        this.mEventAssistHandler = eventHandler;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    public void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        this.mOnErrorEventListener = onErrorEventListener;
    }

    public void setOnReceiverEventListener(OnReceiverEventListener onReceiverEventListener) {
        this.mOnReceiverEventListener = onReceiverEventListener;
    }

    private OnReceiverEventListener mInternalReceiverEventListener =
            new OnReceiverEventListener() {
        @Override
        public void onReceiverEvent(int eventCode, Bundle bundle) {
            if(eventCode == InterEvent.CODE_REQUEST_NOTIFY_TIMER){
                mPlayer.setUseTimerProxy(true);
            }else if(eventCode == InterEvent.CODE_REQUEST_STOP_TIMER){
                mPlayer.setUseTimerProxy(false);
            }
            if(mEventAssistHandler!=null)
                mEventAssistHandler.onAssistHandle(BaseVideoView.this, eventCode, bundle);
            if(mOnReceiverEventListener!=null)
                mOnReceiverEventListener.onReceiverEvent(eventCode, bundle);
        }
    };

    //Internal StateGetter for SuperContainer
    private StateGetter mInternalStateGetter = new StateGetter() {
        @Override
        public PlayerStateGetter getPlayerStateGetter() {
            return mInternalPlayerStateGetter;
        }
    };

    //Internal PlayerStateGetter for StateGetter
    private PlayerStateGetter mInternalPlayerStateGetter =
            new PlayerStateGetter() {
        @Override
        public int getState() {
            return mPlayer.getState();
        }

        @Override
        public int getCurrentPosition() {
            return mPlayer.getCurrentPosition();
        }

        @Override
        public int getDuration() {
            return mPlayer.getDuration();
        }

        @Override
        public int getBufferPercentage() {
            return mPlayer.getBufferPercentage();
        }

        @Override
        public boolean isBuffering() {
            return isBuffering;
        }
    };

    public void rePlay(int msc){
        mPlayer.rePlay(msc);
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        this.mAspectRatio = aspectRatio;
        if(mRender!=null)
            mRender.updateAspectRatio(aspectRatio);
    }

    @Override
    public void setVolume(float left, float right){
        mPlayer.setVolume(left, right);
    }

    @Override
    public void setSpeed(float speed){
        mPlayer.setSpeed(speed);
    }

    /**
     * if you want to clear frame and recreate render, call this method.
     */
    public void updateRender(){
        releaseRender();
        setRenderType(mRenderType);
    }

    @Override
    public void setRenderType(int renderType) {
        boolean renderChange = mRenderType!=renderType;
        if(!renderChange && mRender!=null && !mRender.isReleased())
            return;
        releaseRender();
        switch (renderType){
            case IRender.RENDER_TYPE_SURFACE_VIEW:
                mRenderType = IRender.RENDER_TYPE_SURFACE_VIEW;
                mRender = new RenderSurfaceView(getContext());
                break;
            default:
            case IRender.RENDER_TYPE_TEXTURE_VIEW:
                mRenderType = IRender.RENDER_TYPE_TEXTURE_VIEW;
                mRender = new RenderTextureView(getContext());
                ((RenderTextureView)mRender).setTakeOverSurfaceTexture(true);
                break;
        }
        //clear render holder
        mRenderHolder = null;
        mPlayer.setSurface(null);
        mRender.updateAspectRatio(mAspectRatio);
        mRender.setRenderCallback(mRenderCallback);
        //update some params
        mRender.updateVideoSize(mVideoWidth, mVideoHeight);
        mRender.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
        //update video rotation
        mRender.setVideoRotation(mVideoRotation);
        //add to container
        mSuperContainer.setRenderView(mRender.getRenderView());
    }

    @Override
    public boolean isInPlaybackState() {
        int state = getState();
        return state!= IPlayer.STATE_END
                && state!= IPlayer.STATE_ERROR
                && state!= IPlayer.STATE_IDLE
                && state!= IPlayer.STATE_INITIALIZED
                && state!= IPlayer.STATE_STOPPED;
    }

    @Override
    public IRender getRender() {
        return mRender;
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    //getAudioSessionId from player
    @Override
    public int getAudioSessionId() {
        return mPlayer.getAudioSessionId();
    }

    //stream buffer percent
    //min 0, and max 100.
    @Override
    public int getBufferPercentage() {
        return mPlayer.getBufferPercentage();
    }

    /**
     * See also
     * {@link IPlayer#STATE_END}
     * {@link IPlayer#STATE_ERROR}
     * {@link IPlayer#STATE_IDLE}
     * {@link IPlayer#STATE_INITIALIZED}
     * {@link IPlayer#STATE_PREPARED}
     * {@link IPlayer#STATE_STARTED}
     * {@link IPlayer#STATE_PAUSED}
     * {@link IPlayer#STATE_STOPPED}
     * {@link IPlayer#STATE_PLAYBACK_COMPLETE}
     */
    @Override
    public int getState() {
        return mPlayer.getState();
    }

    @Override
    public void start() {
        mPlayer.start();
    }

    /**
     * If you want to start play at a specified time,
     * please set this method.
     * @param msc
     */
    @Override
    public void start(int msc) {
        mPlayer.start(msc);
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public void resume() {
        mPlayer.resume();
    }

    @Override
    public void seekTo(int msc) {
        mPlayer.seekTo(msc);
    }

    @Override
    public void stop() {
        mPlayer.stop();
    }

    @Override
    public void stopPlayback() {
        PLog.e(TAG,"stopPlayback release.");
        releaseAudioFocus();
        mPlayer.destroy();
        mRenderHolder = null;
        releaseRender();
        mSuperContainer.destroy();
    }

    /**
     * release render
     * see also
     * {@link RenderTextureView#release()}
     */
    private void releaseRender(){
        if(mRender!=null){
            mRender.release();
            mRender = null;
        }
    }

    private OnPlayerEventListener mInternalPlayerEventListener =
            new OnPlayerEventListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            switch (eventCode){
                //when get video size , need update render for measure.
                case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE:
                    if(bundle!=null){
                        mVideoWidth = bundle.getInt(EventKey.INT_ARG1);
                        mVideoHeight = bundle.getInt(EventKey.INT_ARG2);
                        mVideoSarNum = bundle.getInt(EventKey.INT_ARG3);
                        mVideoSarDen = bundle.getInt(EventKey.INT_ARG4);
                        PLog.d(TAG,"onVideoSizeChange : videoWidth = " + mVideoWidth
                                + ", videoHeight = " + mVideoHeight
                                + ", videoSarNum = " + mVideoSarNum
                                + ", videoSarDen = " + mVideoSarDen);
                        if(mRender!=null){
                            //update video size
                            mRender.updateVideoSize(mVideoWidth, mVideoHeight);
                            //update video sarNum,sarDen
                            mRender.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
                        }
                    }
                    break;
                //when get video rotation, need update render rotation.
                case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED:
                    if(bundle!=null){
                        //if rotation change need update render.
                        mVideoRotation = bundle.getInt(EventKey.INT_DATA);
                        PLog.d(TAG,"onVideoRotationChange : videoRotation = " + mVideoRotation);
                        if(mRender!=null)
                            mRender.setVideoRotation(mVideoRotation);
                    }
                    break;
                //when prepared bind surface.
                case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:
                    if(bundle!=null && mRender!=null){
                        mVideoWidth = bundle.getInt(EventKey.INT_ARG1);
                        mVideoHeight = bundle.getInt(EventKey.INT_ARG2);
                        mRender.updateVideoSize(mVideoWidth, mVideoHeight);
                    }
                    bindRenderHolder(mRenderHolder);
                    break;
                case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START:
                    isBuffering = true;
                    break;
                case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END:
                    isBuffering = false;
                    break;
            }
            if(mOnPlayerEventListener!=null)
                mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
            //last dispatch event , because bundle will be recycle after dispatch.
            mSuperContainer.dispatchPlayEvent(eventCode, bundle);
        }
    };

    private OnErrorEventListener mInternalErrorEventListener =
            new OnErrorEventListener() {
        @Override
        public void onErrorEvent(int eventCode, Bundle bundle) {
            PLog.e(TAG,"onError : code = " + eventCode
                    + ", Message = " + (bundle==null?"no message":bundle.toString()));
            if(mOnErrorEventListener!=null)
                mOnErrorEventListener.onErrorEvent(eventCode, bundle);
            //last dispatch event , because bundle will be recycle after dispatch.
            mSuperContainer.dispatchErrorEvent(eventCode, bundle);
        }
    };

    //on render holder ready ,bind the player.
    private void bindRenderHolder(IRender.IRenderHolder renderHolder){
        if(renderHolder!=null)
            renderHolder.bindPlayer(mPlayer);
    }

    private IRender.IRenderCallback mRenderCallback = new IRender.IRenderCallback() {
        @Override
        public void onSurfaceCreated(IRender.IRenderHolder renderHolder, int width, int height) {
            PLog.d(TAG,"onSurfaceCreated : width = " + width + ", height = " + height);
            //on surface create ,try to attach player.
            mRenderHolder = renderHolder;
            bindRenderHolder(mRenderHolder);
        }
        @Override
        public void onSurfaceChanged(IRender.IRenderHolder renderHolder,
                                     int format, int width, int height) {
            //not handle some...
        }
        @Override
        public void onSurfaceDestroy(IRender.IRenderHolder renderHolder) {
            PLog.d(TAG,"onSurfaceDestroy...");
            //on surface destroy detach player
            mRenderHolder = null;
        }
    };

    //----------------------------set video view style--------------------------

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setRoundRectShape(float radius) {
        mStyleSetter.setRoundRectShape(radius);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setRoundRectShape(Rect rect, float radius) {
        mStyleSetter.setRoundRectShape(rect, radius);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setOvalRectShape() {
        mStyleSetter.setOvalRectShape();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setOvalRectShape(Rect rect) {
        mStyleSetter.setOvalRectShape(rect);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clearShapeStyle() {
        mStyleSetter.clearShapeStyle();
    }

    @Override
    public void setElevationShadow(float elevation) {
        mStyleSetter.setElevationShadow(elevation);
    }

    @Override
    public void setElevationShadow(int backgroundColor, float elevation) {
        mStyleSetter.setElevationShadow(backgroundColor, elevation);
    }
}

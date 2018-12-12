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

package com.kk.taurus.playerbase.assist;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.kk.taurus.playerbase.AVPlayer;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.extension.NetworkEventProducer;
import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.provider.IDataProvider;
import com.kk.taurus.playerbase.receiver.IReceiverGroup;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.receiver.PlayerStateGetter;
import com.kk.taurus.playerbase.receiver.StateGetter;
import com.kk.taurus.playerbase.render.AspectRatio;
import com.kk.taurus.playerbase.render.IRender;
import com.kk.taurus.playerbase.render.RenderSurfaceView;
import com.kk.taurus.playerbase.render.RenderTextureView;
import com.kk.taurus.playerbase.widget.SuperContainer;

/**
 *
 * Created by Taurus on 2018/5/21.
 *
 * This class is mainly used for association between player and component view.
 * For example, maybe you need to switch playback in different views.
 * In this scene, you can use this class.
 * You only need to import your layout container and playback resources.
 *
 */
public final class RelationAssist implements AssistPlay {

    private final String TAG = "RelationAssist";

    private Context mContext;

    private AVPlayer mPlayer;

    /**
     * SuperContainer for ReceiverGroup and Render.
     */
    private SuperContainer mSuperContainer;

    /**
     * ReceiverGroup from out setting.
     */
    private IReceiverGroup mReceiverGroup;

    private int mRenderType = IRender.RENDER_TYPE_TEXTURE_VIEW;
    private boolean mRenderTypeChange;
    private IRender mRender;
    private AspectRatio mAspectRatio = AspectRatio.AspectRatio_FIT_PARENT;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoSarNum;
    private int mVideoSarDen;
    private int mVideoRotation;

    private IRender.IRenderHolder mRenderHolder;

    private DataSource mDataSource;

    private boolean isBuffering;

    private OnPlayerEventListener mOnPlayerEventListener;
    private OnErrorEventListener mOnErrorEventListener;
    private OnReceiverEventListener mOnReceiverEventListener;

    private OnAssistPlayEventHandler mOnEventAssistHandler;

    public RelationAssist(Context context){
        this(context, null);
    }

    public RelationAssist(Context context, SuperContainer superContainer){
        this.mContext = context;
        mPlayer = new AVPlayer();
        if(superContainer == null){
            superContainer = new SuperContainer(context);
        }
        if(PlayerConfig.isUseDefaultNetworkEventProducer())
            superContainer.addEventProducer(new NetworkEventProducer(context));
        mSuperContainer = superContainer;
        mSuperContainer.setStateGetter(mInternalStateGetter);
    }

    public SuperContainer getSuperContainer() {
        return mSuperContainer;
    }

    private void attachPlayerListener(){
        mPlayer.setOnPlayerEventListener(mInternalPlayerEventListener);
        mPlayer.setOnErrorEventListener(mInternalErrorEventListener);
        mSuperContainer.setOnReceiverEventListener(mInternalReceiverEventListener);
    }

    private void detachPlayerListener(){
        mPlayer.setOnPlayerEventListener(null);
        mPlayer.setOnErrorEventListener(null);
        mSuperContainer.setOnReceiverEventListener(null);
    }

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

    private OnPlayerEventListener mInternalPlayerEventListener =
            new OnPlayerEventListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            onInternalHandlePlayerEvent(eventCode, bundle);
            if(mOnPlayerEventListener!=null)
                mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
            mSuperContainer.dispatchPlayEvent(eventCode, bundle);
        }
    };

    private void onInternalHandlePlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            //when get video size , need update render for measure.
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE:
                if(bundle!=null){
                    mVideoWidth = bundle.getInt(EventKey.INT_ARG1);
                    mVideoHeight = bundle.getInt(EventKey.INT_ARG2);
                    mVideoSarNum = bundle.getInt(EventKey.INT_ARG3);
                    mVideoSarDen = bundle.getInt(EventKey.INT_ARG4);
                    if(mRender!=null){
                        mRender.updateVideoSize(mVideoWidth, mVideoHeight);
                        mRender.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
                    }
                }
                break;
            //when get video rotation, need update render rotation.
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED:
                if(bundle!=null){
                    mVideoRotation = bundle.getInt(EventKey.INT_DATA);
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
    }

    private OnErrorEventListener mInternalErrorEventListener =
            new OnErrorEventListener() {
        @Override
        public void onErrorEvent(int eventCode, Bundle bundle) {
            onInternalHandleErrorEvent(eventCode, bundle);
            if(mOnErrorEventListener!=null)
                mOnErrorEventListener.onErrorEvent(eventCode, bundle);
            mSuperContainer.dispatchErrorEvent(eventCode, bundle);
        }
    };

    private void onInternalHandleErrorEvent(int eventCode, Bundle bundle) {
        //not handle
    }

    public void setEventAssistHandler(OnAssistPlayEventHandler onEventAssistHandler) {
        this.mOnEventAssistHandler = onEventAssistHandler;
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
            //if setting AssistEventHandler, call back it to handle.
            if(mOnEventAssistHandler !=null)
                mOnEventAssistHandler.onAssistHandle(RelationAssist.this, eventCode, bundle);
            if(mOnReceiverEventListener!=null)
                mOnReceiverEventListener.onReceiverEvent(eventCode, bundle);
        }
    };

    @Override
    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    @Override
    public void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        this.mOnErrorEventListener = onErrorEventListener;
    }

    @Override
    public void setOnReceiverEventListener(OnReceiverEventListener onReceiverEventListener) {
        this.mOnReceiverEventListener = onReceiverEventListener;
    }

    @Override
    public void setOnProviderListener(IDataProvider.OnProviderListener onProviderListener) {
        mPlayer.setOnProviderListener(onProviderListener);
    }

    @Override
    public void setDataProvider(IDataProvider dataProvider) {
        mPlayer.setDataProvider(dataProvider);
    }

    @Override
    public boolean switchDecoder(int decoderPlanId) {
        boolean switchDecoder = mPlayer.switchDecoder(decoderPlanId);
        if(switchDecoder){
            releaseRender();
        }
        return switchDecoder;
    }

    /**
     * see also {@link AVPlayer#option(int, Bundle)}
     * @param code
     * @param bundle
     */
    public void option(int code, Bundle bundle){
        mPlayer.option(code, bundle);
    }

    @Override
    public void setReceiverGroup(IReceiverGroup receiverGroup) {
        this.mReceiverGroup = receiverGroup;
    }

    public IReceiverGroup getReceiverGroup() {
        return mReceiverGroup;
    }

    /**
     * set render type
     *
     * see also
     * {@link IRender#RENDER_TYPE_TEXTURE_VIEW}
     * {@link IRender#RENDER_TYPE_SURFACE_VIEW}
     *
     * @param renderType
     */
    @Override
    public void setRenderType(int renderType){
        mRenderTypeChange = mRenderType!=renderType;
        this.mRenderType = renderType;
        updateRender();
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        this.mAspectRatio = aspectRatio;
        if(mRender!=null)
            mRender.updateAspectRatio(aspectRatio);
    }

    public IRender getRender() {
        return mRender;
    }

    @Override
    public void setVolume(float left, float right) {
        mPlayer.setVolume(left, right);
    }

    @Override
    public void setSpeed(float speed) {
        mPlayer.setSpeed(speed);
    }

    /**
     * Associate the playback view to the specified container
     * @param userContainer
     */
    @Override
    public void attachContainer(ViewGroup userContainer) {
        attachContainer(userContainer, false);
    }

    public void attachContainer(ViewGroup userContainer, boolean updateRender){
        attachPlayerListener();
        detachSuperContainer();
        if(mReceiverGroup!=null){
            mSuperContainer.setReceiverGroup(mReceiverGroup);
        }
        if(updateRender || isNeedForceUpdateRender()){
            releaseRender();
            //update render view.
            updateRender();
        }
        //attach SuperContainer
        if(userContainer!=null){
            userContainer.addView(mSuperContainer,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private boolean isNeedForceUpdateRender(){
        return mRender==null || mRender.isReleased() || mRenderTypeChange;
    }

    private void updateRender(){
        if(isNeedForceUpdateRender()){
            mRenderTypeChange = false;
            releaseRender();
            switch (mRenderType){
                case IRender.RENDER_TYPE_SURFACE_VIEW:
                    mRender = new RenderSurfaceView(mContext);
                    break;
                case IRender.RENDER_TYPE_TEXTURE_VIEW:
                default:
                    mRender = new RenderTextureView(mContext);
                    ((RenderTextureView)mRender).setTakeOverSurfaceTexture(true);
                    break;
            }
            mRenderHolder = null;
            mPlayer.setSurface(null);
            mRender.updateAspectRatio(mAspectRatio);
            mRender.setRenderCallback(mRenderCallback);
            //update some params for render type change
            mRender.updateVideoSize(mVideoWidth, mVideoHeight);
            mRender.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
            //update video rotation
            mRender.setVideoRotation(mVideoRotation);
            mSuperContainer.setRenderView(mRender.getRenderView());
        }
    }

    @Override
    public void setDataSource(DataSource dataSource){
        this.mDataSource = dataSource;
    }

    @Override
    public void play() {
        play(false);
    }

    @Override
    public void play(boolean updateRender) {
        if(updateRender){
            releaseRender();
            updateRender();
        }
        if(mDataSource!=null){
            onInternalSetDataSource(mDataSource);
            onInternalStart();
        }
    }

    private IRender.IRenderCallback mRenderCallback =
            new IRender.IRenderCallback() {
        @Override
        public void onSurfaceCreated(IRender.IRenderHolder renderHolder,
                                     int width, int height) {
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

    private void bindRenderHolder(IRender.IRenderHolder renderHolder){
        if(renderHolder!=null)
            renderHolder.bindPlayer(mPlayer);
    }

    private void releaseRender(){
        if(mRender!=null){
            mRender.setRenderCallback(null);
            mRender.release();
        }
        mRender = null;
    }

    private void detachSuperContainer(){
        ViewParent parent = mSuperContainer.getParent();
        if(parent!=null && parent instanceof ViewGroup){
            ((ViewGroup) parent).removeView(mSuperContainer);
        }
    }

    private void onInternalSetDataSource(DataSource dataSource){
        mPlayer.setDataSource(dataSource);
    }

    private void onInternalStart(int msc){
        mPlayer.start(msc);
    }

    private void onInternalStart(){
        mPlayer.start();
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
    public void rePlay(int msc) {
        if(mDataSource!=null){
            onInternalSetDataSource(mDataSource);
            onInternalStart(msc);
        }
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
    public void reset() {
        mPlayer.reset();
    }

    @Override
    public void destroy() {
        mPlayer.destroy();
        detachPlayerListener();
        mRenderHolder = null;
        releaseRender();
        mSuperContainer.destroy();
        detachSuperContainer();
        setReceiverGroup(null);
    }
}

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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.AVPlayer;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.provider.IDataProvider;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
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

    //use TextureView for render
    public static final int RENDER_TYPE_TEXTURE_VIEW = 0;

    //use SurfaceView for render
    public static final int RENDER_TYPE_SURFACE_VIEW = 1;

    private int nRenderType = RENDER_TYPE_TEXTURE_VIEW;
    private AVPlayer mPlayer;

    //the container for all play view, contain all covers.
    private ViewContainer mViewContainer;

    private OnPlayerEventListener mOnPlayerEventListener;
    private OnErrorEventListener mOnErrorEventListener;

    //style setter for round rect or oval rect.
    private IStyleSetter mStyleSetter;

    //render view, such as TextureView or SurfaceView.
    private IRender mRender;

    private IRender.IRenderHolder mRenderHolder;

    private int mVideoRotation;
    private int mVideoWidth,mVideoHeight;
    private int mVideoSarNum,mVideoSarDen;

    //the stream buffer percent
    private int mBufferPercentage;

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
        mPlayer = createPlayer(getContext());
        //attach listener
        mPlayer.setOnPlayerEventListener(mInternalPlayerEventListener);
        mPlayer.setOnErrorEventListener(mInternalErrorEventListener);
        //init style setter.
        mStyleSetter = new StyleSetter(this);
        mViewContainer = onCreateViewContainer(context);
        addView(mViewContainer,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * you can get the ViewContainer instance ,and dispatch your custom event.
     *
     * see also
     * {@link ViewContainer#dispatchPlayEvent(int, Bundle)}
     * {@link ViewContainer#dispatchErrorEvent(int, Bundle)}
     *
     * @return
     */
    public ViewContainer getViewContainer(){
        return mViewContainer;
    }

    //default return a ViewContainer for frame default.
    //if you want custom you container ,
    //you can return a custom container extends ViewContainer.
    protected ViewContainer onCreateViewContainer(Context context){
        return new ViewContainer(context);
    }

    //create player instance.
    private AVPlayer createPlayer(Context context){
        return new AVPlayer(context);
    }

    /**
     * if you need , you can set a data provider.{@link IDataProvider}
     * you need call this method before {@link this#setDataSource(DataSource)}.
     * @param dataProvider
     */
    public void setDataProvider(IDataProvider dataProvider) {
        mPlayer.setDataProvider(dataProvider);
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        //when data source set, buffer percent reset 0.
        mBufferPercentage = 0;
        //init AudioManager
        requestAudioFocus();
        //Reconfigure the rendering view each time the resource is switched
        setRenderType(nRenderType);
        //set data to player
        mPlayer.setDataSource(dataSource);
    }

    private void requestAudioFocus(){
        PLog.d(TAG,">>requestAudioFocus<<");
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private void releaseAudioFocus(){
        PLog.d(TAG,"<<releaseAudioFocus>>");
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);
    }

    /**
     * if you set receiver group , you need self handle receivers event ,
     * example if you set a controller cover ,you need self handle seek event etc.
     * @param receiverGroup
     */
    public void setReceiverGroup(ReceiverGroup receiverGroup){
        mViewContainer.setReceiverGroup(receiverGroup);
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    public void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        this.mOnErrorEventListener = onErrorEventListener;
    }

    public void setOnReceiverEventListener(OnReceiverEventListener onReceiverEventListener) {
        mViewContainer.setOnReceiverEventListener(onReceiverEventListener);
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(mRender!=null)
            mRender.updateAspectRatio(aspectRatio);
    }

    @Override
    public void setRenderType(int renderType) {
        releaseRender();
        nRenderType = renderType;
        switch (renderType){
            case RENDER_TYPE_SURFACE_VIEW:
                mRender = new RenderSurfaceView(getContext());
                break;
            default:
            case RENDER_TYPE_TEXTURE_VIEW:
                mRender = new RenderTextureView(getContext());
                ((RenderTextureView)mRender).setTakeOverSurfaceTexture(true);
                break;
        }
        //clear render holder
        mRenderHolder = null;
        mPlayer.setDisplay(null);
        mPlayer.setSurface(null);
        mRender.setRenderCallback(mRenderCallback);
        //update some params
        mRender.updateVideoSize(mVideoWidth, mVideoHeight);
        mRender.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
        //update video rotation
        mRender.setVideoRotation(mVideoRotation);
        //add to container
        mViewContainer.setRenderView(mRender.getRenderView());
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
        return mBufferPercentage;
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
    }

    /**
     * release render
     * see also
     * {@link RenderTextureView#release()}
     */
    private void releaseRender(){
        if(mRender!=null)
            mRender.release();
    }

    private OnPlayerEventListener mInternalPlayerEventListener = new OnPlayerEventListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            //when get video size , need update render for measure.
            if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE
                    && bundle!=null){
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
            //when get video rotation, need update render rotation.
            else if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED
                    && bundle!=null){
                //if rotation change need update render.
                mVideoRotation = bundle.getInt(EventKey.INT_DATA);
                PLog.d(TAG,"onVideoRotationChange : videoRotation = " + mVideoRotation);
                if(mRender!=null)
                    mRender.setVideoRotation(mVideoRotation);
            }
            //when prepared bind surface.
            else if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED){
                bindRenderHolder(mRenderHolder);
            }
            //when bufferPercentage update
            else if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_UPDATE){
                mBufferPercentage = bundle.getInt(EventKey.INT_DATA);
                PLog.d(TAG,"bufferUpdate : bufferPercentage = " + mBufferPercentage);
            }
            if(mOnPlayerEventListener!=null)
                mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
            //last dispatch event , because bundle will be recycle after dispatch.
            mViewContainer.dispatchPlayEvent(eventCode, bundle);
        }
    };

    private OnErrorEventListener mInternalErrorEventListener = new OnErrorEventListener() {
        @Override
        public void onErrorEvent(int eventCode, Bundle bundle) {
            PLog.e(TAG,"onError : code = " + eventCode
                    + ", Message = " + (bundle==null?"no message":bundle.toString()));
            if(mOnErrorEventListener!=null)
                mOnErrorEventListener.onErrorEvent(eventCode, bundle);
            //last dispatch event , because bundle will be recycle after dispatch.
            mViewContainer.dispatchErrorEvent(eventCode, bundle);
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
        public void onSurfaceChanged(IRender.IRenderHolder renderHolder, int format, int width, int height) {
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
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setRoundRectShape(float radius) {
        mStyleSetter.setRoundRectShape(radius);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setRoundRectShape(Rect rect, float radius) {
        mStyleSetter.setRoundRectShape(rect, radius);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setOvalRectShape() {
        mStyleSetter.setOvalRectShape();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setOvalRectShape(Rect rect) {
        mStyleSetter.setOvalRectShape(rect);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void clearShapeStyle() {
        mStyleSetter.clearShapeStyle();
    }

    @Override
    public void setElevationShadow(float elevation) {
        mStyleSetter.setElevationShadow(elevation);
    }

}

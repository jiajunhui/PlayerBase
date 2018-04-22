package com.kk.taurus.avplayer.play;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.kk.taurus.avplayer.App;
import com.kk.taurus.playerbase.AVPlayer;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.render.IRender;
import com.kk.taurus.playerbase.render.RenderTextureView;
import com.kk.taurus.playerbase.widget.ViewContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2018/4/15.
 */

public class SPlayer {

    private static SPlayer i;
    private AVPlayer mPlayer;

    private ViewContainer mViewContainer;
    private DataSource mDataSource;

    private Context mAppContext;

    private IRender.IRenderHolder mRenderHolder;

    private int mVideoRotation;
    private int mVideoWidth,mVideoHeight;
    private int mVideoSarNum,mVideoSarDen;

    private List<OnPlayerEventListener> onPlayerEventListeners;

    private SPlayer(){
        mAppContext = App.get().getApplicationContext();
        onPlayerEventListeners = new ArrayList<>();
        mPlayer = new AVPlayer(mAppContext);
        mViewContainer = new ViewContainer(mAppContext);
        mViewContainer.setBackgroundColor(Color.BLACK);
        mViewContainer.setReceiverGroup(ReceiverGroupManager.get().getLiteReceiverGroup(mAppContext));
    }

    public static SPlayer get(){
        if(null==i){
            synchronized (SPlayer.class){
                if(null==i){
                    i = new SPlayer();
                }
            }
        }
        return i;
    }

    public int[] getWH(){
        return new int[]{mVideoWidth,mVideoHeight};
    }

    public void addOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener){
        if(onPlayerEventListeners.contains(onPlayerEventListener))
            return;
        onPlayerEventListeners.add(onPlayerEventListener);
    }

    public void removeOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener){
        onPlayerEventListeners.remove(onPlayerEventListener);
    }

    private void dispatchEventListeners(int eventCode, Bundle bundle){
        for(OnPlayerEventListener listener:onPlayerEventListeners){
            listener.onPlayerEvent(eventCode, bundle);
        }
    }

    private OnReceiverEventListener mInternalReceiverEventListener = new OnReceiverEventListener() {
        @Override
        public void onReceiverEvent(int eventCode, Bundle bundle) {
            switch (eventCode){
                case EventConstant.EVENT_CODE_CONTROLLER_REQUEST_PAUSE:
                    pause();
                    break;
                case EventConstant.EVENT_CODE_CONTROLLER_REQUEST_RESUME:
                    resume();
                    break;
                case EventConstant.EVENT_CODE_CONTROLLER_REQUEST_SEEK:
                    seekTo(bundle.getInt(EventKey.INT_DATA));
                    break;
            }
        }
    };

    public void play(ViewGroup group, DataSource dataSource){
        resetLayoutContainer();
        final IRender render = new RenderTextureView(mAppContext);

        //if render change ,need update some params
        render.updateVideoSize(mVideoWidth, mVideoHeight);
        render.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
        render.setVideoRotation(mVideoRotation);

        mPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onPlayerEvent(int eventCode, Bundle bundle) {
                if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE){
                    mVideoWidth = bundle.getInt(EventKey.INT_ARG1);
                    mVideoHeight = bundle.getInt(EventKey.INT_ARG2);
                    mVideoSarNum = bundle.getInt(EventKey.INT_ARG3);
                    mVideoSarDen = bundle.getInt(EventKey.INT_ARG4);
                    render.updateVideoSize(mVideoWidth, mVideoHeight);
                    render.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
                }else if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED){
                    mVideoRotation = bundle.getInt(EventKey.INT_DATA);
                    render.setVideoRotation(mVideoRotation);
                }else if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED){
                    bindRenderHolder(mRenderHolder);
                }
                dispatchEventListeners(eventCode, bundle);
                mViewContainer.dispatchPlayEvent(eventCode, bundle);
            }
        });
        mPlayer.setOnErrorEventListener(new OnErrorEventListener() {
            @Override
            public void onErrorEvent(int eventCode, Bundle bundle) {
                mViewContainer.dispatchErrorEvent(eventCode, bundle);
            }
        });
        mViewContainer.setOnReceiverEventListener(mInternalReceiverEventListener);
        render.setRenderCallback(new IRender.IRenderCallback() {
            @Override
            public void onSurfaceCreated(IRender.IRenderHolder renderHolder, int width, int height) {
                mRenderHolder = renderHolder;
                bindRenderHolder(mRenderHolder);
            }
            @Override
            public void onSurfaceChanged(IRender.IRenderHolder renderHolder, int format, int width, int height) {

            }
            @Override
            public void onSurfaceDestroy(IRender.IRenderHolder renderHolder) {
                mPlayer.setSurface(null);
            }
        });
        mViewContainer.setRenderView(render.getRenderView());
        if(dataSource!=null){
            setDataSource(dataSource);
            start(0);
        }
        group.removeAllViews();
        group.addView(mViewContainer,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void bindRenderHolder(IRender.IRenderHolder renderHolder){
        if(renderHolder!=null)
            renderHolder.bindPlayer(mPlayer);
    }

    /**
     * 找到container的父布局并移除container
     */
    private void resetLayoutContainer(){
        ViewParent parent = mViewContainer.getParent();
        if(parent!=null && parent instanceof ViewGroup){
            ((ViewGroup)parent).removeAllViews();
        }
    }

    public void setDataSource(DataSource dataSource){
        mDataSource = dataSource;
        mPlayer.setDataSource(dataSource);
    }

    public boolean isEqualDataSource(DataSource dataSource){
        if(dataSource==null)
            return false;
        return mDataSource!=null && mDataSource.getData().equals(dataSource.getData());
    }

    public void start(int msc){
        mPlayer.start(msc);
    }

    public void pause(){
        mPlayer.pause();
    }

    public void resume(){
        mPlayer.resume();
    }

    public void seekTo(int msc){
        mPlayer.seekTo(msc);
    }

    public boolean isPlaying(){
        return mPlayer.isPlaying();
    }

    public void stop(){
        mPlayer.stop();
    }

    public void destroy(){
        if(mPlayer!=null)
            mPlayer.destroy();
        i = null;
    }

}

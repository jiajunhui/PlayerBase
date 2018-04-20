package com.kk.taurus.avplayer.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.EventConstant;
import com.kk.taurus.avplayer.play.MonitorDataProvider;
import com.kk.taurus.avplayer.play.ReceiverGroupManager;
import com.kk.taurus.avplayer.utils.PUtil;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.render.AspectRatio;
import com.kk.taurus.playerbase.widget.BaseVideoView;

/**
 * Created by Taurus on 2018/4/19.
 */

public class VideoViewActivity extends AppCompatActivity implements OnReceiverEventListener, OnPlayerEventListener {

    private BaseVideoView mVideoView;

    private int margin;
    private DataSource mDataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_video_view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        margin = PUtil.dip2px(this,2);

        mVideoView = findViewById(R.id.videoView);

        updateVideo(false);

        mDataSource = new DataSource("monitor_id");

        mVideoView.setOnPlayerEventListener(this);
        mVideoView.setOnReceiverEventListener(this);
        mVideoView.setReceiverGroup(ReceiverGroupManager.get().getReceiverGroup(this));

        //设置数据提供者 MonitorDataProvider
        mVideoView.setDataProvider(new MonitorDataProvider());
        mVideoView.setDataSource(mDataSource);
        mVideoView.start();

        // If you want to start play at a specified time,
        // please set this method.
        //mVideoView.start(30*1000);
    }

    public void setRenderSurfaceView(View view){
        mVideoView.setRenderType(BaseVideoView.RENDER_TYPE_SURFACE_VIEW);
    }

    public void setRenderTextureView(View view){
        mVideoView.setRenderType(BaseVideoView.RENDER_TYPE_TEXTURE_VIEW);
    }

    public void onStyleSetRoundRect(View view){
        mVideoView.setRoundRectShape(PUtil.dip2px(this,25));
    }

    public void onStyleSetOvalRect(View view){
        mVideoView.setOvalRectShape();
    }

    public void onShapeStyleReset(View view){
        mVideoView.clearShapeStyle();
    }

    public void onAspect16_9(View view){
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_16_9);
    }

    public void onAspect4_3(View view){
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_4_3);
    }

    public void onAspectFill(View view){
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_FILL_PARENT);
    }

    public void onAspectFit(View view){
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_FIT_PARENT);
    }

    public void onAspectOrigin(View view){
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_ORIGIN);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            updateVideo(true);
        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            updateVideo(false);
        }
    }

    private void updateVideo(boolean landscape){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
        if(landscape){
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.setMargins(0, 0, 0, 0);
        }else{
            layoutParams.width = PUtil.getScreenW(this) - (margin*2);
            layoutParams.height = layoutParams.width * 9/16;
            layoutParams.setMargins(margin, margin, margin, margin);
        }
        mVideoView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case EventConstant.EVENT_CODE_CONTROLLER_REQUEST_PAUSE:
                mVideoView.pause();
                break;
            case EventConstant.EVENT_CODE_CONTROLLER_REQUEST_RESUME:
                mVideoView.resume();
                break;
            case EventConstant.EVENT_CODE_CONTROLLER_REQUEST_SEEK:
                mVideoView.seekTo(bundle.getInt(EventKey.INT_DATA));
                break;
            case EventConstant.EVENT_CODE_COMPLETE_REQUEST_REPLAY:
                mVideoView.setDataProvider(new MonitorDataProvider());
                mVideoView.setDataSource(mDataSource);
                mVideoView.start();
                break;
            case EventConstant.EVENT_CODE_COMPLETE_REQUEST_NEXT:
                mVideoView.setDataSource(mDataSource);
                mVideoView.start();
                break;
        }
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:

                break;
        }
    }
}

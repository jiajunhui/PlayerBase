package com.kk.taurus.avplayer.ui;

import android.Manifest;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.kk.taurus.avplayer.App;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.EventConstant;
import com.kk.taurus.avplayer.play.MonitorDataProvider;
import com.kk.taurus.avplayer.play.ReceiverGroupManager;
import com.kk.taurus.avplayer.utils.PUtil;
import com.kk.taurus.avplayer.view.VisualizerView;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.render.AspectRatio;
import com.kk.taurus.playerbase.widget.BaseVideoView;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Taurus on 2018/4/19.
 */

public class VideoViewActivity extends AppCompatActivity implements OnReceiverEventListener, OnPlayerEventListener {

    private BaseVideoView mVideoView;
    private VisualizerView mMusicWave;

    private Visualizer mVisualizer;

    private int margin;
    private DataSource mDataSource;
    private String mCurrUrl;
    private MonitorDataProvider mMonitorDataProvider;

    private boolean permissionSuccess;

    private byte[] waveType = new byte[]{
            VisualizerView.WAVE_TYPE_BROKEN_LINE,
            VisualizerView.WAVE_TYPE_RECTANGLE,
            VisualizerView.WAVE_TYPE_CURVE};

    private int typeIndex;
    private ReceiverGroup mReceiverGroup;

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
        mMusicWave = findViewById(R.id.musicWave);

        mMusicWave.setWaveType(waveType[typeIndex]);
        mMusicWave.setColors(new int[]{Color.YELLOW, Color.BLUE});

        mDataSource = new DataSource();
        mDataSource.setId(666);

        PermissionGen.with(this)
                .addRequestCode(101)
                .permissions(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS
                )
                .request();

        mMusicWave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permissionSuccess){
                    typeIndex++;
                    typeIndex %= waveType.length;
                    mMusicWave.setWaveType(waveType[typeIndex]);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 101)
    public void permissionSuccess() {
        permissionSuccess = true;
        initPlay();
    }

    @PermissionFail(requestCode = 101)
    public void permissionFailure(){
        permissionSuccess = false;
        initPlay();
    }

    private void initPlay() {
        updateVideo(false);

        mVideoView.setOnPlayerEventListener(this);
        mVideoView.setOnReceiverEventListener(this);
        mReceiverGroup = ReceiverGroupManager.get().getReceiverGroup(this);
        mVideoView.setReceiverGroup(mReceiverGroup);

        //设置数据提供者 MonitorDataProvider
        mMonitorDataProvider = new MonitorDataProvider();
        mVideoView.setDataProvider(mMonitorDataProvider);
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

    public void onDecoderChangeMediaPlayer(View view){
        int curr = mVideoView.getCurrentPosition();
        if(mVideoView.switchDecoder(PlayerConfig.DEFAULT_PLAN_ID)){
            replay(curr);
        }
    }

    public void onDecoderChangeIjkPlayer(View view){
        int curr = mVideoView.getCurrentPosition();
        if(mVideoView.switchDecoder(App.PLAN_ID_IJK)){
            replay(curr);
        }
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
        releaseVisualizer();
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
                replay(0);
                break;
            case EventConstant.EVENT_CODE_COMPLETE_REQUEST_NEXT:
                mVideoView.setDataProvider(mMonitorDataProvider);
                mVideoView.setDataSource(mDataSource);
                mVideoView.start();
                break;
            case EventConstant.EVENT_CODE_ERROR_REQUEST_RETRY:
                replay(0);
                break;
        }
    }

    private void replay(int msc){
        if(TextUtils.isEmpty(mCurrUrl)){
            mVideoView.setDataProvider(mMonitorDataProvider);
            mVideoView.setDataSource(mDataSource);
        }else{
            mVideoView.setDataProvider(null);
            mVideoView.setDataSource(new DataSource(mCurrUrl));
        }
        mVideoView.start(msc);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
                DataSource dataSource = (DataSource) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
                mCurrUrl = dataSource.getData();
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
                releaseVisualizer();
                updateVisualizer();
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:

                break;
        }
    }

    private void releaseVisualizer() {
        if(mVisualizer!=null)
            mVisualizer.release();
    }

    private void updateVisualizer() {
        if(!permissionSuccess)
            return;
        mVisualizer = new Visualizer(mVideoView.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        mMusicWave.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);
    }
}

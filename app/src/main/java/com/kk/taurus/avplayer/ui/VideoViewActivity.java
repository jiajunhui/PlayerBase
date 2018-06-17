package com.kk.taurus.avplayer.ui;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kk.taurus.avplayer.App;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.cover.ControllerCover;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.play.MonitorDataProvider;
import com.kk.taurus.avplayer.play.ReceiverGroupManager;
import com.kk.taurus.avplayer.utils.PUtil;
import com.kk.taurus.avplayer.view.VisualizerView;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.IReceiver;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.render.AspectRatio;
import com.kk.taurus.playerbase.render.IRender;
import com.kk.taurus.playerbase.widget.BaseVideoView;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Taurus on 2018/4/19.
 */

public class VideoViewActivity extends AppCompatActivity implements OnPlayerEventListener {

    private BaseVideoView mVideoView;
    private VisualizerView mMusicWave;

    private Visualizer mVisualizer;

    private int margin;

    private boolean permissionSuccess;

    private byte[] waveType = new byte[]{
            VisualizerView.WAVE_TYPE_BROKEN_LINE,
            VisualizerView.WAVE_TYPE_RECTANGLE,
            VisualizerView.WAVE_TYPE_CURVE};

    private int typeIndex;
    private ReceiverGroup mReceiverGroup;
    private boolean isLandscape;

    private long mDataSourceId;

    private boolean userPause;

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
        mVideoView.setEventHandler(mOnEventAssistHandler);
        mReceiverGroup = ReceiverGroupManager.get().getReceiverGroup(this, null);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_NETWORK_RESOURCE, true);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, true);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_HAS_NEXT, true);
        mVideoView.setReceiverGroup(mReceiverGroup);

        //设置数据提供者 MonitorDataProvider
        MonitorDataProvider dataProvider = new MonitorDataProvider();
        mVideoView.setDataProvider(dataProvider);
        mVideoView.setDataSource(generatorDataSource(mDataSourceId));
        mVideoView.start();

        // If you want to start play at a specified time,
        // please set this method.
        //mVideoView.start(30*1000);
    }

    private DataSource generatorDataSource(long id){
        DataSource dataSource = new DataSource();
        dataSource.setId(id);
        return dataSource;
    }

    public void setRenderSurfaceView(View view){
        mVideoView.setRenderType(IRender.RENDER_TYPE_SURFACE_VIEW);
    }

    public void setRenderTextureView(View view){
        mVideoView.setRenderType(IRender.RENDER_TYPE_TEXTURE_VIEW);
    }

    public void onStyleSetRoundRect(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mVideoView.setRoundRectShape(PUtil.dip2px(this,25));
        }else{
            Toast.makeText(this, "not support", Toast.LENGTH_SHORT).show();
        }
    }

    public void onStyleSetOvalRect(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mVideoView.setOvalRectShape();
        }else{
            Toast.makeText(this, "not support", Toast.LENGTH_SHORT).show();
        }
    }

    public void onShapeStyleReset(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mVideoView.clearShapeStyle();
        }else{
            Toast.makeText(this, "not support", Toast.LENGTH_SHORT).show();
        }
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

    public void onDecoderChangeExoPlayer(View view){
        int curr = mVideoView.getCurrentPosition();
        if(mVideoView.switchDecoder(App.PLAN_ID_EXO)){
            replay(curr);
        }
    }

    public void removeControllerCover(View view){
        mReceiverGroup.removeReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER);
        Toast.makeText(this, "已移除", Toast.LENGTH_SHORT).show();
    }

    public void addControllerCover(View view){
        IReceiver receiver = mReceiverGroup.getReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER);
        if(receiver==null){
            mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER, new ControllerCover(this));
            Toast.makeText(this, "已添加", Toast.LENGTH_SHORT).show();
        }
    }

    public void halfSpeedPlay(View view){
        mVideoView.setSpeed(0.5f);
    }

    public void doubleSpeedPlay(View view){
        mVideoView.setSpeed(2f);
    }

    public void normalSpeedPlay(View view){
        mVideoView.setSpeed(1f);
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
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!userPause)
            mVideoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
        releaseVisualizer();
    }

    @Override
    public void onBackPressed() {
        if(isLandscape){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            isLandscape = true;
            updateVideo(true);
        }else{
            isLandscape = false;
            updateVideo(false);
        }
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandscape);
    }

    private OnVideoViewEventHandler mOnEventAssistHandler = new OnVideoViewEventHandler(){
        @Override
        public void onAssistHandle(BaseVideoView assist, int eventCode, Bundle bundle) {
            super.onAssistHandle(assist, eventCode, bundle);
            switch (eventCode){
                case DataInter.Event.CODE_REQUEST_PAUSE:
                    userPause = true;
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                    if(isLandscape){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }else{
                        finish();
                    }
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_NEXT:
                    mDataSourceId++;
                    mVideoView.setDataSource(generatorDataSource(mDataSourceId));
                    mVideoView.start();
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                    setRequestedOrientation(isLandscape ?
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case DataInter.Event.EVENT_CODE_ERROR_SHOW:
                    mVideoView.stop();
                    break;
            }
        }
    };

    private void replay(int msc){
        mVideoView.setDataSource(generatorDataSource(mDataSourceId));
        mVideoView.start(msc);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
                releaseVisualizer();
                updateVisualizer();
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:

                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESUME:
                userPause = false;
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

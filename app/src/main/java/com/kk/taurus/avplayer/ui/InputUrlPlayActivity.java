package com.kk.taurus.avplayer.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.cover.ControllerCover;
import com.kk.taurus.avplayer.cover.ErrorCover;
import com.kk.taurus.avplayer.cover.LoadingCover;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.utils.PUtil;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.render.AspectRatio;
import com.kk.taurus.playerbase.widget.BaseVideoView;

public class InputUrlPlayActivity extends AppCompatActivity {

    private BaseVideoView mVideoView;
    private View mInputLayout;
    private EditText mInputUrl;
    private boolean isLandScape;
    private ReceiverGroup mReceiverGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_url_play);
        mVideoView = findViewById(R.id.urlVideoView);
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_FIT_PARENT);
        mInputLayout = findViewById(R.id.inputLayout);
        mInputUrl = findViewById(R.id.editUrl);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mVideoView.setEventHandler(mOnEventAssistHandler);
        mReceiverGroup = new ReceiverGroup();
        mReceiverGroup.addReceiver("load_cover", new LoadingCover(this));
        mReceiverGroup.addReceiver("error_cover", new ErrorCover(this));
        mReceiverGroup.addReceiver("controller_cover", new ControllerCover(this));
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, false);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_NETWORK_RESOURCE, true);
        mVideoView.setReceiverGroup(mReceiverGroup);
    }

    private OnVideoViewEventHandler mOnEventAssistHandler = new OnVideoViewEventHandler(){
        @Override
        public void onAssistHandle(BaseVideoView assist, int eventCode, Bundle bundle) {
            super.onAssistHandle(assist, eventCode, bundle);
            switch (eventCode){
                case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                    if(isLandScape){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }else{
                        finish();
                    }
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                    setRequestedOrientation(isLandScape ?
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case DataInter.Event.EVENT_CODE_ERROR_SHOW:
                    mVideoView.stop();
                    break;
            }
        }
        @Override
        public void requestRetry(BaseVideoView videoView, Bundle bundle) {
            if(PUtil.isTopActivity(InputUrlPlayActivity.this)){
                super.requestRetry(videoView, bundle);
            }
        }
    };

    public void playUrl(View view){
        String url = mInputUrl.getText().toString();
        if(TextUtils.isEmpty(url)){
            Toast.makeText(this, "请输入地址!", Toast.LENGTH_SHORT).show();
        }else{
            DataSource dataSource = new DataSource(url);
            mReceiverGroup.getGroupValue().putObject(DataInter.Key.KEY_DATA_SOURCE, dataSource);
            mVideoView.stop();
            mVideoView.setDataSource(dataSource);
            mVideoView.start();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLandScape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        mInputLayout.setVisibility(isLandScape?View.GONE:View.VISIBLE);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandScape);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, isLandScape);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }
}

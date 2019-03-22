package com.kk.taurus.avplayer.ui.window;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.cover.CloseCover;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.play.DemoDataProvider;
import com.kk.taurus.avplayer.play.ReceiverGroupManager;
import com.kk.taurus.avplayer.utils.PUtil;
import com.kk.taurus.avplayer.utils.WindowPermissionCheck;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.provider.IDataProvider;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.widget.BaseVideoView;
import com.kk.taurus.playerbase.window.FloatWindowParams;
import com.kk.taurus.playerbase.window.WindowVideoView;

public class WindowVideoViewActivity extends AppCompatActivity {

    Button mActiveWindow;

    WindowVideoView mWindowVideoView;

    DataSource mDataSource;

    IDataProvider mDataProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_video_view);
        mActiveWindow = findViewById(R.id.btn_active_window);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int SW = getResources().getDisplayMetrics().widthPixels;
        int width = (int) (SW * 0.7f);
        int height = width * 9/16;

        int type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//8.0+
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            type =  WindowManager.LayoutParams.TYPE_PHONE;
        }

        mWindowVideoView = new WindowVideoView(this,
                new FloatWindowParams()
                        .setWindowType(type)
                        .setX(100)
                        .setY(100)
                        .setWidth(width)
                        .setHeight(height));

        mWindowVideoView.setBackgroundColor(Color.BLACK);

        mWindowVideoView.setEventHandler(eventHandler);

        ReceiverGroup receiverGroup = ReceiverGroupManager.get().getLiteReceiverGroup(this);
        receiverGroup.addReceiver(DataInter.ReceiverKey.KEY_CLOSE_COVER, new CloseCover(this));
        receiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_NETWORK_RESOURCE, true);
        receiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, false);
        receiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_SCREEN_SWITCH_ENABLE, false);

        mWindowVideoView.setReceiverGroup(receiverGroup);

        mDataSource = new DataSource();
        mDataSource.setId(1234567);
        mWindowVideoView.setDataProvider(mDataProvider = new DemoDataProvider());
        mWindowVideoView.setDataSource(mDataSource);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WindowPermissionCheck.onActivityResult(this, requestCode, resultCode, data, null);
    }

    private OnVideoViewEventHandler eventHandler = new OnVideoViewEventHandler(){
        @Override
        public void onAssistHandle(BaseVideoView assist, int eventCode, Bundle bundle) {
            super.onAssistHandle(assist, eventCode, bundle);
            switch (eventCode){
                case DataInter.Event.EVENT_CODE_ERROR_SHOW:
                    mWindowVideoView.stop();
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_CLOSE:
                    mWindowVideoView.close();
                    mActiveWindow.setText(R.string.open_window_video_view);
                    break;
            }
        }
        @Override
        public void requestRetry(BaseVideoView assist, Bundle bundle) {
            if(PUtil.isTopActivity(WindowVideoViewActivity.this)){
                super.requestRetry(assist, bundle);
            }
        }
    };

    public void activeWindowVideoView(View view){
        if(mWindowVideoView.isWindowShow()){
            mActiveWindow.setText(R.string.open_window_video_view);
            mWindowVideoView.close();
        }else{
            if(WindowPermissionCheck.checkPermission(this)){
                mActiveWindow.setText(R.string.close_window_video_view);
                mWindowVideoView.setElevationShadow(20);
                mWindowVideoView.show();
                mWindowVideoView.setDataSource(mDataSource);
                mWindowVideoView.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        int state = mWindowVideoView.getState();
        if(state == IPlayer.STATE_PLAYBACK_COMPLETE)
            return;
        mWindowVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int state = mWindowVideoView.getState();
        if(state == IPlayer.STATE_PLAYBACK_COMPLETE)
            return;
        if(mWindowVideoView.isInPlaybackState())
            mWindowVideoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWindowVideoView.close();
        mWindowVideoView.stopPlayback();
        mDataProvider.destroy();
    }
}


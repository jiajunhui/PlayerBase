package com.kk.taurus.avplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kk.taurus.avplayer.App;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.play.MonitorDataProvider;
import com.kk.taurus.avplayer.play.ReceiverGroupManager;
import com.kk.taurus.playerbase.assist.AssistPlay;
import com.kk.taurus.playerbase.assist.OnAssistPlayEventHandler;
import com.kk.taurus.playerbase.assist.OnEventAssistHandler;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.kk.taurus.playerbase.receiver.GroupValue;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.assist.RelationAssist;
import com.kk.taurus.playerbase.window.FloatWindow;

public class MainActivity extends AppCompatActivity implements OnReceiverEventListener {

    private TextView mInfo;
    private Toolbar mToolBar;

    private View mWindowView;

    private boolean isWindowShow;

    private FrameLayout mVideoContainer;
    private FrameLayout mFullScreenContainer;

    private MonitorDataProvider mDataProvider;

    private FloatWindow mFloatWindow;

    private RelationAssist mRelationAssist;

    private boolean isLandScape;

    private GroupValue mGroupValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = findViewById(R.id.id_toolbar);
        mInfo = findViewById(R.id.tv_info);
        mFullScreenContainer = findViewById(R.id.fullScreenContainer);

        setSupportActionBar(mToolBar);

        mGroupValue = new GroupValue();
        mRelationAssist = new RelationAssist(this);
        mRelationAssist.setEventAssistHandler(onEventAssistHandler);

        mDataProvider = new MonitorDataProvider();
        mRelationAssist.setDataProvider(mDataProvider);
        mRelationAssist.getViewContainer().setBackgroundColor(Color.BLACK);
        mRelationAssist.getViewContainer().setGestureScrollEnable(false);
        ReceiverGroup receiverGroup = ReceiverGroupManager.get()
                .getLiteReceiverGroup(this, mGroupValue);
        mRelationAssist.setReceiverGroup(receiverGroup);

        mRelationAssist.setOnReceiverEventListener(this);

        updateDecoderInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.switchMediaPlayer:
                PlayerConfig.setDefaultPlanId(PlayerConfig.DEFAULT_PLAN_ID);
                updateDecoderInfo();
                break;
            case R.id.switchIjkPlayer:
                PlayerConfig.setDefaultPlanId(App.PLAN_ID_IJK);
                updateDecoderInfo();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDecoderInfo();
    }

    private void updateDecoderInfo() {
        DecoderPlan defaultPlan = PlayerConfig.getDefaultPlan();
        mInfo.setText("当前解码方案为:" + defaultPlan.getDesc());
    }

    public void useBaseVideoView(View v){
        intentTo(VideoViewActivity.class);
    }

    public void useWindowManagerPlay(View v){
        showWindow();
    }

    private void showWindow() {
        if(isWindowShow)
            return;
        if(mWindowView==null){
            mWindowView = View.inflate(this, R.layout.layout_window, null);
        }
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int width = (int) (widthPixels * 0.8f);
        if(mFloatWindow ==null){
            mFloatWindow = new FloatWindow(this, new FloatWindow.FloatViewParams()
                    .setView(mWindowView)
                    .setX(100)
                    .setY(100)
                    .setWidth(width).setHeight(width*9/16));
        }
        mVideoContainer = mWindowView.findViewById(R.id.layoutContainer);
        mVideoContainer.removeAllViews();
        mWindowView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyWindow();
                mRelationAssist.stop();
            }
        });
        mWindowView.findViewById(R.id.album_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRelationAssist.attachContainer(mVideoContainer);
                mRelationAssist.setDataSource(new DataSource("monitor_id"));
                mRelationAssist.play();
            }
        });

        mFloatWindow.addToWindow();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isWindowShow = true;
    }

    private void destroyWindow(){
        if(isWindowShow && mFloatWindow !=null){
            mFloatWindow.removeFromWindow();
            isWindowShow = false;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void useAVPlayerLocalVideos(View v){
        intentTo(LocalVideoListActivity.class);
    }

    public void useAVPlayerOnlineVideos(View v){
        intentTo(OnlineVideoListActivity.class);
    }

    private void intentTo(Class<? extends Activity> cls){
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRelationAssist.destroy();
        destroyWindow();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLandScape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private OnEventAssistHandler onEventAssistHandler =
            new OnAssistPlayEventHandler(){
                @Override
                public void onAssistHandle(AssistPlay assistPlay, int eventCode, Bundle bundle) {
                    super.onAssistHandle(assistPlay, eventCode, bundle);
                    switch (eventCode){
                        case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                            onBackPressed();
                            break;
                        case DataInter.Event.EVENT_CODE_ERROR_SHOW:
                            mRelationAssist.stop();
                            break;
                        case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                            if(isLandScape){
                                quitFullScreen();
                            }else{
                                enterFullScreen();
                            }
                            break;
                    }
                }
            };

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {
        switch (eventCode){

        }
    }

    private void enterFullScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mRelationAssist.getViewContainer().setGestureScrollEnable(true);
        ReceiverGroup receiverGroup = ReceiverGroupManager.get().getReceiverGroup(this, mGroupValue);
        receiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE,true);
        receiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, true);
        mRelationAssist.setReceiverGroup(receiverGroup);
        destroyWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mToolBar.setVisibility(View.GONE);
        mRelationAssist.attachContainer(mFullScreenContainer);
    }

    private void quitFullScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mRelationAssist.getViewContainer().setGestureScrollEnable(false);
        ReceiverGroup receiverGroup = ReceiverGroupManager.get().getLiteReceiverGroup(this, mGroupValue);
        receiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE,false);
        receiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, false);
        mRelationAssist.setReceiverGroup(receiverGroup);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mToolBar.setVisibility(View.VISIBLE);
        showWindow();
        mRelationAssist.attachContainer(mVideoContainer);
    }

    @Override
    public void onBackPressed() {
        if(isLandScape){
            quitFullScreen();
            return;
        }
        super.onBackPressed();
    }

}

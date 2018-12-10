package com.kk.taurus.avplayer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.bean.VideoBean;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.play.ListPlayer;
import com.kk.taurus.avplayer.play.OnHandleListener;
import com.kk.taurus.avplayer.utils.OrientationSensor;
import com.kk.taurus.playerbase.entity.DataSource;

import com.kk.taurus.avplayer.base.ISPayer;
import com.kk.taurus.playerbase.player.IPlayer;

public class DetailPlayActivity extends AppCompatActivity implements OnHandleListener {

    private static final String KEY_GO_PLAY = "go_on_play";
    private static final String KEY_ITEM_DATA = "item_data";

    private RelativeLayout mPlayerContainer;

    private boolean isLandScape;

    private OrientationSensor mOrientationSensor;

    public static void launch(Context context, boolean goOnPlay, VideoBean item){
        Intent intent = new Intent(context, DetailPlayActivity.class);
        intent.putExtra(KEY_GO_PLAY, goOnPlay);
        intent.putExtra(KEY_ITEM_DATA, item);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mPlayerContainer = findViewById(R.id.layoutContainer);

        boolean goOnPlay = getIntent().getBooleanExtra(KEY_GO_PLAY, false);
        VideoBean item = (VideoBean) getIntent().getSerializableExtra(KEY_ITEM_DATA);

        ListPlayer.get().setReceiverConfigState(this, ISPayer.RECEIVER_GROUP_CONFIG_DETAIL_PORTRAIT_STATE);
        ListPlayer.get().attachContainer(mPlayerContainer);
        ListPlayer.get().setOnHandleListener(this);
        ListPlayer.get().updateGroupValue(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, true);
        if(!goOnPlay){
            ListPlayer.get().play(new DataSource(item.getPath()));
        }

        mOrientationSensor = new OrientationSensor(this, onOrientationListener);
    }

    private OrientationSensor.OnOrientationListener onOrientationListener =
            new OrientationSensor.OnOrientationListener() {
        @Override
        public void onLandScape(int orientation) {
            if(ListPlayer.get().isInPlaybackState()){
                setRequestedOrientation(orientation);
            }
        }
        @Override
        public void onPortrait(int orientation) {
            if(ListPlayer.get().isInPlaybackState()){
                setRequestedOrientation(orientation);
            }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLandScape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void finish() {
        ListPlayer.get().stop();
        mOrientationSensor.disable();
        super.finish();
    }

    private void toggleScreen(){
        setRequestedOrientation(isLandScape?
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onBackPressed() {
        if(isLandScape){
            toggleScreen();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int state = ListPlayer.get().getState();
        if(state == IPlayer.STATE_PLAYBACK_COMPLETE)
            return;
        if(ListPlayer.get().isInPlaybackState()){
            ListPlayer.get().resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        int state = ListPlayer.get().getState();
        if(state == IPlayer.STATE_PLAYBACK_COMPLETE)
            return;
        if(ListPlayer.get().isInPlaybackState())
            ListPlayer.get().pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ListPlayer.get().attachActivity(this);
        mOrientationSensor.enable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mOrientationSensor.disable();
    }

    @Override
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void onToggleScreen() {
        toggleScreen();
    }
}

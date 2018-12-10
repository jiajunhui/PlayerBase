package com.kk.taurus.avplayer.ui.listplay;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.bean.VideoBean;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.play.ListPlayer;
import com.kk.taurus.avplayer.ui.DetailPlayActivity;
import com.kk.taurus.avplayer.play.OnHandleListener;
import com.kk.taurus.avplayer.utils.DataUtils;
import com.kk.taurus.avplayer.utils.OrientationSensor;
import com.kk.taurus.playerbase.entity.DataSource;

import com.kk.taurus.avplayer.adapter.ListAdapter;
import com.kk.taurus.avplayer.base.ISPayer;
import com.kk.taurus.playerbase.player.IPlayer;

public class ListPlayActivity extends AppCompatActivity implements ListAdapter.OnListListener {

    private RecyclerView mRecycler;
    private FrameLayout mPlayerContainer;

    private boolean isLandScape;
    private ListAdapter mAdapter;
    private boolean toDetail;

    private OrientationSensor mOrientationSensor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mRecycler = findViewById(R.id.recycler);
        mPlayerContainer = findViewById(R.id.listPlayContainer);

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ListAdapter(this, mRecycler, DataUtils.getVideoList());
        mAdapter.setOnListListener(this);
        mRecycler.setAdapter(mAdapter);

        mOrientationSensor = new OrientationSensor(this,onOrientationListener);
        mOrientationSensor.enable();

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
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            mPlayerContainer.setBackgroundColor(Color.BLACK);
            ListPlayer.get().attachContainer(mPlayerContainer, false);
            ListPlayer.get().setReceiverConfigState(this, ISPayer.RECEIVER_GROUP_CONFIG_FULL_SCREEN_STATE);
        }else{
            mPlayerContainer.setBackgroundColor(Color.TRANSPARENT);
            mRecycler.post(new Runnable() {
                @Override
                public void run() {
                    ListAdapter.VideoItemHolder currentHolder = mAdapter.getCurrentHolder();
                    if(currentHolder!=null){
                        ListPlayer.get().attachContainer(currentHolder.layoutContainer, false);
                        ListPlayer.get().setReceiverConfigState(
                                ListPlayActivity.this, ISPayer.RECEIVER_GROUP_CONFIG_LIST_STATE);
                    }
                }
            });
        }
        ListPlayer.get().updateGroupValue(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, isLandScape);
        ListPlayer.get().updateGroupValue(DataInter.Key.KEY_IS_LANDSCAPE, isLandScape);
    }

    private void toggleScreen(){
        setRequestedOrientation(isLandScape?
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListPlayer.get().updateGroupValue(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, isLandScape);
        ListPlayer.get().setOnHandleListener(new OnHandleListener() {
            @Override
            public void onBack() {
                onBackPressed();
            }
            @Override
            public void onToggleScreen() {
                toggleScreen();
            }
        });
        if(!toDetail && ListPlayer.get().isInPlaybackState()){
            ListPlayer.get().resume();
        }
        toDetail = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        int state = ListPlayer.get().getState();
        if(state == IPlayer.STATE_PLAYBACK_COMPLETE)
            return;
        if(!toDetail){
            ListPlayer.get().pause();
        }
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
    public void onBackPressed() {
        if(isLandScape){
            toggleScreen();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onTitleClick(ListAdapter.VideoItemHolder holder, VideoBean item, int position) {
        toDetail = true;
        DetailPlayActivity.launch(this, mAdapter.getPlayPosition()==position, item);
        mAdapter.reset();
    }

    @Override
    public void playItem(ListAdapter.VideoItemHolder holder, VideoBean item, int position) {
        ListPlayer.get().setReceiverConfigState(this, ISPayer.RECEIVER_GROUP_CONFIG_LIST_STATE);
        ListPlayer.get().attachContainer(holder.layoutContainer);
        ListPlayer.get().play(new DataSource(item.getPath()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrientationSensor.disable();
        ListPlayer.get().destroy();
    }
}

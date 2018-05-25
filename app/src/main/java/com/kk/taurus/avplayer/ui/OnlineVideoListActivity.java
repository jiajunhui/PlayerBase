package com.kk.taurus.avplayer.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.adapter.VideoListAdapter;
import com.kk.taurus.avplayer.bean.VideoBean;
import com.kk.taurus.avplayer.play.AssistPlayer;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.play.ReceiverGroupManager;
import com.kk.taurus.avplayer.utils.DataUtils;
import com.kk.taurus.avplayer.utils.OrientationHelper;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.GroupValue;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2018/4/15.
 */

public class OnlineVideoListActivity extends AppCompatActivity implements
        VideoListAdapter.OnListListener,
        OrientationHelper.OnOrientationListener,
        OnReceiverEventListener, OnPlayerEventListener {

    private List<VideoBean> mItems = new ArrayList<>();
    private VideoListAdapter mAdapter;

    private RecyclerView mRecycler;
    private FrameLayout mContainer;

    private boolean toDetail;
    private boolean isLandScape;

    private OrientationHelper mOrientationHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setTitle("在线视频");

        mOrientationHelper = new OrientationHelper(this, this);

        mRecycler = findViewById(R.id.recycler);
        mContainer = findViewById(R.id.listPlayContainer);
        mRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        AssistPlayer.get().addOnReceiverEventListener(this);
        AssistPlayer.get().addOnPlayerEventListener(this);

        ReceiverGroup receiverGroup = ReceiverGroupManager.get().getLiteReceiverGroup(this);
        receiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_NETWORK_RESOURCE, true);
        AssistPlayer.get().setReceiverGroup(receiverGroup);

        mItems.addAll(DataUtils.getVideoList());
        mAdapter = new VideoListAdapter(getApplicationContext(), mRecycler, mItems);
        mAdapter.setOnListListener(OnlineVideoListActivity.this);
        mRecycler.setAdapter(mAdapter);

    }

    private ReceiverGroup getReceiverGroup(){
        return AssistPlayer.get().getReceiverGroup();
    }

    private GroupValue getGroupValue(){
        if(getReceiverGroup()!=null){
            return getReceiverGroup().getGroupValue();
        }
        return null;
    }

    @Override
    public void onSensorUserAgreement() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLandScape = newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE;
        mOrientationHelper.onActivityConfigChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            attachFullScreen();
        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            attachList();
        }
        if(getGroupValue()!=null){
            getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandScape);
        }
    }

    private void attachFullScreen(){
        if(AssistPlayer.get().isPlaying())
            AssistPlayer.get().play(mContainer,null);
    }

    @Override
    public void onTitleClick(VideoBean item, int position) {
        toDetail = true;
        Intent intent = new Intent(this, DetailPageActivity.class);
        intent.putExtra(DetailPageActivity.KEY_ITEM, item);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toDetail = false;
        if(isLandScape){
            attachFullScreen();
        }else{
            attachList();
        }
        AssistPlayer.get().resume();
        if(getGroupValue()!=null){
            getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!toDetail){
            AssistPlayer.get().pause();
        }
    }

    private void attachList() {
        if(mAdapter!=null){
            mAdapter.getListPlayLogic().attachPlay();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AssistPlayer.get().removeReceiverEventListener(this);
        AssistPlayer.get().removePlayerEventListener(this);
        AssistPlayer.get().destroy();
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                setRequestedOrientation(isLandScape?
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                break;
        }
    }
}

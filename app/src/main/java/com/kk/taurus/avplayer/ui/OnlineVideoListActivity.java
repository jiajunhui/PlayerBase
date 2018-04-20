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

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.adapter.VideoListAdapter;
import com.kk.taurus.avplayer.play.SPlayer;
import com.kk.taurus.avplayer.utils.DataUtils;
import com.kk.taurus.avplayer.utils.OrientationHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2018/4/15.
 */

public class OnlineVideoListActivity extends AppCompatActivity
        implements VideoListAdapter.OnListListener, OrientationHelper.OnOrientationListener{

    private List<VideoItem> mItems = new ArrayList<>();
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
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mItems.addAll(DataUtils.getRemoteVideoItems());
        mAdapter = new VideoListAdapter(getApplicationContext(), mRecycler, mItems);
        mAdapter.setOnListListener(OnlineVideoListActivity.this);
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    public void onOrientationChange(boolean reverse, int orientation, int angle) {
        if(toDetail)
            return;
        if(orientation==Configuration.ORIENTATION_LANDSCAPE){
            if(SPlayer.get().isPlaying()){
                setRequestedOrientation(reverse?
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }else if(orientation==Configuration.ORIENTATION_PORTRAIT){
            if(SPlayer.get().isPlaying()){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    @Override
    public void onSensorUserAgreement() {

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
    }

    private void attachFullScreen(){
        if(SPlayer.get().isPlaying())
            SPlayer.get().play(mContainer,null);
    }

    @Override
    public void onTitleClick(VideoItem item, int position) {
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
        SPlayer.get().resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!toDetail){
            SPlayer.get().pause();
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
        SPlayer.get().destroy();
    }

}

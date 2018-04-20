package com.kk.taurus.avplayer.ui;

import android.Manifest;
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
import android.widget.Toast;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.jiajunhui.xapp.medialoader.callback.OnVideoLoaderCallBack;
import com.jiajunhui.xapp.medialoader.loader.MediaLoader;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.adapter.VideoListAdapter;
import com.kk.taurus.avplayer.play.SPlayer;
import com.kk.taurus.avplayer.utils.OrientationHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Taurus on 2018/4/15.
 */

public class LocalVideoListActivity extends AppCompatActivity
        implements VideoListAdapter.OnListListener, OrientationHelper.OnOrientationListener{

    private List<VideoItem> mItems = new ArrayList<>();
    private VideoListAdapter mAdapter;

    private RecyclerView mRecycler;
    private FrameLayout mContainer;

    private boolean isLandScape;
    private boolean toDetail;

    private OrientationHelper mOrientationHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setTitle("本地视频");

        mOrientationHelper = new OrientationHelper(this, this);

        mRecycler = findViewById(R.id.recycler);
        mContainer = findViewById(R.id.listPlayContainer);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .request();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void permissionSuccess() {
        MediaLoader.loadVideos(this, new OnVideoLoaderCallBack() {
            @Override
            public void onResultList(List<VideoItem> items) {
                Collections.sort(items, new MCompartor());
                mItems.addAll(items);
                mAdapter = new VideoListAdapter(LocalVideoListActivity.this, mRecycler, mItems);
                mAdapter.setOnListListener(LocalVideoListActivity.this);
                mRecycler.setAdapter(mAdapter);
            }
        });
    }

    @Override
    public void onTitleClick(VideoItem item, int position) {
        toDetail = true;
        Intent intent = new Intent(this, DetailPageActivity.class);
        intent.putExtra(DetailPageActivity.KEY_ITEM, item);
        startActivity(intent);
    }

    @PermissionFail(requestCode = 100)
    public void permissionFailure(){
        Toast.makeText(this, "权限拒绝,无法正常使用", Toast.LENGTH_LONG).show();

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

    public class MCompartor implements Comparator<VideoItem> {
        @Override
        public int compare(VideoItem lhs, VideoItem rhs) {
            if(lhs.getSize()>rhs.getSize()){
                return -1;
            }
            if(lhs.getSize()<rhs.getSize()){
                return 1;
            }
            return 0;
        }
    }
}

package com.taurus.playerbaselibrary.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.jiajunhui.xapp.medialoader.callback.OnVideoLoaderCallBack;
import com.jiajunhui.xapp.medialoader.loader.MediaLoader;
import com.kk.taurus.baseframe.base.top_bar.BaseTopBarNavigationMenu;
import com.kk.taurus.baseframe.bean.PageState;
import com.kk.taurus.baseframe.bean.TopBarMenu;
import com.kk.taurus.baseframe.ui.activity.TopBarActivity;
import com.taurus.playerbaselibrary.bean.VideosInfo;
import com.taurus.playerbaselibrary.holder.HomeHolder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Taurus on 2017/3/28.
 */

public class HomeActivity extends TopBarActivity<VideosInfo,HomeHolder> implements HomeHolder.OnHomeHolderListener {

    @Override
    public HomeHolder getContentViewHolder(Bundle savedInstanceState) {
        return new HomeHolder(this);
    }

    @Override
    public void loadState() {
        setPageState(PageState.loading());
        PermissionGen.with(HomeActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void permissionSuccess(){
        MediaLoader.loadVideos(this, new OnVideoLoaderCallBack() {
            @Override
            public void onResultList(List<VideoItem> items) {
                Collections.sort(items,new MCompartor());
                VideosInfo videosInfo = new VideosInfo(items);
                setData(videosInfo);
                setPageState(PageState.success());
            }
        });
    }

    @PermissionFail(requestCode = 100)
    public void permissionFailure(){
        showSnackBar("permission deny",null,null);
        setPageState(PageState.success());
    }

    @Override
    public void initData() {
        super.initData();
        getTopBarNavigationIcon().setVisibility(View.GONE);
        setTopBarTitle("KKPlayer");
        setStatusBarColor(Color.parseColor("#f83d46"));
        setMenuType(BaseTopBarNavigationMenu.MENU_TYPE_TEXT,new TopBarMenu().setMenuText("设置"));
        mContentHolder.setOnHomeHolderListener(this);
    }

    @Override
    public void onNavigationMenuClick() {
        super.onNavigationMenuClick();
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(VideoItem item, int position) {
        Intent intent = new Intent(getApplicationContext(),PlayerActivity.class);
        intent.putExtra("data",item);
        startActivity(intent);
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

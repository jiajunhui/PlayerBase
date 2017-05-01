package com.taurus.playerbaselibrary.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.kk.taurus.baseframe.base.top_bar.BaseTopBarNavigationMenu;
import com.kk.taurus.baseframe.bean.TopBarMenu;
import com.kk.taurus.baseframe.ui.activity.TopBarActivity;
import com.taurus.playerbaselibrary.bean.VideosInfo;
import com.taurus.playerbaselibrary.holder.HomeHolder;
import com.taurus.playerbaselibrary.ui.fragment.LocalVideoFragment;
import com.taurus.playerbaselibrary.ui.fragment.OnlineVideosFragment;


/**
 * Created by Taurus on 2017/3/28.
 */

public class HomeActivity extends TopBarActivity<VideosInfo,HomeHolder> implements HomeHolder.OnMainPageListener {

    private OnlineVideosFragment onlineVideosFragment;
    private LocalVideoFragment localVideoFragment;

    @Override
    public HomeHolder getContentViewHolder(Bundle savedInstanceState) {
        return new HomeHolder(this,this);
    }

    @Override
    public void loadState() {
        super.loadState();
        onSwitchOnlineVideos();
    }

    @Override
    public void initData() {
        super.initData();
        getTopBarNavigationIcon().setVisibility(View.GONE);
        setTopBarTitle("Videos");
        setStatusBarColor(Color.parseColor("#f83d46"));
        setMenuType(BaseTopBarNavigationMenu.MENU_TYPE_TEXT,new TopBarMenu().setMenuText("设置"));
    }

    @Override
    public void onNavigationMenuClick() {
        super.onNavigationMenuClick();
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSwitchOnlineVideos() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(localVideoFragment!=null){
            ft.hide(localVideoFragment);
        }
        if(onlineVideosFragment==null){
            onlineVideosFragment = new OnlineVideosFragment();
            ft.add(mContentHolder.getContainer().getId(),onlineVideosFragment);
        }else{
            ft.show(onlineVideosFragment);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onSwitchLocalVideos() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(onlineVideosFragment!=null){
            ft.hide(onlineVideosFragment);
        }
        if(localVideoFragment==null){
            localVideoFragment = new LocalVideoFragment();
            ft.add(mContentHolder.getContainer().getId(),localVideoFragment);
        }else{
            ft.show(localVideoFragment);
        }
        ft.commitAllowingStateLoss();
    }
}

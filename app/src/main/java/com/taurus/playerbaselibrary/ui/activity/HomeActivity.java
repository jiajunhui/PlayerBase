package com.taurus.playerbaselibrary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.kk.taurus.baseframe.ui.activity.ToolBarActivity;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.bean.VideosInfo;
import com.taurus.playerbaselibrary.holder.HomeHolder;
import com.taurus.playerbaselibrary.ui.fragment.LocalVideoFragment;
import com.taurus.playerbaselibrary.ui.fragment.LocalVideoListFragment;
import com.taurus.playerbaselibrary.ui.fragment.OnlineVideosFragment;


/**
 * Created by Taurus on 2017/3/28.
 */

public class HomeActivity extends ToolBarActivity<VideosInfo,HomeHolder> implements HomeHolder.OnMainPageListener {

    private OnlineVideosFragment onlineVideosFragment;
    private LocalVideoFragment localVideoFragment;
    private LocalVideoListFragment localVideoListFragment;

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
        setNavigationIcon(null);
        setCenterTitle("Videos");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    protected boolean onToolBarMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Intent intent = new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onToolBarMenuItemClick(item);
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
//        if(localVideoListFragment!=null){
//            ft.hide(localVideoListFragment);
//        }
//        if(onlineVideosFragment==null){
//            onlineVideosFragment = new OnlineVideosFragment();
//            ft.add(mContentHolder.getContainer().getId(),onlineVideosFragment);
//        }else{
//            ft.show(onlineVideosFragment);
//        }
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
//        if(onlineVideosFragment!=null){
//            ft.hide(onlineVideosFragment);
//        }
//        if(localVideoListFragment==null){
//            localVideoListFragment = new LocalVideoListFragment();
//            ft.add(mContentHolder.getContainer().getId(),localVideoListFragment);
//        }else{
//            ft.show(localVideoListFragment);
//        }
        ft.commitAllowingStateLoss();
    }
}

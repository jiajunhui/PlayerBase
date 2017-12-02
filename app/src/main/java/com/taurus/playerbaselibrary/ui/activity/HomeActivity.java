package com.taurus.playerbaselibrary.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;

import com.kk.taurus.uiframe.a.TitleBarActivity;
import com.kk.taurus.uiframe.v.BaseTitleBarHolder;
import com.kk.taurus.uiframe.v.BaseUserHolder;
import com.kk.taurus.uiframe.v.d.DefaultTitleBarHolder;
import com.taurus.playerbaselibrary.bean.VideosInfo;
import com.taurus.playerbaselibrary.holder.HomeHolder;
import com.taurus.playerbaselibrary.ui.fragment.LocalVideoFragment;
import com.taurus.playerbaselibrary.ui.fragment.LocalVideoListFragment;
import com.taurus.playerbaselibrary.ui.fragment.OnlineVideosFragment;


/**
 * Created by Taurus on 2017/3/28.
 */

public class HomeActivity extends TitleBarActivity<VideosInfo,HomeHolder> implements HomeHolder.OnMainPageListener {

    private OnlineVideosFragment onlineVideosFragment;
    private LocalVideoFragment localVideoFragment;
    private LocalVideoListFragment localVideoListFragment;

    @Override
    public HomeHolder onBindContentHolder() {
        return new HomeHolder(this,this);
    }

    @Override
    public void onLoadState() {
        onSwitchOnlineVideos();
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#3F51B5"));
        }
        BaseUserHolder userHolder = getUserHolder();
        if(userHolder!=null){
            BaseTitleBarHolder titleBarHolder = userHolder.titleBarHolder;
            titleBarHolder.setTitle("Videos");
            if(titleBarHolder instanceof DefaultTitleBarHolder){
                ((DefaultTitleBarHolder) titleBarHolder).setBackIconVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSwitchOnlineVideos() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hiddenLocalVideoPlayGoFragment(ft);
        hiddenLocalVideoFragment(ft);
        if(onlineVideosFragment==null){
            onlineVideosFragment = new OnlineVideosFragment();
            ft.add(getUserContentHolder().getContainer().getId(),onlineVideosFragment);
        }else{
            ft.show(onlineVideosFragment);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onSwitchLocalVideos() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hiddenOnLineFragment(ft);
        hiddenLocalVideoPlayGoFragment(ft);
        if(localVideoFragment==null){
            localVideoFragment = new LocalVideoFragment();
            ft.add(getUserContentHolder().getContainer().getId(),localVideoFragment);
        }else{
            ft.show(localVideoFragment);
        }

        ft.commitAllowingStateLoss();
    }

    @Override
    public void onSwitchLocalVideosPlayGo() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hiddenOnLineFragment(ft);
        hiddenLocalVideoFragment(ft);
        if(localVideoListFragment==null){
            localVideoListFragment = new LocalVideoListFragment();
            ft.add(getUserContentHolder().getContainer().getId(),localVideoListFragment);
        }else{
            ft.show(localVideoListFragment);
        }
        ft.commitAllowingStateLoss();
    }

    private void hiddenOnLineFragment(FragmentTransaction ft){
        if(onlineVideosFragment!=null){
            ft.hide(onlineVideosFragment);
        }
    }

    private void hiddenLocalVideoFragment(FragmentTransaction ft){
        if(localVideoFragment!=null){
            ft.hide(localVideoFragment);
        }
    }

    private void hiddenLocalVideoPlayGoFragment(FragmentTransaction ft){
        if(localVideoListFragment!=null){
            ft.hide(localVideoListFragment);
        }
    }
}

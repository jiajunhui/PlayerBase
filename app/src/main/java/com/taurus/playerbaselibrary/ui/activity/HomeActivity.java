package com.taurus.playerbaselibrary.ui.activity;

import android.content.Intent;
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
import com.kk.taurus.uiframe.w.TitleBarMenu;
import com.taurus.playerbaselibrary.bean.VideosInfo;
import com.taurus.playerbaselibrary.holder.HomeHolder;
import com.taurus.playerbaselibrary.ui.fragment.InputUrlFragment;
import com.taurus.playerbaselibrary.ui.fragment.LocalVideoFragment;
import com.taurus.playerbaselibrary.ui.fragment.LocalVideoListFragment;
import com.taurus.playerbaselibrary.ui.fragment.OnlineVideosFragment;
import com.taurus.playerbaselibrary.ui.fragment.OutlineFragment;


/**
 * Created by Taurus on 2017/3/28.
 */

public class HomeActivity extends TitleBarActivity<VideosInfo,HomeHolder> implements HomeHolder.OnMainPageListener {

    private OnlineVideosFragment onlineVideosFragment;
    private LocalVideoFragment localVideoFragment;
    private LocalVideoListFragment localVideoListFragment;
    private OutlineFragment outlineFragment;
    private InputUrlFragment inputUrlFragment;

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
        setCenterTitle("在线视频");
        BaseUserHolder userHolder = getUserHolder();
        BaseTitleBarHolder titleBarHolder = userHolder.titleBarHolder;
        DefaultTitleBarHolder defaultTitleBarHolder = (DefaultTitleBarHolder)titleBarHolder;
        TitleBarMenu menu = defaultTitleBarHolder.getMenu();
        menu.setTriggerType(TitleBarMenu.TRIGGER_TYPE_TEXT);
        menu.setMenuText("设置");
        menu.setOnMenuListener(new TitleBarMenu.OnMenuListener() {
            @Override
            public void onTriggerClick(int triggerType, View view) {
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setCenterTitle(String title) {
        BaseUserHolder userHolder = getUserHolder();
        if(userHolder!=null){
            BaseTitleBarHolder titleBarHolder = userHolder.titleBarHolder;
            titleBarHolder.setTitle(title);
            if(titleBarHolder instanceof DefaultTitleBarHolder){
                ((DefaultTitleBarHolder) titleBarHolder).setBackIconVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSwitchOnlineVideos() {
        setCenterTitle("在线视频");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hiddenLocalVideoPlayGoFragment(ft);
        hiddenLocalVideoFragment(ft);
        hiddenInputUrlFragment(ft);
        hiddenOutlineFragment(ft);
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
        setCenterTitle("本地视频");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hiddenOnLineFragment(ft);
        hiddenLocalVideoPlayGoFragment(ft);
        hiddenInputUrlFragment(ft);
        hiddenOutlineFragment(ft);
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
        setCenterTitle("无缝切播");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hiddenOnLineFragment(ft);
        hiddenLocalVideoFragment(ft);
        hiddenInputUrlFragment(ft);
        hiddenOutlineFragment(ft);
        if(localVideoListFragment==null){
            localVideoListFragment = new LocalVideoListFragment();
            ft.add(getUserContentHolder().getContainer().getId(),localVideoListFragment);
        }else{
            ft.show(localVideoListFragment);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onSwitchOutline() {
        setCenterTitle("视频切角");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hiddenOnLineFragment(ft);
        hiddenLocalVideoFragment(ft);
        hiddenInputUrlFragment(ft);
        hiddenLocalVideoPlayGoFragment(ft);
        if(outlineFragment==null){
            outlineFragment = new OutlineFragment();
            ft.add(getUserContentHolder().getContainer().getId(),outlineFragment);
        }else{
            ft.show(outlineFragment);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onSwitchInputUrl() {
        setCenterTitle("输入地址");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hiddenOnLineFragment(ft);
        hiddenLocalVideoFragment(ft);
        hiddenLocalVideoPlayGoFragment(ft);
        hiddenOutlineFragment(ft);
        if(inputUrlFragment==null){
            inputUrlFragment = new InputUrlFragment();
            ft.add(getUserContentHolder().getContainer().getId(),inputUrlFragment);
        }else{
            ft.show(inputUrlFragment);
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
            localVideoListFragment.onHidden();
        }
    }

    private void hiddenOutlineFragment(FragmentTransaction ft){
        if(outlineFragment!=null){
            ft.hide(outlineFragment);
            outlineFragment.onHidden();
        }
    }

    private void hiddenInputUrlFragment(FragmentTransaction ft){
        if(inputUrlFragment!=null){
            ft.hide(inputUrlFragment);
        }
    }
}

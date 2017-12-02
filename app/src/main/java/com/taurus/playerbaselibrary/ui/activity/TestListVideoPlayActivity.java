/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.taurus.playerbaselibrary.ui.activity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.jiajunhui.xapp.medialoader.callback.OnVideoLoaderCallBack;
import com.jiajunhui.xapp.medialoader.loader.MediaLoader;
import com.kk.taurus.uiframe.a.StateActivity;
import com.kk.taurus.uiframe.d.BaseState;
import com.kk.taurus.uiframe.i.HolderData;
import com.taurus.playerbaselibrary.holder.MainHolder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class TestListVideoPlayActivity extends StateActivity<HolderData,MainHolder> {

    @Override
    public void onLoadState() {
        setPageState(BaseState.LOADING);
        PermissionGen.with(this)
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
                getUserContentHolder().updateItems(items);
                setPageState(BaseState.SUCCESS);
            }
        });
    }

    @PermissionFail(requestCode = 100)
    public void permissionFailure(){
        Toast.makeText(this, "permission deny", Toast.LENGTH_SHORT).show();
        setPageState(BaseState.ERROR);
    }

    public class MCompartor implements Comparator<VideoItem> {
        @Override
        public int compare(VideoItem lhs, VideoItem rhs) {
            if(lhs.getSize()>rhs.getSize()){
                return 1;
            }
            if(lhs.getSize()<rhs.getSize()){
                return -1;
            }
            return 0;
        }
    }

    @Override
    public MainHolder onBindContentHolder() {
        return new MainHolder(this);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        keepScreenOn();
    }

    @Override
    public void onBackPressed() {
        if(getUserContentHolder().onBackPressed()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){

        }else{
            fullScreen();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

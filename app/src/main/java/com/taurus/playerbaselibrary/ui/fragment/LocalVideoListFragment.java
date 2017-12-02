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

package com.taurus.playerbaselibrary.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.jiajunhui.xapp.medialoader.callback.OnVideoLoaderCallBack;
import com.jiajunhui.xapp.medialoader.loader.MediaLoader;
import com.kk.taurus.uiframe.d.BaseState;
import com.kk.taurus.uiframe.f.StateFragment;
import com.taurus.playerbaselibrary.bean.VideosInfo;
import com.taurus.playerbaselibrary.holder.LocalVideoListHolder;
import com.taurus.playerbaselibrary.ui.activity.FullScreenActivity;
import com.taurus.playerbaselibrary.ui.activity.SecondActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Taurus on 2017/10/10.
 */

public class LocalVideoListFragment extends StateFragment<VideosInfo,LocalVideoListHolder> implements LocalVideoListHolder.OnLocalVideoListHolderListener {
    @Override
    public LocalVideoListHolder onBindContentHolder() {
        return new LocalVideoListHolder(mContext);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        getUserContentHolder().setOnLocalVideoListHolderListener(this);
    }

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
        MediaLoader.loadVideos(getActivity(), new OnVideoLoaderCallBack() {
            @Override
            public void onResultList(List<VideoItem> items) {
                Collections.sort(items,new MCompartor());
                VideosInfo videosInfo = new VideosInfo(items);
                setData(videosInfo);
                setPageState(BaseState.SUCCESS);
            }
        });
    }

    @PermissionFail(requestCode = 100)
    public void permissionFailure(){
        setPageState(BaseState.ERROR);
    }

    public void onHidden(){
        getUserContentHolder().onHidden();
    }

    @Override
    public void onIntentToDetail(VideoItem item, int position) {
        Intent intent = new Intent(getActivity(), SecondActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFullScreen() {
        Intent intent = new Intent(getActivity(), FullScreenActivity.class);
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

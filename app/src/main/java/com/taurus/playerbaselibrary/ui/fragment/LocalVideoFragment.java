package com.taurus.playerbaselibrary.ui.fragment;

import android.Manifest;
import android.content.Intent;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.jiajunhui.xapp.medialoader.callback.OnVideoLoaderCallBack;
import com.jiajunhui.xapp.medialoader.loader.MediaLoader;
import com.kk.taurus.uiframe.d.BaseState;
import com.kk.taurus.uiframe.f.StateFragment;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.taurus.playerbaselibrary.bean.VideosInfo;
import com.taurus.playerbaselibrary.holder.LocalVideoHolder;
import com.taurus.playerbaselibrary.ui.activity.PlayerActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Taurus on 2017/4/30.
 */

public class LocalVideoFragment extends StateFragment<VideosInfo,LocalVideoHolder> implements LocalVideoHolder.OnLocalVideoListener {

    @Override
    public ContentHolder onBindContentHolder() {
        return new LocalVideoHolder(mContext);
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
                Collections.sort(items,new LocalVideoFragment.MCompartor());
                VideosInfo videosInfo = new VideosInfo(items);
                getUserContentHolder().setOnLocalVideoListener(LocalVideoFragment.this);
                setData(videosInfo);
                setPageState(BaseState.SUCCESS);

//                Intent intent = new Intent(getApplicationContext(),PlayerActivity.class);
//                VideoItem item = new VideoItem();
//                item.setPath("http://vfx.mtime.cn/Video/2015/03/13/mp4/150313150746828551.mp4");
//                intent.putExtra("data",item);
//                startActivity(intent);
            }
        });
    }

    @PermissionFail(requestCode = 100)
    public void permissionFailure(){
        setPageState(BaseState.ERROR);
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

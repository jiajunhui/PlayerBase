package com.taurus.playerbaselibrary.ui.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.baseframe.ui.activity.ToolsActivity;
import com.kk.taurus.filebase.engine.FileEngine;
import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.callback.OnAdCoverClickListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.DefaultReceiverCollections;
import com.kk.taurus.playerbase.cover.base.BaseAdCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerControllerCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerErrorCover;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.PlayData;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.setting.ViewType;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.callback.OnCompleteCallBack;
import com.taurus.playerbaselibrary.cover.PlayCompleteCover;
import com.taurus.playerbaselibrary.cover.PlayerErrorCover;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends ToolsActivity implements OnPlayerEventListener {

    private RelativeLayout mContainer;
    private BasePlayer mPlayer;
    private DefaultReceiverCollections mCoverCollections;
    private VideoItem item;
    private VideoData videoData;
    private PlayCompleteCover completeCover;

    private int mCount;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCount++;
            if(mCount % 5 == 0){
                screenShot();
            }
            mHandler.sendEmptyMessageDelayed(0,1000);
        }
    };

    private void screenShot(){
        View renderView = mPlayer.getRenderView();
        if(renderView instanceof TextureView){
            Bitmap bitmap = ((TextureView) renderView).getBitmap();
            if(bitmap!=null){
                String info = FileEngine.bitmapToFile(bitmap, getExternalCacheDir(), System.currentTimeMillis() + ".png");
                System.out.println(info);
            }
        }

    }

    @Override
    public void loadState() {

    }

    @Override
    public View getContentView(Bundle savedInstanceState) {
        return View.inflate(this,R.layout.activity_main,null);
    }

    @Override
    public void parseIntent() {
        super.parseIntent();
        item = (VideoItem) getIntent().getSerializableExtra("data");
        videoData = new VideoData(item.getPath());
    }

    @Override
    public void initData() {
        super.initData();
        fullScreen();
        keepScreenOn();
        mContainer = (RelativeLayout) findViewById(R.id.container);

        mPlayer = new DefaultPlayer(this);
        mPlayer.setViewType(ViewType.TEXTUREVIEW);
        mContainer.addView(mPlayer,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mCoverCollections = new DefaultReceiverCollections(this);
        mCoverCollections.buildDefault().addCover(PlayCompleteCover.KEY,completeCover = new PlayCompleteCover(this,null))
                .addCover(BasePlayerErrorCover.KEY_INT_DATA,new PlayerErrorCover(this));

        mPlayer.bindCoverCollections(mCoverCollections);

        BasePlayerControllerCover controllerCover = mCoverCollections.getReceiver(BasePlayerControllerCover.KEY);
        controllerCover.setVideoTitle(item.getDisplayName());
        controllerCover.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(completeCover!=null){
            completeCover.setOnCompleteListener(new OnCompleteCallBack(){
                @Override
                public void onReplay(PlayCompleteCover completeCover) {
                    super.onReplay(completeCover);
                    mPlayer.rePlay(0);
                }
            });
        }

        mPlayer.setOnPlayerEventListener(this);
        normalStart();
//        testAdStart();
    }

    private void normalStart(){
//        videoData.setData("http://221.179.217.9:80/88888888/16/20160710/269132842/269132842.ts/index.m3u8?rrsip=221.179.217.7,rrsip=221.179.217.8&fmt=ts2hls&servicetype=0&icpid=&accounttype=1&limitflux=-1&limitdur=-1&accountinfo=rKlvqC9Bu1OKxWIIyStkN1MZOJYRGRmNIUAvp+MASurCeESoTSlCaVqYoCRBg7TarfZYj4NsETM99SV2RSrK+j4/Y/GvaNspjE62RRfxOV9uRiaw46Nrc2K0k8P7gzfb:20170502102853,08A5C8EFF290,219.141.176.130,20170502102853,00010000000000000000000010922412,AC07C324AB215EE2B1111353ABDE4599,,1,0,-1,294,1,2201300,207,575034,1,END");
        mPlayer.setDataSource(videoData);
        mPlayer.start();
    }

    private void testAdStart(){
        PlayData playData = new PlayData(videoData);
        List<BaseAdVideo> adVideos = new ArrayList<>();
        adVideos.add(new BaseAdVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
//        adVideos.add(new BaseAdVideo("http://172.16.216.70:8080/batamu_trans19.mp4"));
        playData.setAdVideos(adVideos);

        final BaseAdCover adCover = mCoverCollections.getReceiver(BaseAdCover.KEY);
        adCover.setOnAdCoverClickListener(new OnAdCoverClickListener() {
            @Override
            public void onAdCoverClick(BaseAdVideo adVideo) {
                showToast("click : " + adVideo.getData());
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(mPlayer!=null){
            mPlayer.doConfigChange(newConfig);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mPlayer!=null){
            mPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mPlayer!=null){
            mPlayer.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayer!=null){
            mPlayer.destroy(true);
        }
        mHandler.removeMessages(0);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
//                mHandler.sendEmptyMessage(0);
                break;
        }
    }

}

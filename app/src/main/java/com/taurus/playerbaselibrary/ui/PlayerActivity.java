package com.taurus.playerbaselibrary.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.baseframe.ui.activity.ToolsActivity;
import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.callback.OnAdCallBack;
import com.kk.taurus.playerbase.callback.OnAdCoverClickListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.DefaultReceiverCollections;
import com.kk.taurus.playerbase.cover.base.BaseAdCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerControllerCover;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.PlayData;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.BaseAdPlayer;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.callback.OnCompleteCallBack;
import com.taurus.playerbaselibrary.cover.PlayCompleteCover;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends ToolsActivity implements OnPlayerEventListener {

    private BasePlayer mPlayer;
    private DefaultReceiverCollections mCoverCollections;
    private VideoItem item;
    private VideoData videoData;
    private PlayCompleteCover completeCover;

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
        mPlayer = (DefaultPlayer) findViewById(R.id.player);

        mCoverCollections = new DefaultReceiverCollections(this);
        mCoverCollections.buildDefault().addCover(PlayCompleteCover.KEY,completeCover = new PlayCompleteCover(this,null));

//        CornerCutCover cornerCutCover = new CornerCutCover(this,null);
//        cornerCutCover.setCornerRadius(80);
//        cornerCutCover.setCornerBgColor(Color.WHITE);
//        mCoverCollections.addCornerCutCover(cornerCutCover);

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
        mPlayer.setDataSource(videoData);
        mPlayer.start();
    }

    private void testAdStart(){
        PlayData playData = new PlayData(videoData);
        List<BaseAdVideo> adVideos = new ArrayList<>();
        adVideos.add(new BaseAdVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
        adVideos.add(new BaseAdVideo("http://172.16.218.64:8080/batamu_trans19.mp4"));
        playData.setAdVideos(adVideos);

        final BaseAdCover adCover = mCoverCollections.getReceiver(BaseAdCover.KEY);
        adCover.setOnAdCoverClickListener(new OnAdCoverClickListener() {
            @Override
            public void onAdCoverClick(BaseAdVideo adVideo) {
                showToast("click : " + adVideo.getData());
            }
        });

        mPlayer.playData(playData,new OnAdCallBack(){
            @Override
            public void onAdPlay(BaseAdPlayer adPlayer, BaseAdVideo adVideo) {
                super.onAdPlay(adPlayer, adVideo);
            }

            @Override
            public void onAdPlayComplete(BaseAdVideo adVideo, boolean isAllComplete) {
                Toast.makeText(PlayerActivity.this, adVideo.getData(), Toast.LENGTH_SHORT).show();
                super.onAdPlayComplete(adVideo, isAllComplete);
            }

            @Override
            public void onVideoStart(BaseAdPlayer adPlayer,VideoData data) {
                super.onVideoStart(adPlayer,data);
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
            mPlayer.destroy();
        }
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:

                break;
        }
    }
}

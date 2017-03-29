package com.taurus.playerbaselibrary.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.baseframe.ui.activity.ToolsActivity;
import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.DefaultCoverCollections;
import com.kk.taurus.playerbase.cover.base.BasePlayerControllerCover;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.taurus.playerbaselibrary.R;

public class PlayerActivity extends ToolsActivity implements OnPlayerEventListener {

    private BasePlayer mPlayer;
    private DefaultCoverCollections mCoverCollections;
    private VideoItem item;
    private VideoData videoData;

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

        mCoverCollections = new DefaultCoverCollections(this);
        mCoverCollections.buildDefault();
        mPlayer.bindCoverCollections(mCoverCollections);

        BasePlayerControllerCover controllerCover = mCoverCollections.getCover(BasePlayerControllerCover.KEY);
        controllerCover.setVideoTitle(item.getDisplayName());
        controllerCover.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPlayer.setOnPlayerEventListener(this);
        mPlayer.setDataSource(videoData);
        mPlayer.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(mPlayer!=null){
            mPlayer.doConfigChange(newConfig);
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

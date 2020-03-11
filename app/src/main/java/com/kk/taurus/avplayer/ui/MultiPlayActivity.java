package com.kk.taurus.avplayer.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.ReceiverGroupManager;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.widget.BaseVideoView;

public class MultiPlayActivity extends AppCompatActivity {

    BaseVideoView mVideoView01,mVideoView02,mVideoView03,mVideoView04,mVideoView05,mVideoView06;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_play);
        mVideoView01 = findViewById(R.id.videoView01);
        mVideoView02 = findViewById(R.id.videoView02);
        mVideoView03 = findViewById(R.id.videoView03);
        mVideoView04 = findViewById(R.id.videoView04);
        mVideoView05 = findViewById(R.id.videoView05);
        mVideoView06 = findViewById(R.id.videoView06);

        DataSource dataSource01 = new DataSource("https://mov.bn.netease.com/open-movie/nos/mp4/2016/06/22/SBP8G92E3_hd.mp4");
        mVideoView01.setDataSource(dataSource01);
        mVideoView01.setReceiverGroup(ReceiverGroupManager.get().getReceiverGroup(this));
        mVideoView01.setEventHandler(new OnVideoViewEventHandler());
        mVideoView01.start();

        DataSource dataSource02 = new DataSource("https://mov.bn.netease.com/open-movie/nos/mp4/2015/08/27/SB13F5AGJ_sd.mp4");
        mVideoView02.setDataSource(dataSource02);
        mVideoView02.setReceiverGroup(ReceiverGroupManager.get().getReceiverGroup(this));
        mVideoView02.setEventHandler(new OnVideoViewEventHandler());
        mVideoView02.start();

        DataSource dataSource03 = new DataSource("https://mov.bn.netease.com/open-movie/nos/mp4/2018/01/12/SD70VQJ74_sd.mp4");
        mVideoView03.setDataSource(dataSource03);
        mVideoView03.setReceiverGroup(ReceiverGroupManager.get().getReceiverGroup(this));
        mVideoView03.setEventHandler(new OnVideoViewEventHandler());
        mVideoView03.start();

        DataSource dataSource04 = new DataSource("https://mov.bn.netease.com/open-movie/nos/mp4/2017/05/31/SCKR8V6E9_hd.mp4");
        mVideoView04.setDataSource(dataSource04);
        mVideoView04.setReceiverGroup(ReceiverGroupManager.get().getReceiverGroup(this));
        mVideoView04.setEventHandler(new OnVideoViewEventHandler());
        mVideoView04.start();

        DataSource dataSource05 = new DataSource("https://mov.bn.netease.com/open-movie/nos/mp4/2016/01/11/SBC46Q9DV_hd.mp4");
        mVideoView05.setDataSource(dataSource05);
        mVideoView05.setReceiverGroup(ReceiverGroupManager.get().getReceiverGroup(this));
        mVideoView05.setEventHandler(new OnVideoViewEventHandler());
        mVideoView05.start();

        DataSource dataSource06 = new DataSource("https://mov.bn.netease.com/open-movie/nos/mp4/2018/04/19/SDEQS1GO6_hd.mp4");
        mVideoView06.setDataSource(dataSource06);
        mVideoView06.setReceiverGroup(ReceiverGroupManager.get().getReceiverGroup(this));
        mVideoView06.setEventHandler(new OnVideoViewEventHandler());
        mVideoView06.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView01.stopPlayback();
        mVideoView02.stopPlayback();
        mVideoView03.stopPlayback();
        mVideoView04.stopPlayback();
        mVideoView05.stopPlayback();
        mVideoView06.stopPlayback();
    }
}

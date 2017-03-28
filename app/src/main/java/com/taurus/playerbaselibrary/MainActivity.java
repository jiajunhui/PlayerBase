package com.taurus.playerbaselibrary;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.DefaultCoverCollections;
import com.kk.taurus.playerbase.cover.DefaultPlayerLoadingCover;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.PlayData;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.BasePlayer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPlayerEventListener {

    private BasePlayer mPlayer;
    private DefaultCoverCollections mCoverCollections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayer = (DefaultPlayer) findViewById(R.id.player);

        mCoverCollections = new DefaultCoverCollections(this);
        mCoverCollections
                .setDefaultPlayerControllerCover()
                .addPlayerLoadingCover(new DefaultPlayerLoadingCover(this,new LoadingObserver(this)))
                .setDefaultPlayerGestureCover()
                .setDefaultPlayerErrorCover()
                .setDefaultPlayerAdCover();
        mPlayer.buildCoverCollections(mCoverCollections);

        VideoData data = new VideoData("http://172.16.218.64:8080/lvyexianzong.mkv");
//        VideoData data = new VideoData("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        PlayData playData = new PlayData(data);
        BaseAdVideo adVideo = new BaseAdVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        List<BaseAdVideo> adVideos = new ArrayList<>();
        adVideos.add(adVideo);
        playData.setAdVideos(adVideos);
//        mPlayer.setDataSource(data);
        mPlayer.playData(playData);
        mPlayer.setOnPlayerEventListener(this);
        mPlayer.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
                TestPlayData playData = new TestPlayData();
                playData.setUrl("http://172.16.218.64:8080/lvyexianzong.mkv");
                mCoverCollections.refreshData(playData);
                break;
        }
    }
}

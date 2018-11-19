package com.kk.taurus.avplayer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.widget.BaseVideoView;

public class TestPlayActivity extends AppCompatActivity {

    private BaseVideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_play);

        mVideoView = findViewById(R.id.testVideoView);

        DataSource dataSource = new DataSource();
        dataSource.setRawId(R.raw.big_buck_bunny);
//        dataSource.setAssetsPath("video/big_buck_bunny.mp4");
        mVideoView.setDataSource(dataSource);
        mVideoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }
}

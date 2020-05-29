package com.kk.taurus.avplayer.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kk.taurus.avplayer.cover.LoadingCover;
import com.kk.taurus.avplayer.cover.TvControllerCover;
import com.kk.taurus.avplayer.utils.DataUtils;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.widget.BaseVideoView;

import static com.kk.taurus.avplayer.play.DataInter.ReceiverKey.KEY_LOADING_COVER;

public class TvBaseVideoViewActivity extends AppCompatActivity {

    private BaseVideoView mVideoView;
    private ReceiverGroup mReceiverGroup;

    private boolean hasStart;
    private boolean videoViewKeyEnable = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoView = new BaseVideoView(this);
        mVideoView.setFocusable(true);
        mVideoView.requestFocus();
        mReceiverGroup = new ReceiverGroup();
        mReceiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(this));
        mReceiverGroup.addReceiver("key_event_cover", new TvControllerCover(this));
        mVideoView.setReceiverGroup(mReceiverGroup);
        mVideoView.setEventHandler(new OnVideoViewEventHandler());
        setContentView(mVideoView);
        initPlay();
    }


    private void initPlay(){
        if(!hasStart){
            DataSource dataSource = new DataSource(DataUtils.VIDEO_URL_09);
            dataSource.setTitle("音乐和艺术如何改变世界");
            mVideoView.setDataSource(dataSource);
            mVideoView.start();
            hasStart = true;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
            videoViewKeyEnable = !videoViewKeyEnable;
            mVideoView.getSuperContainer().setKeyEventEnable(videoViewKeyEnable);
            Toast.makeText(this, videoViewKeyEnable ? "播放器可以分发" : "播放器移除分发", Toast.LENGTH_SHORT).show();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }
}

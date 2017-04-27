package com.taurus.playerbaselibrary.ui;

import android.os.Bundle;
import android.view.View;

import com.kk.taurus.baseframe.ui.activity.ToolsActivity;
import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.cover.DefaultReceiverCollections;
import com.kk.taurus.playerbase.cover.DefaultDpadFocusCover;
import com.kk.taurus.playerbase.inter.IDpadFocusCover;
import com.kk.taurus.playerbase.setting.VideoData;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.cover.TestKeyActionDownMenuCover;
import com.taurus.playerbaselibrary.cover.TestKeyActionDownUpCover;

/**
 * Created by Taurus on 2017/4/5.
 */

public class TestKeyActionPlayerActivity extends ToolsActivity implements OnCoverEventListener {

    private DefaultPlayer mPlayer;

    @Override
    public View getContentView(Bundle savedInstanceState) {
        return View.inflate(this, R.layout.activity_test_key_action,null);
    }

    @Override
    public void initData() {
        super.initData();
        mPlayer = (DefaultPlayer) findViewById(R.id.player);
        DefaultReceiverCollections coverCollections = new DefaultReceiverCollections(this);
        coverCollections.buildDefault()
                .addFocusCover(new DefaultDpadFocusCover(this,null))
                .addCover("test_key_action_down_up",new TestKeyActionDownUpCover(this,null))
                .addCover("test_key_action_down_menu",new TestKeyActionDownMenuCover(this,null));
        mPlayer.bindCoverCollections(coverCollections);
        mPlayer.setOnCoverEventListener(this);
        mPlayer.dPadRequestFocus();
        VideoData videoData = new VideoData("http://172.16.218.64:8080/batamu_trans19.mp4");
        mPlayer.setDataSource(videoData);
        mPlayer.start();
    }

    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case IDpadFocusCover.EVENT_CODE_ACTION_DOWN_BACK:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayer!=null){
            mPlayer.destroy();
        }
    }
}

package com.kk.taurus.avplayer.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.bean.VideoBean;
import com.kk.taurus.avplayer.play.AssistPlayer;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.play.ReceiverGroupManager;
import com.kk.taurus.avplayer.utils.OrientationHelper;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;

/**
 * Created by Taurus on 2018/4/18.
 */

public class DetailPageActivity extends AppCompatActivity
        implements OrientationHelper.OnOrientationListener, OnReceiverEventListener {

    public static final String KEY_ITEM = "item_data";

    private RelativeLayout mLayoutContainer;

    private OrientationHelper orientationHelper;

    private boolean isLandscape;
    private ReceiverGroup mReceiverGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_page);

        orientationHelper = new OrientationHelper(this, this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        VideoBean item = (VideoBean) getIntent().getSerializableExtra(KEY_ITEM);

        mLayoutContainer = findViewById(R.id.layoutContainer);

        AssistPlayer.get().addOnReceiverEventListener(this);

        mReceiverGroup = ReceiverGroupManager.get().getReceiverGroup(this);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, true);
        AssistPlayer.get().setReceiverGroup(mReceiverGroup);

        DataSource intentDataSource = new DataSource(item.getPath());
        intentDataSource.setTitle(item.getDisplayName());
        DataSource dataSource = null;
        if(!AssistPlayer.get().isInPlaybackState()){
            dataSource = intentDataSource;
        }

        mReceiverGroup.getGroupValue().putObject(DataInter.Key.KEY_DATA_SOURCE, intentDataSource);

        AssistPlayer.get().play(mLayoutContainer, dataSource);

    }

    @Override
    public void onSensorUserAgreement() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationHelper.onActivityConfigChanged(newConfig);
        isLandscape = newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE;
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandscape);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AssistPlayer.get().removeReceiverEventListener(this);

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                setRequestedOrientation(isLandscape?
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                if(isLandscape){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    return;
                }
                finish();
                break;
        }
    }
}

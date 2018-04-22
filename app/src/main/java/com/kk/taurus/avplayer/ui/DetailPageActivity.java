package com.kk.taurus.avplayer.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.SPlayer;
import com.kk.taurus.avplayer.utils.OrientationHelper;
import com.kk.taurus.avplayer.utils.PUtil;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;

/**
 * Created by Taurus on 2018/4/18.
 */

public class DetailPageActivity extends AppCompatActivity
        implements OrientationHelper.OnOrientationListener, OnPlayerEventListener {

    public static final String KEY_ITEM = "item_data";

    private RelativeLayout mLayoutContainer;

    private OrientationHelper orientationHelper;

    private boolean isLandscape;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_page);

        orientationHelper = new OrientationHelper(this, this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        VideoItem item = (VideoItem) getIntent().getSerializableExtra(KEY_ITEM);

        mLayoutContainer = findViewById(R.id.layoutContainer);

        updateVideoContainer(isLandscape);

        SPlayer.get().addOnPlayerEventListener(this);

        DataSource intentDataSource = new DataSource(item.getPath());
        DataSource dataSource = null;
        if(!SPlayer.get().isPlaying() || !SPlayer.get().isEqualDataSource(intentDataSource)){
            dataSource = intentDataSource;
        }

        SPlayer.get().play(mLayoutContainer, dataSource);

    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE:
                updateVideoContainer(isLandscape);
                break;
        }
    }

    @Override
    public void onOrientationChange(boolean reverse, int orientation, int angle) {
        isLandscape = orientation==Configuration.ORIENTATION_LANDSCAPE;
        if(orientation==Configuration.ORIENTATION_LANDSCAPE){
            if(SPlayer.get().isPlaying()){
                setRequestedOrientation(reverse?
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }else if(orientation==Configuration.ORIENTATION_PORTRAIT){
            if(SPlayer.get().isPlaying()){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    @Override
    public void onSensorUserAgreement() {

    }

    private void updateVideoContainer(boolean landscape){
        ViewGroup.LayoutParams layoutParams = mLayoutContainer.getLayoutParams();
        if(landscape){
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }else{
            int w = PUtil.getScreenW(this);
            int h = w * 9/16;
            int[] wh = SPlayer.get().getWH();
            if(wh[1] > wh[0]){
                h = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            layoutParams.width = w;
            layoutParams.height = h;
        }
        mLayoutContainer.setLayoutParams(layoutParams);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            updateVideoContainer(true);
        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            updateVideoContainer(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPlayer.get().removeOnPlayerEventListener(this);
    }
}

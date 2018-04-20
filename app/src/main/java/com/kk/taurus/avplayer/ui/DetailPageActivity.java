package com.kk.taurus.avplayer.ui;

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
import com.kk.taurus.avplayer.utils.PUtil;
import com.kk.taurus.playerbase.entity.DataSource;

/**
 * Created by Taurus on 2018/4/18.
 */

public class DetailPageActivity extends AppCompatActivity {

    public static final String KEY_ITEM = "item_data";

    private RelativeLayout mLayoutContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_page);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        VideoItem item = (VideoItem) getIntent().getSerializableExtra(KEY_ITEM);

        mLayoutContainer = findViewById(R.id.layoutContainer);

        DataSource intentDataSource = new DataSource(item.getPath());
        DataSource dataSource = null;
        if(!SPlayer.get().isPlaying() || !SPlayer.get().isEqualDataSource(intentDataSource)){
            dataSource = intentDataSource;
        }

        SPlayer.get().play(mLayoutContainer, dataSource);

    }

    private void updateVideoContainer(boolean landscape){
        ViewGroup.LayoutParams layoutParams = mLayoutContainer.getLayoutParams();
        if(landscape){
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }else{
            layoutParams.width = PUtil.getScreenW(this);
            layoutParams.height = layoutParams.width * 9/16;
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
}

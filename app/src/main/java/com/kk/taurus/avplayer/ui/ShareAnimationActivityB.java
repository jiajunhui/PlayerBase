package com.kk.taurus.avplayer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.ShareAnimationPlayer;
import com.kk.taurus.playerbase.entity.DataSource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareAnimationActivityB extends AppCompatActivity {

    public static final String KEY_DATA = "data_source";

    @BindView(R.id.top_container)
    RelativeLayout mTopContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_animation_b);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        DataSource dataSource = (DataSource) getIntent().getSerializableExtra(KEY_DATA);

        DataSource useData = null;
        DataSource playData = ShareAnimationPlayer.get().getDataSource();
        boolean dataChange = playData!=null && !playData.getData().equals(dataSource.getData());
        if(!ShareAnimationPlayer.get().isInPlaybackState() || dataChange){
            useData = dataSource;
        }

        ShareAnimationPlayer.get().play(mTopContainer, useData);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareAnimationPlayer.get().destroy();
    }
}

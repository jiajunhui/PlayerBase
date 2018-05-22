package com.kk.taurus.avplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kk.taurus.avplayer.App;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.entity.DecoderPlan;

public class MainActivity extends AppCompatActivity {

    private TextView mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfo = findViewById(R.id.tv_info);

        updateDecoderInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDecoderInfo();
    }

    private void updateDecoderInfo() {
        DecoderPlan defaultPlan = PlayerConfig.getDefaultPlan();
        mInfo.setText("当前解码方案为:" + defaultPlan.getDesc());
    }

    public void mediaPlayerDecode(View v){
        PlayerConfig.setDefaultPlanId(PlayerConfig.DEFAULT_PLAN_ID);
        updateDecoderInfo();
    }

    public void ijkPlayerDecode(View v){
        PlayerConfig.setDefaultPlanId(App.PLAN_ID_IJK);
        updateDecoderInfo();
    }

    public void useBaseVideoView(View v){
        intentTo(VideoViewActivity.class);
    }

    public void useAVPlayerLocalVideos(View v){
        intentTo(LocalVideoListActivity.class);
    }

    public void useAVPlayerOnlineVideos(View v){
        intentTo(OnlineVideoListActivity.class);
    }

    private void intentTo(Class<? extends Activity> cls){
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}

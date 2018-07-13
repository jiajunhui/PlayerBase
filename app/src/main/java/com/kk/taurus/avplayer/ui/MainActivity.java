package com.kk.taurus.avplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kk.taurus.avplayer.App;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;

public class MainActivity extends AppCompatActivity implements OnReceiverEventListener {

    private TextView mInfo;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = findViewById(R.id.id_toolbar);
        mInfo = findViewById(R.id.tv_info);

        setSupportActionBar(mToolBar);

        updateDecoderInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.switchIjkPlayer:
                PlayerConfig.setDefaultPlanId(App.PLAN_ID_IJK);
                updateDecoderInfo();
                break;
            case R.id.switchMediaPlayer:
                PlayerConfig.setDefaultPlanId(PlayerConfig.DEFAULT_PLAN_ID);
                updateDecoderInfo();
                break;
            case R.id.switchExoPlayer:
                PlayerConfig.setDefaultPlanId(App.PLAN_ID_EXO);
                updateDecoderInfo();
                break;
            case R.id.inputUrlPlay:
                intentTo(InputUrlPlayActivity.class);
                break;
        }
        return true;
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

    public void useBaseVideoView(View v){
        intentTo(VideoViewActivity.class);
    }

    public void useWindowSwitchPlay(View v){
        intentTo(WindowSwitchPlayActivity.class);
    }

    public void useWindowVideoView(View v){
        intentTo(WindowVideoViewActivity.class);
    }

    public void useAVPlayerLocalVideos(View v){
        intentTo(LocalVideoListActivity.class);
    }

    public void useAVPlayerOnlineVideos(View v){
        intentTo(OnlineVideoListActivity.class);
    }

    public void shareAnimationVideos(View v){
        intentTo(ShareAnimationActivityA.class);
    }

    private void intentTo(Class<? extends Activity> cls){
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {
        switch (eventCode){

        }
    }

}

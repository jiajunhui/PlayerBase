package com.kk.taurus.avplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kk.taurus.avplayer.ui.InputUrlPlayActivity;
import com.kk.taurus.avplayer.ui.ViewPagerPlayActivity;
import com.kk.taurus.avplayer.ui.listplay.MultiListActivity;
import com.kk.taurus.avplayer.ui.ShareAnimationActivityA;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.entity.DecoderPlan;

import com.kk.taurus.avplayer.ui.listplay.ListPlayActivity;
import com.kk.taurus.avplayer.ui.BaseVideoViewActivity;
import com.kk.taurus.avplayer.ui.window.FloatWindowActivity;
import com.kk.taurus.avplayer.ui.window.WindowVideoViewActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView mInfo;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

    public void useBaseVideoView(View view){
        intentTo(BaseVideoViewActivity.class);
    }

    public void useWindowVideoView(View view){
        intentTo(WindowVideoViewActivity.class);
    }

    public void useFloatWindow(View view){
        intentTo(FloatWindowActivity.class);
    }

    public void viewPagerPlay(View view){
        intentTo(ViewPagerPlayActivity.class);
    }

    public void singleListPlay(View view){
        intentTo(ListPlayActivity.class);
    }

    public void multiListPlay(View view){
        intentTo(MultiListActivity.class);
    }

    public void shareAnimationVideos(View view){
        intentTo(ShareAnimationActivityA.class);
    }

    private void intentTo(Class<? extends Activity> cls){
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }
}

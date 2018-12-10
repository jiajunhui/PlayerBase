package com.kk.taurus.avplayer.ui.listplay;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.adapter.VideoListPagerAdapter;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.play.ListPlayer;
import com.kk.taurus.avplayer.play.OnHandleListener;
import com.kk.taurus.avplayer.ui.fragment.VideoListFragment;

import java.util.ArrayList;
import java.util.List;

import com.kk.taurus.avplayer.base.ISPayer;
import com.kk.taurus.avplayer.utils.OrientationSensor;
import com.kk.taurus.playerbase.player.IPlayer;

public class MultiListActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private FrameLayout mFullScreenContainer;

    private boolean isLandScape;
    private boolean toDetail;

    private OrientationSensor mOrientationSensor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_list);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mFullScreenContainer = findViewById(R.id.fullScreenContainer);

        mTabLayout.setTabTextColors(Color.BLACK, Color.BLUE);
        mTabLayout.setSelectedTabIndicatorColor(Color.BLUE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                ListPlayer.get().stop();
                ListPlayer.get().setPlayPageIndex(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        List<VideoListFragment> fragments = new ArrayList<>();
        fragments.add(VideoListFragment.create(0));
        fragments.add(VideoListFragment.create(1));
        fragments.add(VideoListFragment.create(2));

        VideoListPagerAdapter pagerAdapter = new VideoListPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(pagerAdapter);

        mOrientationSensor = new OrientationSensor(this,onOrientationListener);
        mOrientationSensor.enable();

    }

    private OrientationSensor.OnOrientationListener onOrientationListener =
            new OrientationSensor.OnOrientationListener() {
                @Override
                public void onLandScape(int orientation) {
                    if(ListPlayer.get().isInPlaybackState()){
                        setRequestedOrientation(orientation);
                    }
                }
                @Override
                public void onPortrait(int orientation) {
                    if(ListPlayer.get().isInPlaybackState()){
                        setRequestedOrientation(orientation);
                    }
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        ListPlayer.get().updateGroupValue(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, isLandScape);
        ListPlayer.get().setOnHandleListener(new OnHandleListener() {
            @Override
            public void onBack() {
                onBackPressed();
            }
            @Override
            public void onToggleScreen() {
                toggleScreen();
            }
        });if(!toDetail && ListPlayer.get().isInPlaybackState()){
            ListPlayer.get().resume();
        }
        toDetail = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        int state = ListPlayer.get().getState();
        if(state == IPlayer.STATE_PLAYBACK_COMPLETE)
            return;
        if(!toDetail){
            ListPlayer.get().pause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ListPlayer.get().attachActivity(this);
        mOrientationSensor.enable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mOrientationSensor.disable();
    }

    @Override
    public void onBackPressed() {
        if(isLandScape){
            toggleScreen();
            return;
        }
        super.onBackPressed();
    }

    public void toDetail(){
        toDetail = true;
    }

    private void toggleScreen(){
        setRequestedOrientation(isLandScape?
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLandScape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        mFullScreenContainer.setBackgroundColor(isLandScape?Color.BLACK:Color.TRANSPARENT);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            ListPlayer.get().setReceiverConfigState(this, ISPayer.RECEIVER_GROUP_CONFIG_FULL_SCREEN_STATE);
            ListPlayer.get().attachContainer(mFullScreenContainer, false);
        }
        ListPlayer.get().updateGroupValue(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, isLandScape);
        ListPlayer.get().updateGroupValue(DataInter.Key.KEY_IS_LANDSCAPE, isLandScape);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrientationSensor.disable();
        ListPlayer.get().destroy();
    }
}

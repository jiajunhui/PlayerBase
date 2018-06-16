package com.kk.taurus.avplayer.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.bean.VideoBean;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.play.ReceiverGroupManager;
import com.kk.taurus.avplayer.play.ShareAnimationPlayer;
import com.kk.taurus.avplayer.utils.ImageDisplayEngine;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShareAnimationActivityA extends AppCompatActivity {

    @BindView(R.id.albumImage)
    ImageView mAlbumImage;
    @BindView(R.id.playIcon)
    ImageView playIcon;
    @BindView(R.id.album_layout)
    RelativeLayout mAlbumLayout;
    @BindView(R.id.layoutContainer)
    FrameLayout mLayoutContainer;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private Unbinder unbinder;

    private DataSource mData;

    private boolean toNext;
    private ReceiverGroup mReceiverGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_animation_a);
        unbinder = ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        VideoBean mVideoBean = new VideoBean(
                "不想从被子里出来",
                "http://open-image.nosdn.127.net/57baaaeaad4e4fda8bdaceafdb9d45c2.jpg",
                "https://mov.bn.netease.com/open-movie/nos/mp4/2018/01/12/SD70VQJ74_sd.mp4");

        mData = new DataSource(mVideoBean.getPath());
        mData.setTitle(mVideoBean.getDisplayName());

        ImageDisplayEngine.display(this, mAlbumImage, mVideoBean.getCover(), R.mipmap.ic_launcher);
        mTvTitle.setText(mVideoBean.getDisplayName());

        mReceiverGroup = ReceiverGroupManager.get().getReceiverGroup(this);

    }

    @OnClick({R.id.album_layout, R.id.tv_title})
    public void onViewClicked(View view) {
        ShareAnimationPlayer.get().setReceiverGroup(mReceiverGroup);
        switch (view.getId()) {
            case R.id.album_layout:
                playIcon.setVisibility(View.GONE);
                ShareAnimationPlayer.get().play(mLayoutContainer, mData);
                break;
            case R.id.tv_title:
                toNext = true;
                Intent intent = new Intent(this, ShareAnimationActivityB.class);
                intent.putExtra(ShareAnimationActivityB.KEY_DATA, mData);
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                            this, mLayoutContainer, "videoShare");
                    ActivityCompat.startActivity(this, intent, options.toBundle());
                }else{
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        playIcon.setVisibility(View.VISIBLE);

        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, false);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_SCREEN_SWITCH_ENABLE, false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!toNext){
            ShareAnimationPlayer.get().pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ShareAnimationPlayer.get().destroy();

        unbinder.unbind();
    }
}

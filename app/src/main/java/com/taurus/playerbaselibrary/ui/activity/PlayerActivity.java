package com.taurus.playerbaselibrary.ui.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.filebase.engine.FileEngine;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.DefaultReceiverCollections;
import com.kk.taurus.playerbase.cover.base.BasePlayerControllerCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerErrorCover;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.DecoderType;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.StandardPlayer;
import com.kk.taurus.uiframe.a.ToolsActivity;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.callback.OnCompleteCallBack;
import com.taurus.playerbaselibrary.cover.PlayCompleteCover;
import com.taurus.playerbaselibrary.cover.PlayerErrorCover;
import com.taurus.playerbaselibrary.utils.SharedPrefer;

import static com.taurus.playerbaselibrary.holder.SettingHolder.KEY_PLAYER_IJK_DECODE_MEDIACODEC;

public class PlayerActivity extends ToolsActivity implements OnPlayerEventListener {

    private RelativeLayout mContainer;
    private StandardPlayer mPlayer;
    private DefaultReceiverCollections mCoverCollections;
    private VideoItem item;
    private VideoData videoData;
    private PlayCompleteCover completeCover;

    private int mCount;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCount++;
            if(mCount % 5 == 0){
                screenShot();
            }
            mHandler.sendEmptyMessageDelayed(0,1000);
        }
    };

    private void screenShot(){
        View renderView = mPlayer.getRenderView();
        if(renderView instanceof TextureView){
            Bitmap bitmap = ((TextureView) renderView).getBitmap();
            if(bitmap!=null){
                String info = FileEngine.bitmapToFile(bitmap, getExternalCacheDir(), System.currentTimeMillis() + ".png");
                System.out.println(info);
            }
        }

    }

    @Override
    public void onLoadState() {

    }

    @Override
    public View getContentView() {
        return View.inflate(this,R.layout.activity_player,null);
    }

    @Override
    protected void onParseIntent() {
        super.onParseIntent();
        item = (VideoItem) getIntent().getSerializableExtra("data");
        videoData = new VideoData(item.getPath());
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        fullScreen();
        keepScreenOn();
        mContainer = (RelativeLayout) findViewById(R.id.container);

        mPlayer = new StandardPlayer(this);
        mContainer.addView(mPlayer,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mCoverCollections = new DefaultReceiverCollections(this);
        mCoverCollections.buildDefault().addCover(PlayCompleteCover.KEY,completeCover = new PlayCompleteCover(this))
                .addCover(BasePlayerErrorCover.KEY_INT_DATA,new PlayerErrorCover(this));

        mPlayer.bindCoverCollections(mCoverCollections);

        BasePlayerControllerCover controllerCover = mCoverCollections.getReceiver(BasePlayerControllerCover.KEY);
        controllerCover.setVideoTitle(item.getDisplayName());
        controllerCover.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(completeCover!=null){
            completeCover.setOnCompleteListener(new OnCompleteCallBack(){
                @Override
                public void onReplay(PlayCompleteCover completeCover) {
                    super.onReplay(completeCover);
                    mPlayer.rePlay(0);
                }
            });
        }

        mPlayer.setOnPlayerEventListener(this);
        normalStart();
    }

    private void normalStart(){
        assertDecodeMode();
        mPlayer.setDataSource(videoData);
        mPlayer.start();
    }

    private void assertDecodeMode(){
        if(mPlayer.getWidgetMode()== IPlayer.WIDGET_MODE_DECODER){
            int playerType = DecoderType.getInstance().getDefaultPlayerType();
            if(playerType==1){
                boolean ijkMediaCodecOpen = SharedPrefer.getInstance().getBoolean(this, KEY_PLAYER_IJK_DECODE_MEDIACODEC, false);
                if(ijkMediaCodecOpen){
                    mPlayer.setDecodeMode(DecodeMode.HARD);
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(mPlayer!=null){
            mPlayer.doConfigChange(newConfig);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mPlayer!=null){
            mPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mPlayer!=null){
            mPlayer.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayer!=null){
            mPlayer.destroy();
        }
        mHandler.removeMessages(0);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
//                mHandler.sendEmptyMessage(0);
                break;
        }
    }

}

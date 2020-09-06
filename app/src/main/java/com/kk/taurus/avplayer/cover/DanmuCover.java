package com.kk.taurus.avplayer.cover;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.receiver.BaseCover;

import java.util.HashMap;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * @ClassName DanmuCover
 * @Description
 * @Author Taurus
 * @Date 2020/9/6 7:12 PM
 */
public class DanmuCover extends BaseCover {

    private IDanmakuView mDanmakuView;
    private DanmakuContext mDanmakuContext;

    public DanmuCover(Context context) {
        super(context);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();

        mDanmakuView = findViewById(R.id.layout_danmu_cover_danmu_view);
        mDanmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                mDanmakuView.start();
            }
            @Override
            public void updateTimer(DanmakuTimer timer) {

            }
            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }
            @Override
            public void drawingFinished() {

            }
        });

        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mDanmakuContext = DanmakuContext.create();
        mDanmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);

        mDanmakuView.prepare(new BaseDanmakuParser() {
            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        }, mDanmakuContext);
        mDanmakuView.showFPS(true);
        mDanmakuView.enableDanmakuDrawingCache(true);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.layout_danmu_cover, null, false);
    }

    @Override
    public int getCoverLevel() {
        return levelLow(1);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE:
                if (mDanmakuView != null && mDanmakuView.isPrepared()) {
                    mDanmakuView.pause();
                }
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESUME:
                if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
                    mDanmakuView.resume();
                }
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY:
                if (mDanmakuView != null) {
                    // dont forget release!
                    mDanmakuView.release();
                    mDanmakuView = null;
                }
                break;
        }
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onProducerEvent(int eventCode, Bundle bundle) {
        super.onProducerEvent(eventCode, bundle);
        if(eventCode == DataInter.ProducerEvent.ADD_DANMU_DATA){
            PLog.d("DanmuTest_Receive", bundle.toString());
            int position = bundle.getInt(EventKey.INT_DATA);
            String text = bundle.getString(EventKey.STRING_DATA);
            addDanmaku(false, position, text);
        }
    }

    private void addDanmaku(boolean islive, int position, String text) {
        if(mDanmakuContext==null)
            return;
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        // for(int i=0;i<100;i++){
        // }
        danmaku.text = text;
        danmaku.padding = 5;
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;
        danmaku.setTime(position + 1200);
        danmaku.textSize = 25f * (getContext().getResources().getDisplayMetrics().density - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;
        // danmaku.underlineColor = Color.GREEN;
        danmaku.borderColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);

    }

}

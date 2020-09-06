package com.kk.taurus.avplayer.event_producer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.Nullable;

import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.utils.RandomUtils;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.extension.BaseEventProducer;
import com.kk.taurus.playerbase.extension.EventCallback;
import com.kk.taurus.playerbase.log.PLog;

import java.util.Random;

/**
 * @ClassName DanmuDataProducer
 * @Description
 * @Author Taurus
 * @Date 2020/9/6 6:03 PM
 */
public class DanmuDataProducer extends BaseEventProducer {

    private final int MSG_LOAD_DANMU_DATA = 100;

    private final int LOAD_DANMU_DATA_PERIOD = 10 * 1000;

    private int mCurrStartMs = 1;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(getSender()==null || getPlayerStateGetter()==null)
                return;
            switch (msg.what){
                case MSG_LOAD_DANMU_DATA:
                    if(msg.arg1 != 0){
                        mCurrStartMs = msg.arg1;
                    }
                    final int count = new Random().nextInt(20);
                    for(int i=0;i<count;i++){
                        Bundle obtain = BundlePool.obtain();
                        obtain.putInt(EventKey.INT_DATA, mCurrStartMs + (new Random().nextInt(10)*1000));
                        obtain.putString(EventKey.STRING_DATA, RandomUtils.getRandomLowerLetterStr(RandomUtils.getRandomInt(3,20)));
                        getSender().sendEvent(DataInter.ProducerEvent.ADD_DANMU_DATA, obtain);
                        PLog.d("DanmuTest_Send", obtain.toString());
                    }
                    mCurrStartMs += LOAD_DANMU_DATA_PERIOD;
                    mHandler.sendEmptyMessageDelayed(MSG_LOAD_DANMU_DATA, LOAD_DANMU_DATA_PERIOD);
                    break;
            }
        }
    };

    @Override
    public void onAdded() {

    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void destroy() {

    }

    @Nullable
    @Override
    protected EventCallback getEventCallback() {
        return new EventCallback() {
            @Override
            public void onPlayerEvent(int eventCode, Bundle data) {
                switch (eventCode){
                    case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:
                        mHandler.sendEmptyMessage(MSG_LOAD_DANMU_DATA);
                        break;
                    case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                        int seekTo = getPlayerStateGetter()!=null?getPlayerStateGetter().getCurrentPosition():-1;
                        mHandler.removeMessages(MSG_LOAD_DANMU_DATA);
                        Message obtain = Message.obtain();
                        obtain.what = MSG_LOAD_DANMU_DATA;
                        obtain.arg1 = seekTo;
                        mHandler.sendMessage(obtain);
                        break;
                    case OnPlayerEventListener.PLAYER_EVENT_ON_STOP:
                    case OnPlayerEventListener.PLAYER_EVENT_ON_RESET:
                    case OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY:
                    case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
                        mHandler.removeMessages(MSG_LOAD_DANMU_DATA);
                        break;
                }
            }
            @Override
            public void onErrorEvent(int eventCode, Bundle data) {
                mHandler.removeMessages(MSG_LOAD_DANMU_DATA);
            }
            @Override
            public void onReceiverEvent(int eventCode, Bundle data) {

            }
        };
    }

}

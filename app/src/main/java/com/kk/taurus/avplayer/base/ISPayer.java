package com.kk.taurus.avplayer.base;


import android.view.ViewGroup;

import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.provider.IDataProvider;
import com.kk.taurus.playerbase.receiver.GroupValue;
import com.kk.taurus.playerbase.receiver.IReceiver;
import com.kk.taurus.playerbase.receiver.IReceiverGroup;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;

/**
 * Created by JiaJunHui on 2018/6/21.
 */
public interface ISPayer {

    int RECEIVER_GROUP_CONFIG_SILENCE_STATE = 1;
    int RECEIVER_GROUP_CONFIG_LIST_STATE = 2;
    int RECEIVER_GROUP_CONFIG_DETAIL_PORTRAIT_STATE = 3;
    int RECEIVER_GROUP_CONFIG_FULL_SCREEN_STATE = 4;

    void addOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener);

    boolean removePlayerEventListener(OnPlayerEventListener onPlayerEventListener);

    void addOnErrorEventListener(OnErrorEventListener onErrorEventListener);

    boolean removeErrorEventListener(OnErrorEventListener onErrorEventListener);

    void addOnReceiverEventListener(OnReceiverEventListener onReceiverEventListener);

    boolean removeReceiverEventListener(OnReceiverEventListener onReceiverEventListener);

    void setReceiverGroup(IReceiverGroup receiverGroup);

    IReceiverGroup getReceiverGroup();

    void removeReceiver(String receiverKey);

    void addReceiver(String key, IReceiver receiver);

    void attachContainer(ViewGroup userContainer);

    void play(DataSource dataSource);

    void play(DataSource dataSource, boolean updateRender);

    void setDataProvider(IDataProvider dataProvider);

    GroupValue getGroupValue();

    void updateGroupValue(String key, Object value);

    void registerOnGroupValueUpdateListener(IReceiverGroup.OnGroupValueUpdateListener onGroupValueUpdateListener);

    void unregisterOnGroupValueUpdateListener(IReceiverGroup.OnGroupValueUpdateListener onGroupValueUpdateListener);

    boolean isInPlaybackState();

    boolean isPlaying();

    int getCurrentPosition();

    int getState();

    void pause();

    void resume();

    void stop();

    void reset();

    void rePlay(int position);

    void destroy() ;

}

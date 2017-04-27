package com.kk.taurus.playerbase.inter;

import com.kk.taurus.playerbase.callback.BaseEventReceiver;

import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public interface IReceiverCollections {
    <T> T getCover(String key);
    List<BaseEventReceiver> getCovers();
}

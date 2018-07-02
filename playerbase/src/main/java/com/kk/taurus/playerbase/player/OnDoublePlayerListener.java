package com.kk.taurus.playerbase.player;

import com.kk.taurus.playerbase.entity.DataSource;

public interface OnDoublePlayerListener {

    DataSource getNextDataSource();

    void onNextHandlerPrepared(OnNextHandler nextHandler);

}

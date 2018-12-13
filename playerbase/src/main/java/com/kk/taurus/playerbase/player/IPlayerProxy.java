package com.kk.taurus.playerbase.player;

import android.os.Bundle;

import com.kk.taurus.playerbase.entity.DataSource;

/**
 * Created by Taurus on 2018/12/12.
 */
public interface IPlayerProxy {

    void onDataSourceReady(DataSource dataSource);

    void onIntentStop();

    void onIntentReset();

    void onIntentDestroy();

    void onPlayerEvent(int eventCode, Bundle bundle);

    void onErrorEvent(int eventCode, Bundle bundle);

    int getRecord(DataSource dataSource);

}

package com.kk.taurus.playerbase.record;

import com.kk.taurus.playerbase.entity.DataSource;

/**
 * Created by Taurus on 2018/12/12.
 */
public interface RecordKeyProvider {

    String generatorKey(DataSource dataSource);

}

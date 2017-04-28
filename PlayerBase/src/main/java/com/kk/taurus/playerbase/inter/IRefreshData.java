package com.kk.taurus.playerbase.inter;

import com.kk.taurus.playerbase.adapter.BaseVideoDataAdapter;
import com.kk.taurus.playerbase.setting.CoverData;

/**
 * Created by Taurus on 2017/4/28.
 */

public interface IRefreshData {
    void onRefreshDataAdapter(BaseVideoDataAdapter dataAdapter);
    void onRefreshCoverData(CoverData data);
}

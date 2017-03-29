package com.kk.taurus.playerbase.inter;

import com.kk.taurus.playerbase.cover.base.BaseCover;

import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public interface ICoverCollections {
    <T> T getCover(String key);
    List<BaseCover> getCovers();
}

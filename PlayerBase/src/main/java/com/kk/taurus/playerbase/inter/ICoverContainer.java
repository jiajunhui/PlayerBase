package com.kk.taurus.playerbase.inter;

import android.view.ViewGroup;

import com.kk.taurus.playerbase.cover.base.BaseCover;

/**
 * Created by Taurus on 2017/4/29.
 */

public interface ICoverContainer {
    void addCover(BaseCover cover);
    void removeCover(BaseCover cover);
    void removeAllCovers();
    boolean isContainsCover(BaseCover cover);
    int getCoverCount();
    ViewGroup getContainerRoot();
}

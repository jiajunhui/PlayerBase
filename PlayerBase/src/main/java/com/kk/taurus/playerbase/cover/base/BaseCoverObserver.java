package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.view.View;

import com.kk.taurus.playerbase.callback.CoverObserver;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BaseCoverObserver<T> implements CoverObserver<T> {

    protected Context mContext;
    protected BaseCoverCollections coverCollections;

    public BaseCoverObserver(Context context){
        this(context,null);
    }

    public BaseCoverObserver(Context context,BaseCoverCollections coverCollections){
        this.mContext = context;
        this.coverCollections = coverCollections;
    }

    @Override
    public View initCustomCoverView(Context context) {
        return null;
    }

    @Override
    public void onDataChange(T data) {

    }
}

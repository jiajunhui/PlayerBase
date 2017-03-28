package com.taurus.playerbaselibrary;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.kk.taurus.playerbase.cover.base.BaseCoverCollections;
import com.kk.taurus.playerbase.cover.base.BaseCoverObserver;

/**
 * Created by Taurus on 2017/3/27.
 */

public class LoadingObserver extends BaseCoverObserver<TestPlayData> {

    public LoadingObserver(Context context) {
        super(context);
    }

    public LoadingObserver(Context context, BaseCoverCollections coverCollections) {
        super(context, coverCollections);
    }

    @Override
    public View initCustomCoverView(Context context) {
        return View.inflate(context,R.layout.layout_loading,null);
    }

    @Override
    public void onCoverViewInit(View coverView) {

    }

    @Override
    public void onCoverVisibilityChange(View coverView, int visibility) {

    }

    @Override
    public void onDataChange(TestPlayData data) {
        super.onDataChange(data);
        Log.d("loading_observer",data==null?"null_data":data.getUrl());
    }
}

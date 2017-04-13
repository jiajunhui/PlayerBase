package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.inter.ILoadingCover;

/**
 * Created by Taurus on 2017/3/25.
 */

public abstract class BasePlayerLoadingCover extends BaseCover implements ILoadingCover{

    public static final String KEY = "loading_cover";

    private View mLoadingContainer;
    private View mLoadingView;
    private TextView mLoadingText;

    public BasePlayerLoadingCover(Context context){
        super(context);
    }

    public BasePlayerLoadingCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {
        mLoadingContainer = findViewById(R.id.cover_player_loading_container);
        mLoadingView = findViewById(R.id.cover_player_loading_view_loading);
        mLoadingText = findViewById(R.id.cover_player_loading_text_view_text);
    }

    @Override
    public void onNotifyNetWorkError() {
        setLoadingState(false);
    }

    @Override
    public void setLoadingState(boolean state) {
        setCoverVisibility(state?View.VISIBLE:View.GONE);
    }

    @Override
    public void setLoadingText(String text) {
        if(mLoadingText!=null){
            mLoadingText.setText(text);
        }
    }

}

package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.callback.CoverObserver;
import com.kk.taurus.playerbase.inter.IErrorCover;

/**
 * Created by Taurus on 2017/3/27.
 */

public abstract class BasePlayerErrorCover extends BaseCover implements IErrorCover{

    public static final String KEY = "error_cover";
    protected View mErrorView;
    protected ImageView mIvErrorIcon;
    protected TextView mTvTipText;

    public BasePlayerErrorCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {
        mErrorView = findViewById(R.id.cover_player_error_state_view_box);
        mIvErrorIcon = findViewById(R.id.cover_player_error_state_image_view_icon);
        mTvTipText = findViewById(R.id.cover_player_error_state_text_view_tip);
    }

    @Override
    public void setErrorState(boolean state) {
        setCoverVisibility(state? View.VISIBLE:View.GONE);
    }

    @Override
    public void setImageIcon(int resId) {
        if(mIvErrorIcon!=null){
            mIvErrorIcon.setImageResource(resId);
        }
    }

    @Override
    public void setErrorTipText(String text) {
        if(mTvTipText!=null){
            mTvTipText.setText(text);
        }
    }

}

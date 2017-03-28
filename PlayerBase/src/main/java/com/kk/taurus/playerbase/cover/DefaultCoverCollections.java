package com.kk.taurus.playerbase.cover;

import android.content.Context;

import com.kk.taurus.playerbase.cover.base.BaseAdCover;
import com.kk.taurus.playerbase.cover.base.BaseCoverCollections;
import com.kk.taurus.playerbase.cover.base.BaseGestureOperationCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerControllerCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerErrorCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerLoadingCover;
import com.kk.taurus.playerbase.setting.CoverData;

/**
 * Created by Taurus on 2017/3/24.
 */

public class DefaultCoverCollections extends BaseCoverCollections<CoverData> {

    public DefaultCoverCollections(Context context) {
        super(context);
    }

    public DefaultCoverCollections addPlayerControllerCover(BasePlayerControllerCover controllerCover){
        if(controllerCover==null)
            return this;
        putCover(BasePlayerControllerCover.KEY,controllerCover);
        return this;
    }

    public DefaultCoverCollections addPlayerLoadingCover(BasePlayerLoadingCover loadingCover){
        if(loadingCover==null)
            return this;
        putCover(BasePlayerLoadingCover.KEY,loadingCover);
        return this;
    }

    public DefaultCoverCollections addPlayerGestureCover(BaseGestureOperationCover gestureOperationCover){
        if(gestureOperationCover==null)
            return this;
        putCover(BaseGestureOperationCover.KEY,gestureOperationCover);
        return this;
    }

    public DefaultCoverCollections addPlayerErrorCover(BasePlayerErrorCover errorCover){
        if(errorCover==null)
            return this;
        putCover(BasePlayerErrorCover.KEY,errorCover);
        return this;
    }

    public DefaultCoverCollections addPlayerAdCover(BaseAdCover adCover){
        if(adCover==null)
            return this;
        putCover(BaseAdCover.KEY,adCover);
        return this;
    }

    public DefaultCoverCollections setDefaultPlayerControllerCover(){
        addPlayerControllerCover(new DefaultPlayerControllerCover(mContext,new DefaultCoverObserver(mContext)));
        return this;
    }

    public DefaultCoverCollections setDefaultPlayerLoadingCover(){
        addPlayerLoadingCover(new DefaultPlayerLoadingCover(mContext,new DefaultCoverObserver(mContext)));
        return this;
    }

    public DefaultCoverCollections setDefaultPlayerGestureCover(){
        addPlayerGestureCover(new DefaultPlayerGestureOperationCover(mContext,new DefaultCoverObserver(mContext)));
        return this;
    }

    public DefaultCoverCollections setDefaultPlayerErrorCover(){
        addPlayerErrorCover(new DefaultPlayerErrorCover(mContext,new DefaultCoverObserver(mContext)));
        return this;
    }

    public DefaultCoverCollections setDefaultPlayerAdCover(){
        addPlayerAdCover(new DefaultPlayerAdCover(mContext,new DefaultCoverObserver(mContext)));
        return this;
    }

    public DefaultCoverCollections buildDefault(){
        return setDefaultPlayerControllerCover()
                .setDefaultPlayerLoadingCover()
                .setDefaultPlayerGestureCover()
                .setDefaultPlayerErrorCover()
                .setDefaultPlayerAdCover();
    }

}

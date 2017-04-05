package com.kk.taurus.playerbase.cover;

import android.content.Context;

import com.kk.taurus.playerbase.cover.base.BaseAdCover;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.cover.base.BaseCoverCollections;
import com.kk.taurus.playerbase.cover.base.BaseFocusCover;
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
        return addCover(BasePlayerControllerCover.KEY,controllerCover);
    }

    public DefaultCoverCollections addPlayerLoadingCover(BasePlayerLoadingCover loadingCover){
        if(loadingCover==null)
            return this;
        return addCover(BasePlayerLoadingCover.KEY,loadingCover);
    }

    public DefaultCoverCollections addPlayerGestureCover(BaseGestureOperationCover gestureOperationCover){
        if(gestureOperationCover==null)
            return this;
        return addCover(BaseGestureOperationCover.KEY,gestureOperationCover);
    }

    public DefaultCoverCollections addPlayerErrorCover(BasePlayerErrorCover errorCover){
        if(errorCover==null)
            return this;
        return addCover(BasePlayerErrorCover.KEY,errorCover);
    }

    public DefaultCoverCollections addPlayerAdCover(BaseAdCover adCover){
        if(adCover==null)
            return this;
        return addCover(BaseAdCover.KEY,adCover);
    }

    public DefaultCoverCollections addCornerCutCover(CornerCutCover cornerCutCover){
        if(cornerCutCover==null)
            return this;
        return addCover(CornerCutCover.KEY,cornerCutCover);
    }

    public DefaultCoverCollections addFocusCover(BaseFocusCover focusCover){
        if(focusCover==null)
            return this;
        return addCover(BaseFocusCover.KEY,focusCover);
    }

    public DefaultCoverCollections addCover(String key, BaseCover cover){
        putCover(key,cover);
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

package com.kk.taurus.playerbase.cover;

import android.content.Context;

import com.kk.taurus.playerbase.callback.BaseEventReceiver;
import com.kk.taurus.playerbase.cover.base.BaseAdCover;
import com.kk.taurus.playerbase.cover.base.BaseReceiverCollections;
import com.kk.taurus.playerbase.cover.base.BaseFocusCover;
import com.kk.taurus.playerbase.cover.base.BaseGestureOperationCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerControllerCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerErrorCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerLoadingCover;

/**
 * Created by Taurus on 2017/3/24.
 *
 * cover覆盖层集合，添加cover层时注意add的顺序。
 *
 */

public class DefaultReceiverCollections extends BaseReceiverCollections {

    public DefaultReceiverCollections(Context context) {
        super(context);
    }

    public DefaultReceiverCollections addPlayerControllerCover(BasePlayerControllerCover controllerCover){
        if(controllerCover==null)
            return this;
        return addCover(BasePlayerControllerCover.KEY,controllerCover);
    }

    public DefaultReceiverCollections addPlayerLoadingCover(BasePlayerLoadingCover loadingCover){
        if(loadingCover==null)
            return this;
        return addCover(BasePlayerLoadingCover.KEY,loadingCover);
    }

    public DefaultReceiverCollections addPlayerGestureCover(BaseGestureOperationCover gestureOperationCover){
        if(gestureOperationCover==null)
            return this;
        return addCover(BaseGestureOperationCover.KEY,gestureOperationCover);
    }

    public DefaultReceiverCollections addPlayerErrorCover(BasePlayerErrorCover errorCover){
        if(errorCover==null)
            return this;
        return addCover(BasePlayerErrorCover.KEY,errorCover);
    }

    public DefaultReceiverCollections addPlayerAdCover(BaseAdCover adCover){
        if(adCover==null)
            return this;
        return addCover(BaseAdCover.KEY,adCover);
    }

    public DefaultReceiverCollections addCornerCutCover(CornerCutCover cornerCutCover){
        if(cornerCutCover==null)
            return this;
        return addCover(CornerCutCover.KEY,cornerCutCover);
    }

    public DefaultReceiverCollections addFocusCover(BaseFocusCover focusCover){
        if(focusCover==null)
            return this;
        return addCover(BaseFocusCover.KEY,focusCover);
    }

    public DefaultReceiverCollections addCover(String key, BaseEventReceiver cover){
        putCover(key,cover);
        return this;
    }

    public DefaultReceiverCollections setDefaultPlayerControllerCover(){
        addPlayerControllerCover(new DefaultPlayerControllerCover(mContext));
        return this;
    }

    public DefaultReceiverCollections setDefaultPlayerLoadingCover(){
        addPlayerLoadingCover(new DefaultPlayerLoadingCover(mContext));
        return this;
    }

    public DefaultReceiverCollections setDefaultPlayerGestureCover(){
        addPlayerGestureCover(new DefaultPlayerGestureOperationCover(mContext));
        return this;
    }

    public DefaultReceiverCollections setDefaultPlayerErrorCover(){
        addPlayerErrorCover(new DefaultPlayerErrorCover(mContext));
        return this;
    }

    public DefaultReceiverCollections setDefaultPlayerAdCover(){
        addPlayerAdCover(new DefaultPlayerAdCover(mContext));
        return this;
    }

    public DefaultReceiverCollections buildDefault(){
        return setDefaultPlayerControllerCover()
                .setDefaultPlayerLoadingCover()
                .setDefaultPlayerGestureCover()
                .setDefaultPlayerErrorCover()
                .setDefaultPlayerAdCover();
    }

}

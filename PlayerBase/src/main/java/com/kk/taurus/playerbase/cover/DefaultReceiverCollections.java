/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.kk.taurus.playerbase.cover;

import android.content.Context;

import com.kk.taurus.playerbase.eventHandler.BaseEventReceiver;
import com.kk.taurus.playerbase.cover.base.BaseCover;
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

    public DefaultReceiverCollections addCover(String key, BaseCover cover){
        putReceiver(key,cover);
        return this;
    }

    public DefaultReceiverCollections addEventReceiver(String key, BaseEventReceiver receiver){
        putReceiver(key,receiver);
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

    public DefaultReceiverCollections buildDefault(){
        return setDefaultPlayerControllerCover()
                .setDefaultPlayerLoadingCover()
                .setDefaultPlayerGestureCover()
                .setDefaultPlayerErrorCover();
    }

}

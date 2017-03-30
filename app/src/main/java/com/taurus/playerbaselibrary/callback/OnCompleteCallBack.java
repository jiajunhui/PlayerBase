package com.taurus.playerbaselibrary.callback;

import com.taurus.playerbaselibrary.cover.PlayCompleteCover;

/**
 * Created by Taurus on 2017/3/30.
 */

public class OnCompleteCallBack implements OnCompleteListener {
    @Override
    public void onReplay(PlayCompleteCover completeCover) {
        if(completeCover!=null){
            completeCover.setCompleteCoverState(false);
        }
    }
}

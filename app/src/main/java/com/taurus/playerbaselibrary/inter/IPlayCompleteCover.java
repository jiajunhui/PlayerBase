package com.taurus.playerbaselibrary.inter;

import com.taurus.playerbaselibrary.callback.OnCompleteListener;

/**
 * Created by Taurus on 2017/3/30.
 */

public interface IPlayCompleteCover {
    void setCompleteCoverState(boolean state);
    void setOnCompleteListener(OnCompleteListener onCompleteListener);
}

package com.kk.taurus.playerbase.inter;

/**
 * Created by Taurus on 2017/3/27.
 */

public interface IErrorCover {
    void setErrorState(boolean state);
    void setImageIcon(int resId);
    void setErrorTipText(String text);
}

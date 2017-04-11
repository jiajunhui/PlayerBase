package com.kk.taurus.playerbase.inter;

import com.kk.taurus.playerbase.callback.OnAdCoverClickListener;

/**
 * Created by Taurus on 2017/3/28.
 */

public interface IAdCover {
    void setAdCoverState(boolean state);
    void setImagePicState(boolean state);
    void setAdTimerState(boolean state);
    void setAdTimerText(String text);
    void setOnAdCoverClickListener(OnAdCoverClickListener onAdCoverClickListener);
}

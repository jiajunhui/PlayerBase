package com.kk.taurus.playerbase.inter;

/**
 * Created by Taurus on 2017/3/26.
 */

public interface IGestureOperation {
    void setVolumeBoxState(boolean state);
    void setVolumeIcon(int resId);
    void setVolumeText(String text);
    void setBrightnessBoxState(boolean state);
    void setBrightnessText(String text);
    void setFastForwardState(boolean state);
    void setFastForwardStepTime(String text);
    void setFastForwardProgressTime(String text);
}

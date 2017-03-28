package com.kk.taurus.playerbase.inter;

/**
 * Created by Taurus on 2017/3/24.
 */

public interface IPlayerControllerCover {
    void setTopContainerState(boolean state);
    void setBottomContainerState(boolean state);
    void setControllerState(boolean state);
    void updateSystemTime();
    void updateBatteryState(int batteryValue);
    void setPlayState(boolean isPlaying);
    void setPlayTime(long curr, long total);
    void setSeekMax(int max);
    void setSeekProgress(int progress);
    void setSeekSecondProgress(int progress);
    int getSeekProgress();
    void resetSeekBar();
    void resetPlayTime();
}

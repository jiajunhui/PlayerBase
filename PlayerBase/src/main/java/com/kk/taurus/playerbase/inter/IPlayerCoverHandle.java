package com.kk.taurus.playerbase.inter;

import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.Rate;

import java.util.List;

/**
 * Created by Taurus on 2017/4/20.
 */

public interface IPlayerCoverHandle {
    void pause();
    void resume();
    void seekTo(int msc);
    void stop();
    void rePlay(int msc);
    boolean isPlaying();
    int getCurrentPosition();
    int getDuration();
    int getBufferPercentage();
    int getStatus();
    Rate getCurrentDefinition();
    List<Rate> getVideoDefinitions();
    void changeVideoDefinition(Rate rate);
    void setAspectRatio(AspectRatio aspectRatio);
}

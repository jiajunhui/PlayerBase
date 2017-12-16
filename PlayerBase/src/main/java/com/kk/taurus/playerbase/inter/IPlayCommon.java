package com.kk.taurus.playerbase.inter;

import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by mtime on 2017/12/13.
 */

public interface IPlayCommon {

    void setDataSource(VideoData data);
    void start();
    void start(int msc);
    void pause();
    void resume();
    void seekTo(int msc);
    void stop();
    void reset();
    boolean isPlaying();
    int getCurrentPosition();
    int getDuration();
    int getBufferPercentage();
    int getAudioSessionId();
    int getStatus();
    /** get current playing video definition*/
    Rate getCurrentDefinition();
    /** get current playing data source all definitions*/
    List<Rate> getVideoDefinitions();
    /** change playing video definition*/
    void changeVideoDefinition(Rate rate);
    void setDecodeMode(DecodeMode decodeMode);
    DecodeMode getDecodeMode();
    void destroy();

}

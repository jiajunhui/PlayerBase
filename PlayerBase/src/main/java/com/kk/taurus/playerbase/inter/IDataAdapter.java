package com.kk.taurus.playerbase.inter;

/**
 * Created by Taurus on 2017/4/19.
 */

public interface IDataAdapter<T> {

    //返回下一个播放实例对象
    T getLoopNextPlayEntity();

    //返回当前播放实例对象
    T getPlayEntity();

    //返回指定索引位置的实例对象
    T getPlayEntity(int index);

    //返回当前列表索引位置
    int getIndex();

    //返回前一个播放索引
    int getPreIndex();

    //指定当前列表索引位置
    void setIndex(int index);

    //返回播放列表长度
    int getCount();

    //设置是否循环播放
    void setLoop(boolean loop);

}

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

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

import android.support.annotation.MainThread;

import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.plan.IEventBinder;

import java.util.List;

/**
 * Created by mtime on 2017/10/19.
 * 用于处理数据的转换，比如需求中提供视频的id，我们需要依据id向服务器请求实际的播放地址，然后再把数据提供给播放器。
 */

public interface IDataProvider {

    int EXCEPTION_TYPE_LOAD_FAILURE = 1;

    int EVENT_CODE_START_HANDLE_SOURCE_DATA = 80000;
    int EVENT_CODE_START_LOADING_URL_DATA = 80001;
    int EVENT_CODE_END_LOADING_URL_DATA = 80002;

    void setEventBinder(IEventBinder eventBinder);

    void handleSourceData(VideoData data);

    void setOnProviderListener(OnProviderListener onProviderListener);

    interface OnProviderListener{
        @MainThread
        void onProvideDataSource(VideoData data);

        @MainThread
        void onProvideDefinitionList(List<Rate> rates);

        @MainThread
        void onProvideError(int type, String message);
    }

}

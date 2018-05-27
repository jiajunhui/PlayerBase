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

package com.kk.taurus.playerbase.provider;

import android.os.Bundle;

import com.kk.taurus.playerbase.entity.DataSource;

/**
 * Created by Taurus on 2018/3/17.
 *
 * Data providers are designed for special needs.
 * For example, if you need to take a id to get the playback address,then play it.
 * In this case, the process can be independent of a data provider.
 *
 */

public interface IDataProvider {

    int PROVIDER_CODE_SUCCESS_MEDIA_DATA = -77001;

    int PROVIDER_CODE_DATA_PROVIDER_ERROR = -77003;

    void setOnProviderListener(OnProviderListener onProviderListener);

    /**
     * the provider handle data source, Users usually need to be implemented
     * @param sourceData
     */
    void handleSourceData(DataSource sourceData);

    /**
     * cancel the DataProvider handle data source.
     */
    void cancel();

    /**
     * destroy the provider.
     */
    void destroy();


    interface OnProviderListener{
        /**
         * on provider start load data
         */
        void onProviderDataStart();

        /**
         * on provider load data success
         * @param code
         * @param bundle you can set some data to bundle
         */
        void onProviderDataSuccess(int code, Bundle bundle);

        /**
         * on provider load data error
         * @param code
         * @param bundle
         */
        void onProviderError(int code, Bundle bundle);
    }

}

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

package com.kk.taurus.playerbase.event;

import android.os.Bundle;

import com.kk.taurus.playerbase.log.PLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2018/3/17.
 *
 * In order to improve memory performance,
 * the bundle entities passed in the framework
 * come from the bundle buffer pool.
 *
 */

public class BundlePool {

    private static final int POOL_SIZE = 3;

    private static List<Bundle> mPool;

    static {
        mPool = new ArrayList<>();
        for(int i=0;i<POOL_SIZE;i++)
            mPool.add(new Bundle());
    }

    public synchronized static Bundle obtain(){
        for(int i=0;i<POOL_SIZE;i++){
            if(mPool.get(i).isEmpty()){
                return mPool.get(i);
            }
        }
        PLog.w("BundlePool","<create new bundle object>");
        return new Bundle();
    }

}

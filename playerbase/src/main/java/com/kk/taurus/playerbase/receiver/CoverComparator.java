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

package com.kk.taurus.playerbase.receiver;

import java.util.Comparator;

public class CoverComparator implements Comparator<IReceiver> {
    @Override
    public int compare(IReceiver o1, IReceiver o2) {
        int x = 0;
        int y = 0;
        if(o1 instanceof BaseCover){
            x = ((BaseCover) o1).getCoverLevel();
        }
        if(o2 instanceof BaseCover){
            y = ((BaseCover) o2).getCoverLevel();
        }
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}

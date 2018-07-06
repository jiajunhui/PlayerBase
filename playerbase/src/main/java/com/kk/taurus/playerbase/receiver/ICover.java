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

import android.view.View;

/**
 * Created by Taurus on 2017/3/24.
 */

public interface ICover {

    //the max cover priority value per level container.
    int LEVEL_MAX = 1 << 5;

    //level low container start value.
    int COVER_LEVEL_LOW     = 0;

    //level medium container start value.
    int COVER_LEVEL_MEDIUM  = 1 << 5;

    //level high container start value.
    int COVER_LEVEL_HIGH    = 1 << 6;

    void setCoverVisibility(int visibility);
    View getView();
    int getCoverLevel();
}

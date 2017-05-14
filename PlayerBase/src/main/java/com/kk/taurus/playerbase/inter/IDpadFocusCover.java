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
 * Created by Taurus on 2017/4/5.
 */

public interface IDpadFocusCover {

    int EVENT_CODE_ACTION_DOWN_BACK = -44001;
    int EVENT_CODE_ACTION_DOWN_ENTER = -44002;
    int EVENT_CODE_ACTION_DOWN_LEFT = -44003;
    int EVENT_CODE_ACTION_DOWN_UP = -44004;
    int EVENT_CODE_ACTION_DOWN_RIGHT = -44005;
    int EVENT_CODE_ACTION_DOWN_DOWN = -44006;
    int EVENT_CODE_ACTION_DOWN_MENU = -44007;

    int EVENT_CODE_ACTION_UP_BACK = -44010;
    int EVENT_CODE_ACTION_UP_ENTER = -44012;
    int EVENT_CODE_ACTION_UP_LEFT = -44013;
    int EVENT_CODE_ACTION_UP_UP = -44014;
    int EVENT_CODE_ACTION_UP_RIGHT = -44015;
    int EVENT_CODE_ACTION_UP_DOWN = -44016;
    int EVENT_CODE_ACTION_UP_MENU = -44017;

}

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

    int EVENT_CODE_ACTION_DOWN_BACK = 40001;
    int EVENT_CODE_ACTION_DOWN_ENTER = 40002;
    int EVENT_CODE_ACTION_DOWN_LEFT = 40003;
    int EVENT_CODE_ACTION_DOWN_UP = 40004;
    int EVENT_CODE_ACTION_DOWN_RIGHT = 40005;
    int EVENT_CODE_ACTION_DOWN_DOWN = 40006;
    int EVENT_CODE_ACTION_DOWN_MENU = 40007;

    int EVENT_CODE_ACTION_UP_BACK = 40010;
    int EVENT_CODE_ACTION_UP_ENTER = 40012;
    int EVENT_CODE_ACTION_UP_LEFT = 40013;
    int EVENT_CODE_ACTION_UP_UP = 40014;
    int EVENT_CODE_ACTION_UP_RIGHT = 40015;
    int EVENT_CODE_ACTION_UP_DOWN = 40016;
    int EVENT_CODE_ACTION_UP_MENU = 40017;

}

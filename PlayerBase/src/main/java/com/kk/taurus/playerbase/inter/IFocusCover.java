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

import android.view.KeyEvent;
import android.view.View;

/**
 * Created by Taurus on 2017/3/31.
 */

public interface IFocusCover {

    void setFocusable(boolean focusable);
    void requestFocus();

    boolean onKeyActionDownDpadLeft(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadUp(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadRight(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadDown(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadEnter(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadBack(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadMenu(View v, int keyCode, KeyEvent event);

    boolean onKeyActionUpDpadLeft(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadUp(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadRight(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadDown(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadEnter(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadBack(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadMenu(View v, int keyCode, KeyEvent event);
    
}

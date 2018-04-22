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

/**
 * Created by Taurus on 2018/3/17.
 */

public interface EventKey {

    String BYTE_DATA = "byte_data";
    String INT_DATA = "int_data";
    String BOOL_DATA = "bool_data";
    String FLOAT_DATA = "float_data";
    String LONG_DATA = "long_data";
    String DOUBLE_DATA = "double_data";
    String STRING_DATA = "string_data";

    String SERIALIZABLE_DATA = "serializable_data";
    String SERIALIZABLE_EXTRA_DATA = "serializable_extra_data";

    String INT_ARG1 = "int_arg1";
    String INT_ARG2 = "int_arg2";
    String INT_ARG3 = "int_arg3";
    String INT_ARG4 = "int_arg4";


}

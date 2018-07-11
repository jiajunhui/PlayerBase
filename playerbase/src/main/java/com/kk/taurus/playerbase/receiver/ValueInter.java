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

interface ValueInter {

    void putBoolean(String key, boolean value);
    void putBoolean(String key, boolean value, boolean notifyUpdate);

    void putInt(String key, int value);
    void putInt(String key, int value, boolean notifyUpdate);

    void putString(String key, String value);
    void putString(String key, String value, boolean notifyUpdate);

    void putFloat(String key, float value);
    void putFloat(String key, float value, boolean notifyUpdate);

    void putLong(String key, long value);
    void putLong(String key, long value, boolean notifyUpdate);

    void putDouble(String key, double value);
    void putDouble(String key, double value, boolean notifyUpdate);

    void putObject(String key, Object value);
    void putObject(String key, Object value, boolean notifyUpdate);



    <T> T get(String key);

    boolean getBoolean(String key);
    boolean getBoolean(String key, boolean defaultValue);

    int getInt(String key);
    int getInt(String key, int defaultValue);

    String getString(String key);

    float getFloat(String key);
    float getFloat(String key, float defaultValue);

    long getLong(String key);
    long getLong(String key, long defaultValue);

    double getDouble(String key);
    double getDouble(String key, double defaultValue);

}

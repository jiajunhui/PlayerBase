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

package com.taurus.playerbaselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * Created by Taurus on 2017/3/30.
 */

public class SharedPrefer {

    public static final String configName = "setting";
    private static SharedPrefer instance = new SharedPrefer();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String fileName;

    private SharedPrefer(){
    }

    public synchronized static SharedPrefer getInstance(){
        fileName = configName;
        return instance;
    }

    public synchronized static SharedPrefer getInstance(String name){
        fileName = name;
        return instance;
    }

    public synchronized void saveString(Context context, String key, @Nullable String value) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public synchronized void saveStringSet(Context context, String key, @Nullable Set<String> values) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }

    public synchronized void saveInt(Context context, String key, int value) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public synchronized void saveLong(Context context, String key, long value) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public synchronized void saveFloat(Context context, String key, float value) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public synchronized void saveBoolean(Context context, String key, boolean value) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }



    public synchronized String getString(Context context, String key, @Nullable String defaultValue) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,defaultValue);
    }

    public synchronized Set<String> getStringSet(Context context, String key, @Nullable Set<String> defaultValue) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key,defaultValue);
    }

    public synchronized int getInt(Context context, String key, int defaultValue) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,defaultValue);
    }

    public synchronized long getLong(Context context, String key, long defaultValue) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key,defaultValue);
    }

    public synchronized float getFloat(Context context, String key, float defaultValue) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key,defaultValue);
    }

    public synchronized boolean getBoolean(Context context, String key, boolean defaultValue) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,defaultValue);
    }

    public synchronized void remove(Context context, String key) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public synchronized void clear(Context context) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}

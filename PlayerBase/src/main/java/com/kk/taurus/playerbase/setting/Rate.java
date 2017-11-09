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

package com.kk.taurus.playerbase.setting;

import java.io.Serializable;

/**
 * Created by cyw on 2016/3/1.
 */
public class Rate implements Serializable{

    private int definitionType;

    private String rate_key;
    private String rate_value;
    private String rate_data;

    public Rate() {
    }

    public Rate(String rate_key, String rate_value) {
        this.rate_key = rate_key;
        this.rate_value = rate_value;
    }

    public Rate(int definitionType, String rate_key, String rate_value) {
        this.definitionType = definitionType;
        this.rate_key = rate_key;
        this.rate_value = rate_value;
    }

    public int getDefinitionType() {
        return definitionType;
    }

    public void setDefinitionType(int definitionType) {
        this.definitionType = definitionType;
    }

    public String getRate_key() {
        return rate_key;
    }

    public void setRate_key(String rate_key) {
        this.rate_key = rate_key;
    }

    public String getRate_value() {
        return rate_value;
    }

    public void setRate_value(String rate_value) {
        this.rate_value = rate_value;
    }

    public String getDefinition(){
        return rate_value;
    }

    public String getRate_data() {
        return rate_data;
    }

    public void setRate_data(String rate_data) {
        this.rate_data = rate_data;
    }
}

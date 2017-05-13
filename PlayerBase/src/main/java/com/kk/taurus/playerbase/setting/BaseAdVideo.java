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


/**
 * Created by Taurus on 2016/9/29.
 */
public class BaseAdVideo extends VideoData {

    private String redirectUrl;

    public BaseAdVideo(String data) {
        super(data);
    }

    public BaseAdVideo(String data, Rate rate) {
        super(data, rate);
    }

    public BaseAdVideo(String data, int playerType, Rate rate) {
        super(data, playerType, rate);
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}

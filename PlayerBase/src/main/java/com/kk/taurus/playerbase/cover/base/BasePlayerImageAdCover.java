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

package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.inter.IAdImageCover;
import com.kk.taurus.playerbase.setting.BaseAdImage;

import java.util.List;

/**
 * Created by Taurus on 2017/4/24.
 */

public abstract class BasePlayerImageAdCover extends BaseCover implements IAdImageCover{

    protected View mAdBox;
    protected ImageView mIvPic;
    protected List<BaseAdImage> mAdImages;

    public BasePlayerImageAdCover(Context context) {
        super(context);
    }

    public BasePlayerImageAdCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {
        this.mAdImages = getAdImages();
        mAdBox = findViewById(R.id.cover_player_image_ad_box);
        mIvPic = findViewById(R.id.cover_player_image_ad_image_view_pic);
    }

    @Override
    public void setImageAdState(boolean state) {
        setCoverVisibility(state?View.VISIBLE:View.GONE);
    }

    @Override
    public void setAdBoxState(boolean state) {
        if(mAdBox==null)
            return;
        mAdBox.setVisibility(state?View.VISIBLE:View.GONE);
    }

    @Override
    public View initCoverLayout(Context context) {
        return View.inflate(context, R.layout.layout_player_image_ad,null);
    }

    protected abstract List<BaseAdImage> getAdImages();

}

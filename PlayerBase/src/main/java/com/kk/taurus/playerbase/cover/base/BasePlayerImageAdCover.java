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

public class BasePlayerImageAdCover extends BaseCoverPlayerHandle implements IAdImageCover{

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
        mAdBox = findViewById(R.id.cover_player_image_ad_box);
        mIvPic = findViewById(R.id.cover_player_image_ad_image_view_pic);
    }

    @Override
    public void refreshAdData(List<BaseAdImage> adImages) {
        this.mAdImages = adImages;
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
}

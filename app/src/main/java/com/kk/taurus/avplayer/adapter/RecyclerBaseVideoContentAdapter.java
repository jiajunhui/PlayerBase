package com.kk.taurus.avplayer.adapter;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.bean.RecyclerBaseVideoBean;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.render.AspectRatio;
import com.kk.taurus.playerbase.widget.BaseVideoView;

import java.util.List;

/**
 * @author KaraShokZ (张耀中)
 * DESCRIPTION
 * @name RecyclerBaseVideoContentAdapter
 * @date 2021/01/15 14:48
 */
public class RecyclerBaseVideoContentAdapter extends RecyclerView.Adapter<RecyclerBaseVideoContentAdapter.BaseViewHolder> {

    private List<RecyclerBaseVideoBean> dataList;
    private BaseVideoView typeLiveVideoBvv;

    public RecyclerBaseVideoContentAdapter(List<RecyclerBaseVideoBean> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            return new BaseViewHolder(View.inflate(parent.getContext(), R.layout.activity_recycler_base_video_type_video, null));
        }else {
            return new BaseViewHolder(View.inflate(parent.getContext(), R.layout.activity_recycler_base_video_type_item, null));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).itemType;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        RecyclerBaseVideoBean bean = dataList.get(position);
        if (bean.itemType == 1){
            if (typeLiveVideoBvv == null){
                typeLiveVideoBvv = holder.getView(R.id.activity_recycler_base_video_type_video_bvv);
                typeLiveVideoBvv.setOvalRectShape();
//                typeLiveVideoBvv.setRoundRectShape(30);
                typeLiveVideoBvv.setAspectRatio(AspectRatio.AspectRatio_MATCH_PARENT);
                typeLiveVideoBvv.setDataSource(new DataSource(bean.videoUrl));
                typeLiveVideoBvv.start();
            }
        }else {
            TextView typeStoreAddressTv = holder.getView(R.id.activity_recycler_base_video_type_item_tv);
            typeStoreAddressTv.setText(bean.itemStr);
        }
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        public <T extends View> T getView(@IdRes int viewId){
            return mItemView.findViewById(viewId);
        }
    }
}

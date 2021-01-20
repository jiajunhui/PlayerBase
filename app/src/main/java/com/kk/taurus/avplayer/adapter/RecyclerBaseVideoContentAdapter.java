package com.kk.taurus.avplayer.adapter;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.bean.RecyclerBaseVideoBean;
import com.kk.taurus.avplayer.cover.LoadingCover;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.widget.BaseVideoView;

import java.util.List;

import static com.kk.taurus.avplayer.play.DataInter.ReceiverKey.KEY_LOADING_COVER;

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

    public void onDestroy(){
        if(typeLiveVideoBvv!=null)
            typeLiveVideoBvv.stopPlayback();
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
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    typeLiveVideoBvv.setRoundRectShape(30);
                ReceiverGroup receiverGroup = new ReceiverGroup();
                receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(typeLiveVideoBvv.getContext()));
                typeLiveVideoBvv.setReceiverGroup(receiverGroup);
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

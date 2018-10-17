package com.kk.taurus.avplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kk.taurus.avplayer.R;

import java.util.List;

import com.kk.taurus.avplayer.bean.SettingItem;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingItemHolder> {

    private Context mContext;
    private List<SettingItem> mItems;

    private OnItemClickListener<SettingItemHolder, SettingItem> onItemClickListener;

    public SettingAdapter(Context context, List<SettingItem> list){
        this.mContext = context;
        this.mItems = list;
    }

    public void setOnItemClickListener(OnItemClickListener<SettingItemHolder, SettingItem> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SettingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SettingItemHolder(View.inflate(mContext, R.layout.item_setting, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final SettingItemHolder holder, final int position) {
        final SettingItem item = mItems.get(position);
        holder.itemView.setBackgroundColor((item.getCode()/100)%2==0?Color.WHITE:Color.parseColor("#EEEEEE"));
        holder.settingText.setText(item.getItemText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder, item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mItems!=null)
            return mItems.size();
        return 0;
    }

    public static class SettingItemHolder extends RecyclerView.ViewHolder{

        TextView settingText;

        public SettingItemHolder(View itemView) {
            super(itemView);
            settingText = itemView.findViewById(R.id.settingText);
        }

    }

}

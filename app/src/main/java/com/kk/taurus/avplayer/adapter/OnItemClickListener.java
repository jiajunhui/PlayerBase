package com.kk.taurus.avplayer.adapter;

import androidx.recyclerview.widget.RecyclerView;

public interface OnItemClickListener<H extends RecyclerView.ViewHolder, T> {

    void onItemClick(H holder, T item, int position);

}

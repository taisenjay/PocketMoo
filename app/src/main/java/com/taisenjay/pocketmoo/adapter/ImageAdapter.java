package com.taisenjay.pocketmoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.taisenjay.pocketmoo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : WangJian
 * Date   : 2017/7/2
 * Created by a handsome boy with love
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    List<String> datas = new ArrayList<>();
    Context mContext;

    public ImageAdapter(Context context) {
        this.mContext = context;
    }

    public void addAll(List<String> images){
        datas.addAll(images);
        notifyDataSetChanged();
    }

    public void clear(){
        datas.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(datas.get(position)).placeholder(R.mipmap.img_default).into(holder.ivShortcut);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivShortcut;

        public ViewHolder(View itemView) {
            super(itemView);
            ivShortcut = (ImageView) itemView.findViewById(R.id.photo_view);
        }
    }

}

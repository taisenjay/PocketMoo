package com.taisenjay.pocketmoo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.model.NewsSimple;
import com.taisenjay.pocketmoo.ui.HtmlReadActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : WangJian
 * Date   : 2017/7/3
 * Created by a handsome boy with love
 */

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private List<NewsSimple> mDatas = new ArrayList<>();

    private Context mContext;

    public NewsAdapter(Context context) {
        mContext = context;
    }

    public void addAll(List<NewsSimple> datas){
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void clear(){
        mDatas.clear();
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsSimple news = mDatas.get(position);
        holder.title.setText(news.title);
//        holder.content.setText(news.contentPart);
        Glide.with(mContext).load(news.coverUrl).placeholder(R.mipmap.img_default).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView title;

        CardView cvBox;

        public ViewHolder(View itemView) {
            super(itemView);
            cvBox = (CardView) itemView;
            cover = (ImageView) itemView.findViewById(R.id.iv_news_cover);
            title = (TextView) itemView.findViewById(R.id.tv_news_title);
            cvBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewsSimple news = mDatas.get(getLayoutPosition());

                    Intent intent = new Intent(mContext, HtmlReadActivity.class);
                    intent.putExtra(HtmlReadActivity.EXTRA_NEWS,news);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

package com.taisenjay.pocketmoo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.database.DBUtil;
import com.taisenjay.pocketmoo.model.Movie;
import com.taisenjay.pocketmoo.ui.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : WangJian
 * Date   : 2017/7/1
 * Created by a handsome boy with love
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private int index = 0;

    private List<Movie> datas = new ArrayList<>();
    private Context mContext;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getDataSize(){
        return datas.size();
    }

    public MoviesAdapter(Context context){
        this.mContext = context;
    }

    public void addAll(List<Movie> movies){
        datas.addAll(movies);
        notifyDataSetChanged();
    }

    public void clear(){
        datas.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_box, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = datas.get(position);
        String title = movie.publishTime == null ?
                movie.identityCode : movie.identityCode
                + " | " + movie.publishTime;
        holder.tvNoAndDate.setText(title);
        Glide.with(mContext).load(movie.coverUrl)
                .placeholder(R.mipmap.img_default)
                .into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvBox;
        ImageView ivCover;
        TextView tvNoAndDate;

        public ViewHolder(View itemView) {
            super(itemView);
            cvBox = (CardView) itemView;
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            tvNoAndDate = (TextView) itemView.findViewById(R.id.tv_title);
            cvBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Movie movie = datas.get(getLayoutPosition());
                    if (index == 1)
                        movie = datas.get(getLayoutPosition()-1);
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);

                    if (movie.detailUrl != null ) {
                        String movieUrlCode = movie.detailUrl.substring(25, movie.detailUrl.length());
                        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_URL, movieUrlCode);
                    }
                    intent.putExtra("bean",movie);
                    intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID,movie.identityCode);
                    mContext.startActivity(intent);
                }
            });

            if (index == 2){
                cvBox.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        new AlertDialog.Builder(mContext)
                                .setTitle("提示")
                                .setMessage("从我的电影中移除该电影?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(DBUtil.removeMyMovie(mContext,datas.get(getLayoutPosition())) > 0 ){
                                            Toast.makeText(mContext,
                                                    mContext.getString(R.string.rm_success),
                                                    Toast.LENGTH_SHORT).show();
                                            datas.remove(datas.get(getLayoutPosition()));
                                            notifyDataSetChanged();
                                        }else
                                            Toast.makeText(mContext,
                                                    mContext.getString(R.string.rm_fail),
                                                    Toast.LENGTH_SHORT).show();
                                        Log.e("db",index + "");
                                    }
                                })
                                .create().show();


                        return true;
                    }
                });
            }
        }
    }




}

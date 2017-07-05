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
import com.taisenjay.pocketmoo.model.StarSimple;
import com.taisenjay.pocketmoo.ui.StarHomePageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : WangJian
 * Date   : 2017/7/1
 * Created by a handsome boy with love
 */

public class StarsAdapter extends RecyclerView.Adapter<StarsAdapter.ViewHolder>{

    private List<StarSimple> datas = new ArrayList<>();
    private Context mContext;

    public StarsAdapter( Context context) {
        mContext = context;
    }

    public void addAll(List<StarSimple> stars){
        datas.addAll(stars);
        notifyDataSetChanged();
    }

    public void clear(){
        datas.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_star, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StarSimple star = datas.get(position);
        holder.tvName.setText(star.name);
        Glide.with(mContext).load(star.avatar)
                .placeholder(R.mipmap.img_default)
                .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cvBox;
        ImageView ivAvatar;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            cvBox = (CardView) itemView;
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_cover);
            tvName = (TextView) itemView.findViewById(R.id.tv_title);
            cvBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StarSimple star = datas.get(getLayoutPosition());

                    Intent intent = new Intent(mContext, StarHomePageActivity.class);
                    intent.putExtra(StarHomePageActivity.EXTRA_STAR_DATA,star);
                    intent.putExtra("avatar",star.avatar);
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation((Activity) mContext, ivAvatar,
//                                    Constants.KEY_SHARE_ANIM_AVATAR);
//                    ActivityCompat.startActivity(mContext, intent, options.toBundle());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}

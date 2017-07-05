package com.taisenjay.pocketmoo.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.model.Torrent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : WangJian
 * Date   : 2017/7/2
 * Created by a handsome boy with love
 */

public class TorrentAdapter extends RecyclerView.Adapter<TorrentAdapter.ViewHolder>{

    private List<Torrent> datas = new ArrayList<>();
    private Context mContext;

    public TorrentAdapter(Context context,List<Torrent> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    public void addAll(List<Torrent> datas){
        datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void clear(){
        datas.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("TorrentAdapter","onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_torrent, null, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("TorrentAdapter","onBindViewHolder");
        Torrent item = datas.get(position);
        holder.tvName.setText(item.name);
        holder.tvInfo.setText(item.size + " / " + item.date);
    }

    @Override
    public int getItemCount() {
        Log.e("TorrentAdapter","getItemCount");
        Log.e("TorrentAdapter",datas.size() + "");
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_file_name);
            tvInfo = (TextView) itemView.findViewById(R.id.tv_file_info);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String tAddress = datas.get(getLayoutPosition()).address;
                    new AlertDialog.Builder(mContext)
                            .setTitle(mContext.getString(R.string.torrent_title))
                            .setIcon(R.mipmap.icon_warn)
                            .setMessage(tAddress)
                            .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    return;
                                }
                            })
                            .setPositiveButton(mContext.getString(R.string.torrent_copy), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ClipboardManager cm = (ClipboardManager)
                                            mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                    cm.setText(tAddress.substring(0,tAddress.length()/2) + " ...");
                                    Toast.makeText(mContext,mContext.getString(R.string.torrent_copied)
                                            ,Toast.LENGTH_SHORT)
                                            .show();
                                    return;
                                }
                            })
                            .show();
                }
            });
        }
    }
}

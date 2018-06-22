package com.pinky.romanticphotoframes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.utils.App;
import com.pinky.romanticphotoframes.utils.Utils;

import java.util.ArrayList;

/**
 * Created by 2117 on 4/17/2018.
 */
public class MoreAppAdapter extends RecyclerView.Adapter<MoreAppAdapter.CardViewHolder3> {
    ArrayList<App> frmimgs;
    Context context;

    public MoreAppAdapter(Context context, ArrayList<App> filepaths) {
        this.frmimgs = filepaths;
        this.context = context;


    }


    @Override
    public int getItemCount() {

        return frmimgs.size();
    }

    @Override
    public CardViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.more_apps_adapter_text_image, parent, false);

        return new CardViewHolder3(rowView);
    }


    public void onBindViewHolder(CardViewHolder3 holder, int position) {
        ViewGroup.LayoutParams lya_params = holder.cardapp.getLayoutParams();

        lya_params.width = Utils.scwidth_u / 2;
        lya_params.height = (int) (Utils.scwidth_u / 2.2);
//        height / 3


        ViewGroup.LayoutParams iv_params = holder.iv_icon.getLayoutParams();
        iv_params.width = Utils.scwidth_u / 3;
        iv_params.height = Utils.scwidth_u / 3;

        holder.tv_name.setTextColor(Color.parseColor("#000000"));

        Glide.with(context).load(Utils.updateAppsList.get(position).getImgUrl())
                .placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
                .into(holder.iv_icon);


       /* Glide.with(context).load(Utils.updateAppsList.get(position).getImgUrl().toString())
                .placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
                .into(holder.iv_icon);*/
        holder.tv_name.setText(Utils.updateAppsList.get(position).getAppName());


    }

    public class CardViewHolder3 extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        TextView tv_name;

        CardView cardapp;

        public CardViewHolder3(View itemView) {
            super(itemView);
            cardapp = (CardView) itemView.findViewById(R.id.cardapp);
            iv_icon = (ImageView) itemView
                    .findViewById(R.id.iv_cache_icon);
            tv_name = (TextView) itemView
                    .findViewById(R.id.tv_cache_name);

        }
    }

}

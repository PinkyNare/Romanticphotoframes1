package com.pinky.romanticphotoframes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.utils.Utils;

/**
 * Created by 2117 on 4/17/2018.
 */
public class FramesEfectsAdapter extends RecyclerView.Adapter<FramesEfectsAdapter.CardViewHolder3> {
    int[] frmimgs;
    Context context;

    public FramesEfectsAdapter(Context context, int[] filepaths) {
        this.frmimgs = filepaths;
        this.context = context;


    }


    @Override
    public int getItemCount() {

        return frmimgs.length;
    }

    @Override
    public CardViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_cardviewfils, parent, false);

        return new CardViewHolder3(rowView);
    }


    public void onBindViewHolder(CardViewHolder3 holder, int position) {

        holder.ivPic.getLayoutParams().height =(int) (Utils.scheight_u /5.5);

        holder.ivPic.getLayoutParams().width = Utils.scwidth_u / 2;

        Glide.with(context).load(frmimgs[position])
                .placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
                .into(holder.ivPic);


    }

    public class CardViewHolder3 extends RecyclerView.ViewHolder {
        public ImageView ivPic;

        public CardViewHolder3(View itemView) {
            super(itemView);
            ivPic = (ImageView) itemView.findViewById(R.id.ivPic);
        }
    }

}

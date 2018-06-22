package com.pinky.romanticphotoframes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pinky.romanticphotoframes.R;

import java.util.List;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.CardViewHolder> {


    public static final int add = 2;
    private Context context;
    int height, width;
    List<String> frame_ImageObject;


    public ColorsAdapter(Context context, List<String> frame_ImageObject, int width, int height) {
        this.context = context;
        this.frame_ImageObject = frame_ImageObject;
        this.height = height;
        this.width = width;

    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.circleimageview, parent, false);


        return new CardViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        String color = frame_ImageObject.get(position);


        holder.circleImg.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY);


    }

    @Override
    public int getItemCount() {
        return frame_ImageObject.size();
    }


    public class CardViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout whitespace_rel;
        public Button circleImg;

        public CardViewHolder(View itemView) {
            super(itemView);

            circleImg = (Button) itemView.findViewById(R.id.circleImg);
            circleImg.getLayoutParams().width = width / 7;
            circleImg.getLayoutParams().height = width / 7;
        }
    }

}

package com.pinky.romanticphotoframes.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinky.romanticphotoframes.R;

public class FontsDisAdapter extends RecyclerView.Adapter<FontsDisAdapter.ImgViewHolder> {
    String[] typeFaceString;
    Context context;


    int  selctpos=0;

    public FontsDisAdapter(Context context, String[] bitmaps ) {
        // TODO Auto-generated constructor stub
        this.typeFaceString = bitmaps;
        this.context = context;

    }

    public void setColorPos(int position) {
        selctpos=position;

    }

    public class ImgViewHolder extends RecyclerView.ViewHolder {

        TextView fontstyle;

        public ImgViewHolder(View itemView) {
            super(itemView);
            fontstyle = (TextView) itemView.findViewById(R.id.fontstyle);

            // TODO Auto-generated constructor stub
        }

    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return typeFaceString.length;
    }


    @Override
    public ImgViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        View view = LayoutInflater.from(context).inflate(R.layout.fonstylelayout, parent, false);
        // TODO Auto-generated method stub
        return new ImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImgViewHolder holder, int position) {
        // TODO Auto-generated method stub

        if (selctpos==position)
        {
            holder. fontstyle.setTextColor(context.getResources().getColor(R.color.fontclickclr));
        }
        else
            holder. fontstyle.setTextColor(context.getResources().getColor(R.color.white));

        holder. fontstyle.setText(context.getResources().getString(R.string.pakklocltelugu));
        holder.fontstyle.setTypeface(Typeface.createFromAsset(context.getAssets(),
                typeFaceString[position]));


    }
}

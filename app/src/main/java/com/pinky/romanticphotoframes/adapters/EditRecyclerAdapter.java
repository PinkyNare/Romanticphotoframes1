package com.pinky.romanticphotoframes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pinky.romanticphotoframes.R;

public class EditRecyclerAdapter extends RecyclerView.Adapter<EditRecyclerAdapter.ViewHolder> {

    private Context mContext;
    int ImageId[];
    int dwidth, dheight;
    LayoutInflater lv;
    ViewHolder holder;

    boolean effectsClick;
    int[]  frame_ImageObject;
    public EditRecyclerAdapter(Context context, int ImageId[], int[]  frame_ImageObject, int dwidth,
                               int dheight, boolean effectsClick) {
        mContext = context;
        this.ImageId = ImageId;
        this.dwidth = dwidth;
        this.dheight = dheight;
        this.effectsClick = effectsClick;
        this.frame_ImageObject = frame_ImageObject;
        lv = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		loader_object=new ImageLoader_Object(mContext);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            // TODO Auto-generated constructor stub
            imageView = (ImageView) itemView
                    .findViewById(R.id.frame_img_adapter);


        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        int size = -1;
        if (effectsClick) {
            size = ImageId.length;
        } else
            size = frame_ImageObject.length;

        return size;
    }

    @Override
    public void onBindViewHolder(ViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub

        arg0.imageView.setBackgroundColor(Color.TRANSPARENT);

        if (effectsClick) {

            arg0.imageView.getLayoutParams().height = (int) (dwidth / 4.5);
            arg0.imageView.getLayoutParams().width = (int) (dwidth / 4.5);

            arg0.imageView.setPadding(0, 0, 0, 0);
            Glide.with(mContext).load(ImageId[arg1])
                    .placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
                    .into(arg0.imageView);
        } else {

            arg0.imageView.getLayoutParams().height = (int) (dheight /4.5);
            arg0.imageView.getLayoutParams().width = (int) (dheight /3);





            arg0.imageView.setPadding(0, 0, 0, 0);
            Glide.with(mContext).load(frame_ImageObject[arg1])
                    .placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
                    .into(arg0.imageView);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // TODO Auto-generated method stub
        View convertView = lv.inflate(R.layout.edit_adptrimg, arg0,
                false);


        return new ViewHolder(convertView);
    }

}

package com.pinky.romanticphotoframes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pinky.romanticphotoframes.R;

import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.CardViewHolder> {
	private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
	ArrayList<String> filePaths;
	Context context;


	public AlbumsAdapter(Context context, ArrayList<String> filepaths,
                         int height, int width) {

		this.filePaths = filepaths;
		this.context = context;

	}

	
	@Override
	public int getItemCount() {

		return filePaths.size();
	}

	@Override
	public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View rowView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.row_cardviewfils, parent, false);

		return new CardViewHolder(rowView);
	}

	public void onBindViewHolder(CardViewHolder holder, int position) {

//		width/3
		holder.ivPic.getLayoutParams().height = (int) ((context.getResources()
				.getDisplayMetrics().widthPixels) / 3);

		holder.ivPic.getLayoutParams().width = (int) ((context.getResources()
				.getDisplayMetrics().widthPixels) / 2.1);


		Glide.with(context).load(filePaths.get(position))
				.placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
				.into(holder.ivPic);



	}


	public class CardViewHolder extends RecyclerView.ViewHolder {
		public ImageView ivPic;

		public CardViewHolder(View itemView) {
			super(itemView);
			ivPic = (ImageView) itemView.findViewById(R.id.ivPic);
		}

	}


}

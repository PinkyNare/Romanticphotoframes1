package com.pinky.romanticphotoframes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.activities.ShowScrenActivity;
import com.pinky.romanticphotoframes.adapters.AlbumsAdapter;
import com.pinky.romanticphotoframes.touchusages.RecyclerTouchListener;
import com.pinky.romanticphotoframes.utils.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 2117 on 4/17/2018.
 */
public class MyCreationsFragment extends Fragment {

    static RecyclerView recyvw_all;
    static ArrayList<String> filepaths = new ArrayList<String>();
    File file;
    static ImageView noimagetext;
    static AlbumsAdapter mAdapter;
    static File fileSendToFinal;
    static String filePath;
    String[] fileNames;
    static int pos;


    public MyCreationsFragment() {
    }


    public static MyCreationsFragment newInstance() {
        MyCreationsFragment fragment = new MyCreationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_fram_lay, container, false);
        noimagetext = (ImageView) view.findViewById(R.id.image_no);
        noimagetext.getLayoutParams().width = Utils.scwidth_u / 2;
        noimagetext.getLayoutParams().height = Utils.scwidth_u / 2;
        recyvw_all = (RecyclerView) view.findViewById(R.id.recyvw_all);
        loadFiles();
        if (filepaths.size() >= 1) {
            recyvw_all.setVisibility(View.VISIBLE);
            noimagetext.setVisibility(View.GONE);
        } else {
            recyvw_all.setVisibility(View.GONE);
            noimagetext.setVisibility(View.VISIBLE);
        }
        recyvw_all.setHasFixedSize(true);
        recyvw_all.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mAdapter = new AlbumsAdapter(getActivity().getApplicationContext(), filepaths, Utils.scheight_u, Utils.scwidth_u);

        recyvw_all.setAdapter(mAdapter);

        recyvw_all.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyvw_all, new RecyclerTouchListener.RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                fileSendToFinal = new File(filepaths.get(position));
                filePath = filepaths.get(position);
                Intent intent = new Intent(getActivity(), ShowScrenActivity.class);
                pos = position;
                intent.putExtra("imgsavechk", "yes");
                intent.putExtra("final_image_path", filePath);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;
    }


    public void loadFiles() {
        filepaths.clear();
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name));

        if (file.isDirectory()) {
            if (file.list()!=null) {
                fileNames = file.list();
                for (int i = 0; i < fileNames.length; i++) {
                    if (fileNames[i].endsWith(".jpg"))
                        filepaths.add(file.toString() + "/" + fileNames[i]);
                }
            }
        }

    }

    public static void update(String path, boolean bb) {

        if (bb) {
            filepaths.remove(path);
        } else {
            filepaths.add(path);
        }
        try {
            mAdapter.notifyDataSetChanged();
            if (filepaths.size() >= 1) {
                recyvw_all.setVisibility(View.VISIBLE);

                noimagetext.setVisibility(View.GONE);
            } else {
                recyvw_all.setVisibility(View.GONE);
                noimagetext.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }

    }


}

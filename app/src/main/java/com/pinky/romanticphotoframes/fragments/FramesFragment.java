package com.pinky.romanticphotoframes.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.activities.EditingActivity;
import com.pinky.romanticphotoframes.adapters.FramesEfectsAdapter;
import com.pinky.romanticphotoframes.crop.CropImageActivity;
import com.pinky.romanticphotoframes.crop.CropUtil;
import com.pinky.romanticphotoframes.touchusages.RecyclerTouchListener;
import com.pinky.romanticphotoframes.utils.Utils;

import java.io.File;

import static android.view.Gravity.CENTER;

/**
 * Created by 2117 on 4/17/2018.
 */
public class FramesFragment extends Fragment {

    FramesEfectsAdapter mAdapter;
    RecyclerView recyvw_all;

    ///////////////////////

    ////////////////////
    public FramesFragment() {
    }


    public static FramesFragment newInstance() {
        FramesFragment fragment = new FramesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_fram_lay, container, false);

        recyvw_all = (RecyclerView) view.findViewById(R.id.recyvw_all);
        recyvw_all.setHasFixedSize(true);
        recyvw_all.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new FramesEfectsAdapter(getActivity(), Utils.frams_alldata);
        recyvw_all.setAdapter(mAdapter);

        recyvw_all.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyvw_all, new RecyclerTouchListener.RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {

                try {

                    Utils.pos_pass = position;
                    Intent i = new Intent(getActivity(), EditingActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("editmainchk", 2);
                    startActivity(i);

                } catch (ClassCastException e) {

                } catch (Exception e) {

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;
    }


}

package com.pinky.romanticphotoframes.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.adapters.MoreAppAdapter;
import com.pinky.romanticphotoframes.touchusages.RecyclerTouchListener;
import com.pinky.romanticphotoframes.utils.Utils;

/**
 * Created by 2117 on 4/17/2018.
 */
public class MoreAppsFragment extends Fragment
{

    MoreAppAdapter mAdapter;
    RecyclerView recyvw_all;

    public MoreAppsFragment() {
    }


    public static MoreAppsFragment newInstance() {
        MoreAppsFragment fragment = new MoreAppsFragment();
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
        mAdapter = new MoreAppAdapter(getActivity(), Utils.updateAppsList );
        recyvw_all.setAdapter(mAdapter);

        recyvw_all.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyvw_all, new RecyclerTouchListener.RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri
                        .parse(Utils.updateAppsList.get(position)
                                .getAppUrl())), 121);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        return view;
    }


}

package com.pinky.romanticphotoframes.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaouan.revealator.Revealator;
import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.activities.EditingActivity;
import com.pinky.romanticphotoframes.adapters.ColorsAdapter;
import com.pinky.romanticphotoframes.colorpicker.ColorPicker;
import com.pinky.romanticphotoframes.touchusages.RecyclerTouchListener;
import com.pinky.romanticphotoframes.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.codetail.widget.RevealFrameLayout;

/**
 * Created by 2117 on 4/19/2018.
 */
public class TextColorFragment extends Fragment {
    String colorcodes[] = {"#000000", "#ffc0cb", "#008080", "#ffe4e1", "#ff0000", "#ffd700", "#00ffff", "#40e0d0",
            "#ff7373", "#d3ffce", "#e6e6fa", "#0000ff", "#f0f8ff", "#ffa500", "#b0e0e6", "#7fffd4", "#eeeeee", "#cccccc", "#800080", "#333333", "#faebd7", "#fa8072", "#c6e2ff", "#00ff00"
            , "#ffb6c1", "#003366", "#c0c0c0", "#ffff00"};
    RecyclerView recyvw_txt;
    List<String> frame_ImageObject;
    ColorsAdapter frames_adapter;
    public static FloatingActionButton fab_btn;
    RelativeLayout rel_add_clrview;
    TextView cancelbtn, okbtn;
    public static RelativeLayout reveal_layout;

    ColorPicker colorPickerView;


    public TextColorFragment() {
    }


    public static TextColorFragment newInstance() {
        TextColorFragment fragment = new TextColorFragment();
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
        View view = inflater.inflate(R.layout.textrecycl, container, false);

        reveal_layout = (RelativeLayout) view.findViewById(R.id.reveal_layout);

        fab_btn = (FloatingActionButton) view.findViewById(R.id.fab_btn );
        RevealFrameLayout revelfram = (RevealFrameLayout) view.findViewById(R.id.revelfram);
        fab_btn.setVisibility(View.VISIBLE);
        revelfram.setVisibility(View.VISIBLE);

        recyvw_txt = (RecyclerView) view.findViewById(R.id.recyvw_txt);

        rel_add_clrview = (RelativeLayout) view.findViewById(R.id.rel_add_clrview);

        cancelbtn = (TextView) view.findViewById(R.id.cancelbtn);
        okbtn = (TextView) view.findViewById(R.id.okbtn);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.scwidth_u, dpToPx(220));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        layoutParams.add

        rel_add_clrview.setLayoutParams(new RelativeLayout.LayoutParams(Utils.scwidth_u, dpToPx(220)));
        colorPickerView = new ColorPicker(getActivity().getApplicationContext());
        rel_add_clrview.addView(colorPickerView,layoutParams);


        recyvw_txt.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);


        getFrameObjectList();

        recyvw_txt.setLayoutManager(gridLayoutManager);
        recyvw_txt.setItemViewCacheSize(frame_ImageObject.size());
        frames_adapter = new ColorsAdapter(getActivity().getApplicationContext(), frame_ImageObject, Utils.scwidth_u, Utils.scheight_u);
        recyvw_txt.setAdapter(frames_adapter);
        recyvw_txt.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyvw_txt, new RecyclerTouchListener.RecyclerClick_Listener() {

            @Override
            public void onLongClick(View view, int position) {

            }

            @Override
            public void onClick(View view, int position) {

                try {

                    Utils.colorr = Color.parseColor(frame_ImageObject.get(position));
                    EditingActivity.messageEditText.setTextColor(Utils.colorr);

//                    Toast.makeText(getActivity().getApplicationContext(), " selected color " + (String) frame_ImageObject.get(position), Toast.LENGTH_SHORT).show();

                } catch (ClassCastException e) {

                } catch (Exception e) {

                }
            }
        }));


        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Revealator.reveal(reveal_layout)
                        .from(fab_btn)
                        .withCurvedTranslation()
                        .withChildsAnimation()
                        .start();
                fab_btn.setVisibility(View.GONE);


                colorPickerView.setColor(Utils.colorr);


            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fab_btn.setVisibility(View.VISIBLE);
                Revealator.unreveal(reveal_layout)
                        .to(fab_btn)
                        .withCurvedTranslation()
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                            }
                        })
                        .start();
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fab_btn.setVisibility(View.VISIBLE);
                Revealator.unreveal(reveal_layout)
                        .to(fab_btn)
                        .withCurvedTranslation()
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                            }
                        })
                        .start();

                int selectedColor = colorPickerView.getColor();
                Utils.colorr = selectedColor;
                EditingActivity.messageEditText.setTextColor(selectedColor);

            }
        });


        return view;
    }


    public void getFrameObjectList() {
        frame_ImageObject = new ArrayList<String>();
        for (int k = 0; k < colorcodes.length; k++) {
            frame_ImageObject.add(colorcodes[k]);

        }
    }

    public   int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

  /*  public void openColorDialog(View v) {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getActivity().getApplicationContext(),
                Utils.colorr, new ColorPickerDialog.OnColorSelectedListener() {
            public void onColorSelected(int color) {
//                colorr = color;
                Utils.colorr = color;
//                userText.setTextColor(color);
                EditingActivity.messageEditText.setTextColor(Utils.colorr);
            }

            public void onColorSelected(int color, Boolean selected) {
                Utils.colorr = color;
//                userText.setTextColor(color);
                EditingActivity.messageEditText.setTextColor(Utils.colorr);

            }
        });
        colorPickerDialog.show();
    }*/
}



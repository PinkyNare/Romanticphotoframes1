package com.pinky.romanticphotoframes.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.activities.EditingActivity;
import com.pinky.romanticphotoframes.adapters.FontsDisAdapter;
import com.pinky.romanticphotoframes.touchusages.RecyclerTouchListener;

/**
 * Created by 2117 on 4/19/2018.
 */
public class TextFontsFragment extends Fragment {



   /* "fonts/SreeKrushnadevaraya_Regular.otf",  "fonts/Mandali_Regular.otf",
 "fonts/Ramabhadra_Italic.otf","fonts/Mallanna.otf",
 "fonts/Purushothamaa.otf",
"fonts/RaviPrakash.ttf",   "fonts/Chathura_Bold.ttf",
   */

    int preTypeFaceSelect = 0;
    String typeFaceString[] = {

            "fonts/Gurajada_Regular.otf", "fonts/Gidugu.otf", "fonts/NTR_Regular.otf",
            "fonts/Suravaram.otf","fonts/NATS.otf",

             "fonts/Architex.ttf", "fonts/Aspire-DemiBold.ttf", "fonts/AvenirLTStdRoman.otf",
            "fonts/BendyStraw.ttf", "fonts/Bradley Gratis.ttf", "fonts/comic_andy.ttf", "fonts/DelicateSansBold.ttf",
            "fonts/Eutemia.ttf", "fonts/FatCow-Italic.otf", "fonts/Flatstock.ttf", "fonts/GelPenHeavy.ttf",
            "fonts/HOMOARAK.TTF", "fonts/James Almacen.ttf",
            "fonts/JeanSunHo.ttf", "fonts/KODYZ.ttf", "fonts/LemonCookieSans.ttf",
            "fonts/LemonCookieSansBold.ttf",   "fonts/maiden.TTF",

            "fonts/NegativeSpace.ttf", "fonts/PajamaPantsLight.ttf", "fonts/peach-sundress.ttf", "fonts/Peterbuilt.ttf",
             "fonts/Quikhand.ttf", "fonts/Scrappy-looking_demo.ttf", "fonts/TeamSpiritNF.otf",
            "fonts/UndergroundNF.otf",
    };
    FontsDisAdapter fonts_Adapter;
    RecyclerView fonts_recyclvw;

    public TextFontsFragment() {
    }


    public static TextFontsFragment newInstance() {
        TextFontsFragment fragment = new TextFontsFragment();
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
        fonts_recyclvw = (RecyclerView) view.findViewById(R.id.recyvw_txt);

        fonts_Adapter = new FontsDisAdapter(getActivity().getApplicationContext(), typeFaceString);
        fonts_recyclvw.setAdapter(fonts_Adapter);
        fonts_recyclvw.setHasFixedSize(true);
        fonts_recyclvw.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));




        fonts_recyclvw.addOnItemTouchListener(new RecyclerTouchListener(
                getActivity().getApplicationContext(), fonts_recyclvw,
                new RecyclerTouchListener.RecyclerClick_Listener() {

                    @Override
                    public void onLongClick(View view, int position) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onClick(View view, int position) {
                        // TODO Auto-generated method stub
                        EditingActivity.messageEditText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                                typeFaceString[position]), Typeface.NORMAL);
                        preTypeFaceSelect = position;
                        fonts_Adapter.setColorPos(position);
                        fonts_Adapter.notifyDataSetChanged();



                    }
                }));


        return view;
    }
}

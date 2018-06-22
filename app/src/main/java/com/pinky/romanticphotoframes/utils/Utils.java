package com.pinky.romanticphotoframes.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;

import com.pinky.romanticphotoframes.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 2117 on 4/17/2018.
 */
public class Utils {


    public static boolean isCameraImage = false;
    public static int scwidth_u, scheight_u;

    public static Bitmap bmp_img1,bmp_img2;
//    public static Bitmap galbmp_img1,galbmp_img2;

//    public static Bitmap firstImageSelected;
//    public static Bitmap gallbmp;


    public static int[] frams_alldata = {R.drawable.frame1,
            R.drawable.frame2, R.drawable.frame3, R.drawable.frame4,
            R.drawable.frame5, R.drawable.frame6, R.drawable.frame7,
            R.drawable.frame8, R.drawable.frame9, R.drawable.frame10, R.drawable.frame11,
            R.drawable.frame12, R.drawable.frame13, R.drawable.frame14,
            R.drawable.frame15, R.drawable.frame16, R.drawable.frame17,
            R.drawable.frame18,
            R.drawable.frame19,
            R.drawable.frame20
    };

    public static ArrayList<String> mobile_AppInstalled;

    public static ArrayList<App> updateAppsList = new ArrayList<>();

    public static ArrayList<App> lastAppList = new ArrayList<>();
    public static int pos_pass;


    public static ArrayList<String> shareAppNameList = new ArrayList<>
            (Arrays.asList("WhatsApp", "Facebook", "Gmail", "Pinterest", "Instagram", "LinkedIn", "SHAREit", "Skype", "Hike", "Twitter", "More"));
    public static ArrayList<String> shareAppColorList = new ArrayList<>
            (Arrays.asList("#20b384", "#5d84c2", "#e84c5f", "#e72638", "#7b482c", "#47c3ee", "#2b9bff", "#00aff0", "#00acff", "#1da1f2", "#a9a8a8"));
    public static int colorr = Color.BLACK;
    public static boolean isinternet;


    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

//   public static  boolean isinternet=ConnectivityReceiver.isConnected();

}

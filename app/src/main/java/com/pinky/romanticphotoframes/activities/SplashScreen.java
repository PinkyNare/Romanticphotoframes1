package com.pinky.romanticphotoframes.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.utils.ConnectivityReceiver;
import com.pinky.romanticphotoframes.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity {

    public static InterstitialAd interstitialAd_entry;

    Timer timer;
    TimerTask timerTask;
    final Handler handler2 = new Handler();
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.splash);

            DisplayMetrics size = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(size);
            Utils.scwidth_u = size.widthPixels;
            Utils.scheight_u = size.heightPixels;

            Utils.isinternet = ConnectivityReceiver.isConnected();

            if (Utils.isinternet) {
                interstitialAd_entry = new InterstitialAd(SplashScreen.this);
                interstitialAd_entry.setAdUnitId(getResources().getString(R.string.Interstitial_Ad_id_entry));
                requestNewInterstitial();
                startTimer();
                interstitialAd_entry.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // TODO Auto-generated method stub
                        super.onAdLoaded();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.e(" Ad Failed ", "  ========   Splashscreen  ===========   " + i);
//                        0 for no ids in app store
//                        1 means our code wrong
//                        3 means it take some time
                    }

                    public void onAdClosed() {
                        Log.e(" Ad loading ", "  ========   Splashscreen  ===========   ");
                        interstitialAd_entry = null;
//                        addshow=true;
                        startintent();
                    }

                    ;
                });


            } else {
                new Handler().postDelayed(new Runnable() {
                    // Using handler with postDelayed called runnable run method
                    @Override
                    public void run() {
                        startintent();
                    }
                }, 2 * 1000);

            }
        } catch (Exception e) {

        }


    }

    private void requestNewInterstitial() {

        try {
            AdRequest adRequest1 = new AdRequest.Builder().build();
            interstitialAd_entry.loadAd(adRequest1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 0, 500);

    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {
                count++;
                handler2.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (interstitialAd_entry.isLoaded()) {
                            interstitialAd_entry.show();
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                                timerTask.cancel();
                            }

                        } else {
                            if (count > 14) {
                                Log.e("splash", "hello world else if" + count);
                                if (timer != null) {
                                    timer.cancel();
                                    timer = null;
                                    timerTask.cancel();
                                }
                                startintent();
                            } else {
                                Log.e("splash", "hello world else else" + count);
                            }
                        }
                    }

                });


            }
        };
    }

    private void startintent() {
        Intent i = new Intent(SplashScreen.this,
                MainActivity.class);
        startActivity(i);


        finish();
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }


}

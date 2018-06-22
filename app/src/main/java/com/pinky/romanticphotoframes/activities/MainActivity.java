package com.pinky.romanticphotoframes.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.pinky.romanticphotoframes.Myapplication;
import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.fragments.FramesFragment;
import com.pinky.romanticphotoframes.fragments.MoreAppsFragment;
import com.pinky.romanticphotoframes.fragments.MyCreationsFragment;
import com.pinky.romanticphotoframes.fragments.ViewPagerAdapter;
import com.pinky.romanticphotoframes.utils.App;
import com.pinky.romanticphotoframes.utils.ConnectivityReceiver;
import com.pinky.romanticphotoframes.utils.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    Toolbar toolbar_strt;
    ViewPager mviewpager;
    TabLayout tabsdatshow;
    ImageView imgvwswclps;
    static Context context;

    // Parse Data
    static String exitXml = "http://pakkalocalapps.com/backpf.xml";
    static String finalScreenApps = "http://pakkalocalapps.com/latestapps.xml";
    static String updatedAppsXml = "http://pakkalocalapps.com/moreapps.xml";
    int xmlCount;
    static List<App> backApps = new ArrayList<App>();
    ///////////////////////////
//    exit Layout purpose
    public static Animation animation;
    Dialog customDialog;
    public static  String packagename;
    private int dialogWidth;
    private int dialogHeight;
    ///////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packagename = getApplicationContext().getPackageName();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Utils.scheight_u = displaymetrics.heightPixels;
        Utils.scwidth_u = displaymetrics.widthPixels;
        Utils.isinternet = ConnectivityReceiver.isConnected();
        animation = new ScaleAnimation(1.0F, 0.9F, 1.0F, 0.9F,
                Animation.RELATIVE_TO_SELF, (float) 0.5,
                Animation.RELATIVE_TO_SELF, (float) 0.5);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            getPermissions();

        toolbar_strt = (Toolbar) findViewById(R.id.toolbar_strt);
        setSupportActionBar(toolbar_strt);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar_strt.setTitleTextColor(Color.WHITE);
        toolbar_strt.getOverflowIcon()
                .setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        context = MainActivity.this;

        Utils.mobile_AppInstalled = getInstalledApps();

        mviewpager = (ViewPager) findViewById(R.id.mviewpager);
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(FramesFragment.newInstance(), "FRAMES");
        mViewPagerAdapter.addFragment(MyCreationsFragment.newInstance(), "MY CREATIONS");
        mViewPagerAdapter.addFragment(MoreAppsFragment.newInstance(), "MORE APPS [AD]");
        mviewpager.setAdapter(mViewPagerAdapter);

        tabsdatshow = (TabLayout) findViewById(R.id.tabsdatshow);
        tabsdatshow.setupWithViewPager(mviewpager);

        imgvwswclps = (ImageView) findViewById(R.id.imgvwswclps);
        imgvwswclps.setImageResource(R.drawable.usedbg);


        if (Utils.isinternet) {
            backApps = new ArrayList<App>();
            Utils.lastAppList = new ArrayList<>();
            Utils.updateAppsList = new ArrayList<>();
            xmlParsingUsingVolley(exitXml);
            displayAd();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Drawable overflowIcon = toolbar_strt.getOverflowIcon();
        if (overflowIcon != null) {
            Drawable newIcon = overflowIcon.mutate();
            newIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            toolbar_strt.setOverflowIcon(newIcon);
        }
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_rate, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the navigation drawer is open, hide action items related to the
        // content
        // view
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            CharSequence menuTitle = menuItem.getTitle();
            SpannableString styledMenuTitle = new SpannableString(menuTitle);
            styledMenuTitle.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                    menuTitle.length(), 0);
            menuItem.setTitle(styledMenuTitle);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_rate_us) {
            if (Utils.isinternet) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName())));
            } else {
                no_Internet_Dialouge();
            }
            return true;
        }
        if (id == R.id.action_more) {
            if (Utils.isinternet) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Pakka+Local")));
            } else {
                no_Internet_Dialouge();
            }
            return true;
        }
        if (id == R.id.action_privacy) {
            if (Utils.isinternet) {
                startActivity(new Intent(MainActivity.this, WebShowActivity.class));
            } else {
                no_Internet_Dialouge();
            }
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void no_Internet_Dialouge() {

        AlertDialog.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder = new AlertDialog.Builder(MainActivity.this,
                    android.R.style.Theme_Material_Dialog_Alert);
        } else {
            mBuilder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_MyDialog);
        }

        mBuilder.setMessage("Sorry No Internet Connection please try again later");
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mBuilder.create();
        mBuilder.show();

    }

    private void getPermissions() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                MainActivity.this.finish();
            }


        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }


    @Override
    protected void onResume() {
        try {
            Myapplication.getInstance().setConnectivityListener(this);
        } catch (Exception e) {

        }

        if (Utils.isinternet)
            displayAd();
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (Utils.isinternet) {
            try {
                if (backApps != null && backApps.size() >= 1) {
                    exit_Click();
                } else
                    finish();
            } catch (Exception e) {
                MainActivity.this.finish();
            }
        } else {
            MainActivity.this.finish();

        }

    }


    private void exit_Click() {

        customDialog = new Dialog(MainActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.exit_layout);
        int screnwidth = getResources().getDisplayMetrics().widthPixels;
        int scrnheight = getResources().getDisplayMetrics().heightPixels;
        dialogWidth = (screnwidth - (screnwidth / 10));
        dialogHeight = (scrnheight - scrnheight / 24);


        LinearLayout exitlinear = (LinearLayout) customDialog
                .findViewById(R.id.exitlinear);
        exitlinear.getLayoutParams().height = (int) (dialogHeight / 1.3);
        LinearLayout linear44 = (LinearLayout) customDialog
                .findViewById(R.id.linear442);
        linear44.getLayoutParams().width = dialogWidth;
        linear44.getLayoutParams().height = dialogHeight / 10
                + (int) ((dialogHeight / 12) * 4.5);

        LinearLayout linear2 = (LinearLayout) customDialog
                .findViewById(R.id.linear22);
        linear2.getLayoutParams().width = dialogWidth;
        linear2.getLayoutParams().height = (dialogHeight / 12) * 6;

        RelativeLayout real1 = (RelativeLayout) customDialog
                .findViewById(R.id.real12);
        real1.getLayoutParams().width = dialogWidth;
        real1.getLayoutParams().height = (int) ((dialogHeight / 15) * 1.8);

        RelativeLayout real2 = (RelativeLayout) customDialog
                .findViewById(R.id.real22);
        real2.getLayoutParams().width = dialogWidth;
        real2.getLayoutParams().height = (int) ((dialogHeight / 15) * 1.5);

        View view = (View) customDialog.findViewById(R.id.view2);
        view.getLayoutParams().width = dialogWidth;
        view.getLayoutParams().height = (dialogHeight / 11);

        ImageView firstImage = (ImageView) customDialog
                .findViewById(R.id.firstImage);
        firstImage.getLayoutParams().width = dialogWidth / 3 - 20;
        firstImage.getLayoutParams().height = dialogWidth / 3 - 20;
        ImageView secondImage = (ImageView) customDialog
                .findViewById(R.id.secondImage);
        secondImage.getLayoutParams().width = dialogWidth / 3 - 20;
        secondImage.getLayoutParams().height = dialogWidth / 3 - 20;
        ImageView thirdImage = (ImageView) customDialog
                .findViewById(R.id.thirdImage);
        thirdImage.getLayoutParams().width = dialogWidth / 3 - 20;
        thirdImage.getLayoutParams().height = dialogWidth / 3 - 20;
        ImageView fourthImage = (ImageView) customDialog
                .findViewById(R.id.fourthImage);
        fourthImage.getLayoutParams().width = dialogWidth / 3 - 20;
        fourthImage.getLayoutParams().height = dialogWidth / 3 - 20;

        Button exitImage = (Button) customDialog.findViewById(R.id.exitImage);

        exitImage.getLayoutParams().width = (dialogWidth / 4);
        exitImage.getLayoutParams().height = dialogWidth / 6;

        Button cancelImage = (Button) customDialog
                .findViewById(R.id.cancelImage);

        cancelImage.getLayoutParams().width = (dialogWidth / 4);
        cancelImage.getLayoutParams().height = dialogWidth / 6;
        firstImage.startAnimation(animation);
        secondImage.startAnimation(animation);
        thirdImage.startAnimation(animation);
        fourthImage.startAnimation(animation);

        TextView firstText = (TextView) customDialog
                .findViewById(R.id.firstText);
        TextView secondText = (TextView) customDialog
                .findViewById(R.id.secondText);
        TextView thirdText = (TextView) customDialog
                .findViewById(R.id.thirdText);
        TextView fourthText = (TextView) customDialog
                .findViewById(R.id.fourthText);

        if (backApps != null && backApps.size() >= 4) {
            firstText.setText(backApps.get(0).getAppName());
            secondText.setText(backApps.get(1).getAppName());
            thirdText.setText(backApps.get(2).getAppName());
            fourthText.setText(backApps.get(3).getAppName());

        }

        if (backApps != null && backApps.size() >= 4) {

            Glide.with(getApplicationContext()).load(backApps.get(0).getImgUrl().toString())
                    .placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
                    .into(firstImage);
            Glide.with(getApplicationContext()).load(backApps.get(1).getImgUrl().toString())
                    .placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
                    .into(secondImage);
            Glide.with(getApplicationContext()).load(backApps.get(2).getImgUrl().toString())
                    .placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
                    .into(thirdImage);
            Glide.with(getApplicationContext()).load(backApps.get(3).getImgUrl().toString())
                    .placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
                    .into(fourthImage);


            firstImage.startAnimation(animation);
            secondImage.startAnimation(animation);
            thirdImage.startAnimation(animation);
            fourthImage.startAnimation(animation);

        }
        if (Utils.isinternet && backApps != null && backApps.size() >= 4) {
            firstImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (backApps != null && backApps.size() >= 1)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse(backApps.get(0).getAppUrl())));

                }
            });

            secondImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (backApps != null && backApps.size() >= 2)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse(backApps.get(1).getAppUrl())));

                }
            });

            thirdImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (backApps != null && backApps.size() >= 3)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse(backApps.get(2).getAppUrl())));

                }
            });

            fourthImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (backApps != null && backApps.size() >= 4)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse(backApps.get(3).getAppUrl())));

                }
            });

        }

        cancelImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                customDialog.dismiss();
            }
        });
        exitImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                MainActivity.this.finish();
            }
        });

        customDialog.setCancelable(false);
        customDialog.show();

    }


    public void xmlParsingUsingVolley(String url) {

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            DocumentBuilderFactory dbf =
                                    DocumentBuilderFactory.newInstance();
                            DocumentBuilder db = dbf.newDocumentBuilder();
                            InputSource is = new InputSource();
                            is.setCharacterStream(new StringReader(response));

                            Document doc = db.parse(is);
                            NodeList nodes = doc.getElementsByTagName("app");

                            // iterate the employees
                            ArrayList<App> arrayList = new ArrayList<>();
                            arrayList.clear();
                            for (int i = 0; i < nodes.getLength(); i++) {
                                Element eElement = (Element) nodes.item(i);
                                // Get the title
                                App appBean = new App();
                                appBean.setAppName(getNode("name", eElement));
                                appBean.setAppUrl(getNode("url", eElement));
                                appBean.setImgUrl(getNode("image", eElement));
                                arrayList.add(appBean);
                            }
                            switch (xmlCount) {
                                case 0:
                                    backApps.clear();
                                    backApps.addAll(arrayList);
                                    xmlCount++;
                                    xmlParsingUsingVolley(updatedAppsXml);
                                    break;
                                case 1:
                                    Utils.updateAppsList.clear();
                                    Utils.updateAppsList.addAll(loadUndowloadedApp(arrayList));
                                    xmlCount++;
                                    xmlParsingUsingVolley(finalScreenApps);
                                    break;
                                case 2:
                                    Utils.lastAppList.clear();
                                    Utils.lastAppList.addAll(loadUndowloadedApp(arrayList));
                                    break;

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle error response
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.e("Error", " Volley Paersing Error " + error.getMessage());
                        }
                    }
                }
        );
        Volley.newRequestQueue(MainActivity.this).add(req);
    }


    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }


    public static ArrayList<String> getInstalledApps() {
        ArrayList<String> packList = new ArrayList<String>();
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String packgname = p.packageName;
                packList.add(packgname);
            }
        }
        packList.add(packagename);
        return packList;
    }

    public static ArrayList<App> loadUndowloadedApp(ArrayList<App> appList) {
        ArrayList<App> dummyList = new ArrayList<App>();
        if (appList.size() > 0) {


            for (int i = 0; i < appList.size(); i++) {
                String appname = appList.get(i).getAppUrl();
                String[] names = appname.split("=");

                appname = names[1];

                if (!Utils.mobile_AppInstalled.contains(appname)) {
                    dummyList.add(appList.get(i));

                }
            }

        }
        return dummyList;
    }

    private static boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    private void displayAd() {
        try {
            LinearLayout banner_lay = (LinearLayout) findViewById(R.id.banner_lay);
            banner_lay.setVisibility(View.VISIBLE);

            RelativeLayout bannerspace = (RelativeLayout) findViewById(R.id.bannerspace_main);
            ViewGroup.MarginLayoutParams mparams = (ViewGroup.MarginLayoutParams) bannerspace.getLayoutParams();
            mparams.bottomMargin = Utils.dpToPx(5, getApplicationContext());
            // Create an ad.
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getResources().getString(R.string.Banner_Ad_id));

            // until the ad is loaded.
            FrameLayout linearLayout = (FrameLayout) findViewById(R.id.banner_main);
            linearLayout.addView(adView);

            // Create an ad request. Check logcat output for the hashed device
            // ID to
            // get test ads on a physical device.
            AdRequest adRequest = new AdRequest.Builder().build();
            // Start loading the ad in the background.
            adView.loadAd(adRequest);

        } catch (Exception e) {
            Log.e("Info", "MainActivity Ad Display Error" + e.getMessage());
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.isinternet = isConnected;

        if (isConnected) {
            displayAd();
            if (backApps != null && backApps.size() >= 4 && Utils.lastAppList != null) {
            } else {
                xmlCount = 0;
                backApps = new ArrayList<App>();
                Utils.lastAppList = new ArrayList<>();
                Utils.updateAppsList = new ArrayList<>();
                xmlParsingUsingVolley(exitXml);

            }
        }

    }
}

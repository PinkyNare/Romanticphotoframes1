package com.pinky.romanticphotoframes.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.fragments.MyCreationsFragment;
import com.pinky.romanticphotoframes.touchusages.RecyclerTouchListener;
import com.pinky.romanticphotoframes.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 2117 on 4/17/2018.
 */
public class ShowScrenActivity extends AppCompatActivity {


    Toolbar toolbar_share;

    List<String> infoNamesList;
    RelativeLayout mainlayout;
    String filepath, imgsave_chk;
    Bitmap shareImage;
    ImageView userSendImage;
    Context context;
    ArrayList<AppBean> appBeanList = new ArrayList<AppBean>();
    List<String> appNames = new ArrayList<String>();
    List<Drawable> appIcons = new ArrayList<Drawable>();
    GridView share_grid;
    TextView share_to_apps_text;
    Button btn_exit, btn_cancel;
    TextView txt_title;
    Dialog dialog1;
    RecyclerView grid;

    Handler handler = new Handler();
    CacheAdapter gridAppAdapter;
    public int count = 0;
    TextView moreappstext;
    ImageView saveTogal;


    RelativeLayout save_lay, saveima_lay;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_share_lay);


        toolbar_share = (Toolbar) findViewById(R.id.toolbar_share);
        setSupportActionBar(toolbar_share);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_share.setTitle("EMI Calculator");
        toolbar_share.setTitleTextColor(Color.WHITE);
        toolbar_share.getOverflowIcon()
                .setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar_share.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowScrenActivity.this.finish();

            }
        });
        saveTogal = (ImageView) findViewById(R.id.saveTogal);
        saveTogal.setLayoutParams(new RelativeLayout.LayoutParams(Utils.scwidth_u / 5, Utils.scwidth_u / 5));

        if (Utils.isinternet) {
            displayAd();
        }

        mainlayout = (RelativeLayout) findViewById(R.id.image_layout);
        userSendImage = (ImageView) findViewById(R.id.saved_image);
        share_to_apps_text = (TextView) findViewById(R.id.share_to_apps_text);
        context = this;
        share_grid = (GridView) findViewById(R.id.share_grid);

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize = 3;

        imgsave_chk = getIntent().getExtras().getString("imgsavechk");

        filepath = getIntent().getExtras().getString("final_image_path");
        shareImage = BitmapFactory.decodeFile(filepath);


        if (shareImage != null)
            userSendImage.setImageBitmap(shareImage);

        save_lay = (RelativeLayout) findViewById(R.id.save_lay);

        saveima_lay = (RelativeLayout) findViewById(R.id.save_lay);


        if (imgsave_chk.equalsIgnoreCase("yes")) {
            save_lay.setVisibility(View.GONE);
        }
        saveima_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save_lay.setVisibility(View.GONE);

                saveData();
            }
        });


        grid = (RecyclerView) findViewById(R.id.appgrid);
        grid.setHasFixedSize(true);
        grid.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        moreappstext = (TextView) findViewById(R.id.moreappstext);

        if (Utils.isinternet) {
            moreappstext.setText("PakkaLocal Apps [AD]:-");
            grid.setVisibility(View.VISIBLE);
        }

        handler.postDelayed(runnable, 1000);
        if (Utils.lastAppList.size() < 1) {
            grid.setVisibility(View.GONE);
            moreappstext.setVisibility(View.GONE);
        }


//        height/2.3
        mainlayout.getLayoutParams().height = (int) (Utils.scheight_u / 2.5);
        userSendImage.getLayoutParams().width = (int) (Utils.scwidth_u  );
        if (shareImage != null)
            userSendImage.setImageBitmap(shareImage);


        for (int i = 0; i < Utils.shareAppNameList.size() - 1; i++) {
            appNames.add(Utils.shareAppNameList.get(i));
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        Uri uri = Uri.parse("android.resource://"
                + getApplicationContext().getPackageName()
                + "/drawable/nscrollimage1");

        sendIntent.setType("image/*");
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        List<ResolveInfo> resolveInfoList = getPackageManager()
                .queryIntentActivities(sendIntent, 0);
        for (int i = 0; i < appNames.size(); i++) {


            for (ResolveInfo resolveInfo : resolveInfoList) {
                String appname = resolveInfo.activityInfo
                        .loadLabel(getPackageManager()).toString()
                        .toLowerCase();

                if (appname.startsWith(appNames.get(i).toLowerCase())) {

                    AppBean bean = new AppBean();
                    bean.setAppName(""
                            + resolveInfo.activityInfo.loadLabel(
                            getPackageManager()).toString());
                    bean.setPackName(""
                            + resolveInfo.activityInfo.packageName.toString());
                    bean.setIcon(resolveInfo.activityInfo.loadIcon(context.getPackageManager()));
                    if (appBeanList.size() < 3) {
                        appBeanList.add(bean);
                    } else {
                        break;
                    }

                }
            }

        }
        AppBean bean2 = new AppBean();
        bean2.setAppName("More");
        Drawable allIcon = getResources().getDrawable(R.drawable.ic_apps_black_24dp);
        bean2.setIcon(allIcon);
        appBeanList.add(bean2);
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),
                appBeanList);
        share_grid.setAdapter(adapter);


        grid.addOnItemTouchListener(new RecyclerTouchListener(this,
                grid, new RecyclerTouchListener.RecyclerClick_Listener() {
            @Override
            public void onClick(View view, final int position) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.lastAppList.get(position).getAppUrl())));
                } catch (Exception e) {
                    Log.e("Error ", "Final Screen Catch error");
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isinternet) {
            displayAd();
        }
    }
    private final Runnable runnable = new Runnable() {
        public void run() {
            if (Utils.lastAppList != null && Utils.lastAppList.size() > 0) {
                if (Utils.isinternet) {
                    gridAppAdapter = new CacheAdapter();
                    grid.setAdapter(gridAppAdapter);
                    grid.setVisibility(View.VISIBLE);
                    if (Utils.lastAppList.size() < 1) {
                        grid.setVisibility(View.GONE);
                        moreappstext.setVisibility(View.GONE);
                    }

                    moreappstext.setText("More Apps [AD]:-");
                    handler.removeCallbacks(runnable);
                }
            } else {
                count = count + 1;
                if (count == 25) {
                    count = 0;
                    grid.setVisibility(View.GONE);
                    handler.removeCallbacks(runnable);
                }
                handler.postDelayed(runnable, 1000);
            }
        }
    };

    public class CustomAdapter extends BaseAdapter {

        Context context;
        ArrayList<AppBean> appBeanList;
        LayoutInflater inflater;

        public CustomAdapter(Context context, ArrayList<AppBean> appbeanList) {
            this.context = context;
            this.appBeanList = appbeanList;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return appBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return appBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.sho_srcn_item_shre, null);

            holder.appIcon = (ImageView) convertView
                    .findViewById(R.id.iv_cache_icon);



            holder.appIcon.getLayoutParams().height = Utils.scwidth_u / 7;
            holder.appIcon.getLayoutParams().width = Utils.scwidth_u / 7;

            holder.appIcon.setImageDrawable(appBeanList.get(position)
                    .getIcon());


            while (true) {
                for (int i = 0; i < Utils.shareAppColorList.size(); i++) {
                    if (appBeanList.get(position).getAppName()
                            .equalsIgnoreCase(Utils.shareAppNameList.get(i))) {
                        break;
                    }
                }
                break;
            }

            holder.appIcon
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String appurl = "https://play.google.com/store/apps/details?id=" + getPackageName();
                            try {
                                if (position == appBeanList.size() - 1) {

                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("image/*");
                                    Uri uri = Uri.parse("file://" + filepath);
                                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                                    intent.putExtra(Intent.EXTRA_TEXT, "Try this app : " + appurl);
                                    if (intent != null)
                                        startActivity(Intent.createChooser(intent,
                                                "share image using "
                                                        + appBeanList.get(position)
                                                        .getAppName()));

                                } else {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("image/*");
                                    Uri uri = Uri.parse("file://" + filepath);
                                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                                    intent.putExtra(Intent.EXTRA_TEXT, "Try this app : " + appurl);
                                    intent.setPackage(appBeanList.get(position)
                                            .getPackName());
                                    if (intent != null)
                                        startActivity(Intent.createChooser(intent,
                                                "share image using "
                                                        + appBeanList.get(position)
                                                        .getAppName()));

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

            return convertView;
        }

        class ViewHolder {
            //            TextView appName;
            ImageView appIcon;
            RelativeLayout root;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Drawable overflowIcon = toolbar_share.getOverflowIcon();
        if (overflowIcon != null) {
            Drawable newIcon = overflowIcon.mutate();
            newIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            toolbar_share.setOverflowIcon(newIcon);
        }
        getMenuInflater().inflate(R.menu.share_menu, menu);
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
        if (id == R.id.setWallpaper) {
            WallpaperManager wallPM = WallpaperManager
                    .getInstance(getApplicationContext());
            try {
                Bitmap bitmapResized = Bitmap.createScaledBitmap(shareImage,
                        Utils.scwidth_u - 10, Utils.scheight_u - 10, true);
                wallPM.setBitmap(bitmapResized);
                Toast.makeText(context, "Updated wallpaper successfully",
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Erorr", "--->" + e.getMessage());
            }

        } else if (id == R.id.delete) {
            deleteDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteDialog() {

        dialog1 = new Dialog(ShowScrenActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_title);
        dialog1.getWindow().setLayout((int) (Utils.scwidth_u / 1.2), WindowManager.LayoutParams.WRAP_CONTENT);
//        dialog1.getWindow().setBackgroundDrawableResource(R.drawable.dialog_round);
        dialog1.show();
        dialog1.setCancelable(false);
        txt_title = (TextView) dialog1.findViewById(R.id.txt_title);
        txt_title.setText("Do you want to delete?");
        btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);
        btn_cancel.setText("No");
        btn_exit = (Button) dialog1.findViewById(R.id.btn_exit);
        btn_exit.setText("Yes");
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = new File(filepath);
                f.delete();

                sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                        .fromFile(f)));

                MyCreationsFragment.update(filepath, true);
                ShowScrenActivity.this.finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.cancel();
            }
        });
    }

    public class AppBean {
        String appName, packName;
        Drawable icon;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }
    }

    private class CacheAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public int getItemCount() {
            return Utils.lastAppList.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder arg0, int position) {
            View view;
            ViewHolder holder = new ViewHolder(arg0.itemView);
            holder.layout.setBackgroundColor(Color.WHITE);
            ViewGroup.LayoutParams lya_params = holder.layout.getLayoutParams();
            lya_params.width = Utils.scwidth_u / 2;
            lya_params.height = (int) (Utils.scwidth_u / 2.2);

            ViewGroup.LayoutParams iv_params = holder.appicon.getLayoutParams();
            iv_params.width = Utils.scwidth_u / 3;
            iv_params.height = Utils.scwidth_u / 3;

            Glide.with(getApplicationContext()).load(Utils.lastAppList.get(position)
                    .getImgUrl().toString())
                    .placeholder(R.drawable.ic_stub).error(R.mipmap.ic_launcher)
                    .into(holder.appicon);
            holder.appname
                    .setText(Utils.lastAppList.get(position).getAppName());

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
            View view = View.inflate(getApplicationContext(),
                    R.layout.more_apps_adapter_text_image, null);
            return new ViewHolder(view);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView appicon;
        TextView appname;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = (RelativeLayout) itemView.findViewById(R.id.root);
            appicon = (ImageView) itemView
                    .findViewById(R.id.iv_cache_icon);
            appname = (TextView) itemView
                    .findViewById(R.id.tv_cache_name);
        }
    }


    private void displayAd() {
        try {
            LinearLayout banner_lay = (LinearLayout) findViewById(R.id.banner_lin_share);
            banner_lay.setVisibility(View.VISIBLE);
            RelativeLayout bannerspace = (RelativeLayout) findViewById(R.id.bannerspace_share);
            ViewGroup.MarginLayoutParams mparams = (ViewGroup.MarginLayoutParams) bannerspace.getLayoutParams();
            mparams.bottomMargin = Utils.dpToPx(5, getApplicationContext());
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getResources().getString(R.string.Banner_Ad_id));
            FrameLayout linearLayout = (FrameLayout) findViewById(R.id.banner_share);
            linearLayout.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        } catch (Exception e) {
            Log.e("Info", "MainActivity Ad Display Error" + e.getMessage());
        }
    }


    public static void addImageToGallery(String filePath, final Context context) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

    }

    File MyDir, distLocation;
    FileOutputStream outPutFile;
    String image_Name;

    private void saveData() {
        image_Name = "Image-"
                + new SimpleDateFormat("ddMMyy_HHmmss").format(Calendar
                .getInstance().getTime()) + ".jpg";

        MyDir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + getResources().getString(R.string.app_name));

        if (!MyDir.exists()) {
            MyDir.mkdirs();
        }
        distLocation = new File(MyDir, image_Name);

        if (distLocation.exists())
            distLocation.delete();

        outPutFile = null;


        try {
            outPutFile = new FileOutputStream(distLocation);
            shareImage.compress(Bitmap.CompressFormat.JPEG, 90, outPutFile);
            outPutFile.flush();
            Toast.makeText(context, "Image saved successfully",
                    Toast.LENGTH_SHORT).show();
            addImageToGallery(distLocation.getAbsolutePath(), context);
            MyCreationsFragment.update(distLocation.getAbsolutePath(), false);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}

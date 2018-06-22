package com.pinky.romanticphotoframes.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.jaouan.revealator.Revealator;
import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.adapters.EditRecyclerAdapter;
import com.pinky.romanticphotoframes.clipart.ClipArt;
import com.pinky.romanticphotoframes.crop.CropImageActivity;
import com.pinky.romanticphotoframes.crop.CropUtil;
import com.pinky.romanticphotoframes.effectsusage.ImageFilters;
import com.pinky.romanticphotoframes.fragments.TextColorFragment;
import com.pinky.romanticphotoframes.fragments.TextFontsFragment;
import com.pinky.romanticphotoframes.fragments.TextKeyBoardFragment;
import com.pinky.romanticphotoframes.fragments.ViewPagerAdapter;
import com.pinky.romanticphotoframes.touchusages.RecyclerTouchListener;
import com.pinky.romanticphotoframes.touchusages.Touch;
import com.pinky.romanticphotoframes.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;

/**
 * Created by 2117 on 4/17/2018.
 */
public class EditingActivity extends AppCompatActivity implements View.OnClickListener {


    TextView edit_txt_main, frams_txt_main, text_txt_main, efct_txt_main, save_txt_main;
    ImageView edit_img_main, frams_img_main, text_img_main, efct_img_main, save_img_main;

    public static RelativeLayout prfile_lay;
    ImageView img_prms_pic1, img_prms_pic2, img_prms_swap;
    LinearLayout lin_pick1_clk, lin_pick2_clk, lin_swap_clk;

    public static RelativeLayout rel_btm_btns;
    Button btn_exit, btn_cancel;
    TextView txt_title;
    Dialog dialog;
    public static Context context;
    public static RelativeLayout optionlayout, save_rel;

    boolean textShow = false;
    LinearLayout imgCameraGallery, imgFrame, imgEffect, imgText, imgsave;
    LinearLayout lin_prms_st_btns;
    public static EditText messageEditText;

    int clips_count = 1;
    ImageView userImagefrm1, userImagefrm2, uderFrameSelected;
    Bitmap resultpic1, resultpic2;
    int numcount = 0;
    //    text usage
    ViewPager txtmviewpager;
    TabLayout texttabsdatshow;
    static Handler handlerchk = new Handler();
    ////////////////////////////////////

    AlertDialog.Builder opencameragallery;
    AlertDialog alert;

    static RecyclerView framegallery, effectsrecyclerview;

    int imggalcampic1chk2 = 0;

    int[] effectslistview = {
            R.drawable.original,
            R.drawable.effect1,
            R.drawable.effect02,
            R.drawable.effect03,
            R.drawable.effect04,
            R.drawable.effect05,
            R.drawable.effect06,
            R.drawable.effect07,
            R.drawable.effect08,
            R.drawable.effect09,
            R.drawable.effect10,
            R.drawable.effect11,
            R.drawable.effect12,
            R.drawable.effect13,
            R.drawable.effect14,
            R.drawable.effect15,


    };
    ImageFilters imageFilters;


    public int color_tedt = Color.WHITE;
    public String txt_tedt;
    public Typeface typeface_tedt = Typeface.DEFAULT;


    static int cntchk = 0;
    public static Timer timer;
    Bitmap tempbitmap;
    int camera_ReqCode = 121, gallery_ReqCode = 212, pos, frmpos;
    File filep, f1;
    Uri outputFileUri;
    public static RelativeLayout textDialogLinear;
    private ImageView doneText;
    private ImageView cancelText;
    public static File sdImageMainDirectory;


    EditRecyclerAdapter adapterframe, adaptereffect;

    public static InterstitialAd interstitialAd_entryonsave;

    RelativeLayout rel_user_img1, rel_user_img2;
    LinearLayout.LayoutParams parms1, parms2;

    @SuppressLint("Range")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_act);
        context = EditingActivity.this;

        imggalcampic1chk2 = 0;
        if (Utils.isinternet) {
            if (SplashScreen.interstitialAd_entry == null) {
                //2 nd load
                interstitialAd_entryonsave = new InterstitialAd(EditingActivity.this);
                interstitialAd_entryonsave.setAdUnitId(getResources().getString(R.string.Interstitial_Ad_id_entryonsave));


                try {
                    AdRequest adRequest1 = new AdRequest.Builder().build();
                    interstitialAd_entryonsave.loadAd(adRequest1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        rel_user_img1 = (RelativeLayout) findViewById(R.id.rel_user_img1);
        rel_user_img2 = (RelativeLayout) findViewById(R.id.rel_user_img2);


        parms1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        parms1.weight = 1;
        rel_user_img1.setLayoutParams(parms1);

        parms2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        parms2.weight = 1;
        rel_user_img2.setLayoutParams(parms2);


        imageFilters = new ImageFilters(this);
        rel_btm_btns = (RelativeLayout) findViewById(R.id.rel_btm_btns);

        img_prms_pic1 = (ImageView) findViewById(R.id.img_prms_pic1);
        img_prms_pic2 = (ImageView) findViewById(R.id.img_prms_pic2);
        img_prms_swap = (ImageView) findViewById(R.id.img_prms_swap);

        img_prms_pic1.setLayoutParams(new RelativeLayout.LayoutParams(Utils.scwidth_u / 8, Utils.scwidth_u / 8));
        img_prms_pic2.setLayoutParams(new RelativeLayout.LayoutParams(Utils.scwidth_u / 8, Utils.scwidth_u / 8));
        img_prms_swap.setLayoutParams(new RelativeLayout.LayoutParams(Utils.scwidth_u / 8, Utils.scwidth_u / 8));


        lin_pick1_clk = (LinearLayout) findViewById(R.id.lin_pick1_clk);
        lin_pick2_clk = (LinearLayout) findViewById(R.id.lin_pick2_clk);
        lin_swap_clk = (LinearLayout) findViewById(R.id.lin_swap_clk);

        prfile_lay = (RelativeLayout) findViewById(R.id.prfile_lay);
        prfile_lay.setVisibility(View.GONE);


        lin_pick1_clk.setOnClickListener(this);
        lin_pick2_clk.setOnClickListener(this);
        lin_swap_clk.setOnClickListener(this);

        save_txt_main = (TextView) findViewById(R.id.save_txt_main);
        save_img_main = (ImageView) findViewById(R.id.save_img_main);

        edit_txt_main = (TextView) findViewById(R.id.edit_txt_main);
        edit_img_main = (ImageView) findViewById(R.id.edit_img_main);

        frams_img_main = (ImageView) findViewById(R.id.frams_img_main);
        frams_txt_main = (TextView) findViewById(R.id.frams_txt_main);

        text_img_main = (ImageView) findViewById(R.id.text_img_main);
        text_txt_main = (TextView) findViewById(R.id.text_txt_main);

        efct_img_main = (ImageView) findViewById(R.id.efct_img_main);
        efct_txt_main = (TextView) findViewById(R.id.efct_txt_main);

        messageEditText = (EditText) findViewById(R.id.messageEditText);

        txtmviewpager = (ViewPager) findViewById(R.id.txtmviewpager);
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(TextKeyBoardFragment.newInstance(), "");
        mViewPagerAdapter.addFragment(TextFontsFragment.newInstance(), getResources().getString(R.string.keybrdtext));
        mViewPagerAdapter.addFragment(TextColorFragment.newInstance(), "");
        txtmviewpager.setAdapter(mViewPagerAdapter);

        texttabsdatshow = (TabLayout) findViewById(R.id.texttabsdatshow);
        texttabsdatshow.setupWithViewPager(txtmviewpager);

        texttabsdatshow.getTabAt(0).setIcon(R.drawable.ic_text_kaybrd);
        texttabsdatshow.getTabAt(2).setIcon(R.drawable.ic_text_color);

        texttabsdatshow.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                txtmviewpager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    texttabsdatshow.getTabAt(0).setIcon(R.drawable.ic_keyboard_clk);
                    texttabsdatshow.getTabAt(2).setIcon(R.drawable.ic_text_color);
                } else if (tab.getPosition() == 2) {
                    texttabsdatshow.getTabAt(0).setIcon(R.drawable.ic_text_kaybrd);
                    texttabsdatshow.getTabAt(2).setIcon(R.drawable.ic_color_clk);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                texttabsdatshow.getTabAt(0).setIcon(R.drawable.ic_text_kaybrd);
                texttabsdatshow.getTabAt(2).setIcon(R.drawable.ic_text_color);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        save_rel = (RelativeLayout) findViewById(R.id.save_rel);
        optionlayout = (RelativeLayout) findViewById(R.id.optionlayout);
        optionlayout.setBackgroundColor(Color.TRANSPARENT);

        lin_prms_st_btns = (LinearLayout) findViewById(R.id.lin_prms_st_btns);
        lin_prms_st_btns.setLayoutParams(new RelativeLayout.LayoutParams(Utils.scheight_u / 9, ViewGroup.LayoutParams.FILL_PARENT));

        uderFrameSelected = (ImageView) findViewById(R.id.uderFrameSelected);

        uderFrameSelected.setImageResource(Utils.frams_alldata[Utils.pos_pass]);
        userImagefrm1 = (ImageView) findViewById(R.id.userImagefrm1);
        userImagefrm2 = (ImageView) findViewById(R.id.userImagefrm2);

        textDialogLinear = (RelativeLayout) findViewById(R.id.textDialogLinear);
        effectsrecyclerview = (RecyclerView) findViewById(R.id.effectsrecyclerView);
        try {
            if (Utils.bmp_img1 != null) {
                userImagefrm1.setImageBitmap(Utils.bmp_img1);
                resultpic1 = Utils.bmp_img1;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (Utils.bmp_img2 != null) {
                resultpic2 = Utils.bmp_img2;
                userImagefrm2.setImageBitmap(Utils.bmp_img2);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        userImagefrm1.setOnTouchListener(new Touch(EditingActivity.this));
        userImagefrm2.setOnTouchListener(new Touch(EditingActivity.this));

        imgsave = (LinearLayout) findViewById(R.id.imgsave);
        imgCameraGallery = (LinearLayout) findViewById(R.id.imgCameraGallery);
        imgFrame = (LinearLayout) findViewById(R.id.imgFrame);
        imgEffect = (LinearLayout) findViewById(R.id.imgEffect);
        imgText = (LinearLayout) findViewById(R.id.imgText);

        framegallery = (RecyclerView) findViewById(R.id.framegallery);
        doneText = (ImageView) findViewById(R.id.textOk);
        cancelText = (ImageView) findViewById(R.id.textCancel);
        doneText.getLayoutParams().width = Utils.scwidth_u / 8;
        doneText.getLayoutParams().height = Utils.scwidth_u / 8;
        cancelText.getLayoutParams().width = Utils.scwidth_u / 8;
        cancelText.getLayoutParams().height = Utils.scwidth_u / 8;

        File rootFile = new File(Environment.getExternalStorageDirectory()
                + File.separator);
        sdImageMainDirectory = new File(rootFile, "frame.jpg");

        initViews();
        setValAlphaclicks(0);

        handlerchk.postDelayed(runnablechk, 5000);
        rel_btm_btns.setVisibility(View.VISIBLE);


    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
    }

    public void initViews() {
        adapterframe = new EditRecyclerAdapter(this, null, Utils.frams_alldata,
                Utils.scheight_u, Utils.scwidth_u, false);
        framegallery.setAdapter(adapterframe);
        framegallery.setHasFixedSize(true);
        framegallery.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.HORIZONTAL));
        adaptereffect = new EditRecyclerAdapter(this, effectslistview, null,
                Utils.scwidth_u, Utils.scheight_u, true);
        effectsrecyclerview.setAdapter(adaptereffect);
        effectsrecyclerview.setHasFixedSize(true);
        effectsrecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.HORIZONTAL));
        effectsrecyclerview.addOnItemTouchListener(new RecyclerTouchListener(
                getApplicationContext(), effectsrecyclerview,
                new RecyclerTouchListener.RecyclerClick_Listener() {
                    @Override
                    public void onLongClick(View view, int position) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onClick(View view, int position) {
                        // TODO Auto-generated method stub
                        pos = position;
                        Asynic as = new Asynic();
                        as.execute("");
                    }
                }));

        framegallery.addOnItemTouchListener(new RecyclerTouchListener(
                getApplicationContext(), framegallery,
                new RecyclerTouchListener.RecyclerClick_Listener() {
                    @Override
                    public void onLongClick(View view, int position) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onClick(View view, int position) {
                        // TODO Auto-generated method stub
                        frmpos = position;

                        if (position == 2) {
                            parms1.weight = 1.1f;
                            parms2.weight = 0.9f;
                        } else if (position == 10) {
                            parms1.weight = 1.05f;
                            parms2.weight = 0.95f;
                        } else if (position == 15) {
                            parms1.weight = 1.15f;
                            parms2.weight = 0.85f;
                        } else if (position == 17) {
                            parms1.weight = 1.03f;
                            parms2.weight = 0.97f;

                        } else if (position == 19) {
                            parms1.weight = 1.07f;
                            parms2.weight = 0.93f;
                        } else {
                            parms1.weight = 1;
                            parms2.weight = 1;
                        }

                        rel_user_img1.setLayoutParams(parms1);
                        rel_user_img2.setLayoutParams(parms2);

                        uderFrameSelected.setImageResource(Utils.frams_alldata[position]);
                    }
                }));

        imgsave.setOnClickListener(this);
        imgCameraGallery.setOnClickListener(this);
        imgFrame.setOnClickListener(this);
        imgEffect.setOnClickListener(this);
        imgText.setOnClickListener(this);

        optionlayout.setVisibility(View.INVISIBLE);
        textDialogLinear.setVisibility(View.INVISIBLE);
        disableall();

        doneText.setOnClickListener(new View.OnClickListener() {
            private Bitmap tempbitmap1;

            @Override
            public void onClick(View v) {
                fabcheck();
                optionlayout.setVisibility(View.INVISIBLE);
                textDialogLinear.setVisibility(View.INVISIBLE);
                String userInputString = messageEditText.getText().toString();

                if (userInputString.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Enter Text", Toast.LENGTH_SHORT).show();
                } else {
                    tempbitmap1 = textAsBitmap(userInputString, Utils.colorr,
                            messageEditText.getTypeface());
                    if (textShow) {
                        check_Clipart(tempbitmap1, userInputString, Utils.colorr, messageEditText.getTypeface());

                    } else {

                        disableall();
                        ClipArt clipart = new ClipArt(EditingActivity.this, tempbitmap1, Utils.colorr, messageEditText.getTypeface(), messageEditText.getText().toString());
                        clipart.setId(clips_count++);
                        save_rel.addView(clipart);
                        clipart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                disableall();
                            }
                        });
                    }
                }
                textDialogLinear.setVisibility(View.INVISIBLE);

            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                fabcheck();
                if (textShow) {
                    messageEditText.setText(txt_tedt);
                    messageEditText.setTextColor(color_tedt);
                    messageEditText.setTypeface(typeface_tedt);
                } else {
                    Utils.colorr = Color.WHITE;
                    color_tedt = Color.WHITE;
                    txt_tedt = "";
                    typeface_tedt = Typeface.DEFAULT;
                }
                optionlayout.setVisibility(View.INVISIBLE);
                textDialogLinear.setVisibility(View.INVISIBLE);

            }
        });

    }

    private void fabcheck() {

        if (TextColorFragment.fab_btn != null && TextColorFragment.fab_btn.getVisibility() == View.GONE) {
            TextColorFragment.fab_btn.setVisibility(View.VISIBLE);
            Revealator.unreveal(TextColorFragment.reveal_layout)
                    .to(TextColorFragment.fab_btn)
                    .withCurvedTranslation()
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                        }
                    })
                    .start();
        }
    }

    // 1st Button openCameraGallery Operation.......................... Start
    // ***********************************
    public void openCameraGallery() {
        disableall();
        optionlayout.setVisibility(View.INVISIBLE);
        opencameragallery = new AlertDialog.Builder(context);
        LayoutInflater lf = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = lf.inflate(R.layout.opn_cam_gal_lay, null);

        opencameragallery.setView(vv);
        LinearLayout lincam = (LinearLayout) vv.findViewById(R.id.lincam);

        lincam.setLayoutParams(new RelativeLayout.LayoutParams(Utils.scwidth_u, (int) (Utils.scheight_u / 4.5)));

        LinearLayout openCamera = (LinearLayout) vv
                .findViewById(R.id.OpenCamera);
        LinearLayout openGallery = (LinearLayout) vv
                .findViewById(R.id.OpenGallery);

        lincam.setBackgroundColor(getResources().getColor(R.color.white));
        TextView cancelimagegall = (TextView) vv
                .findViewById(R.id.cancelimagegall);


        cancelimagegall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alert.dismiss();
                setValAlphaclicks(0);
            }
        });

        alert = opencameragallery.create();
        alert.setCanceledOnTouchOutside(false);
        alert.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        alert.show();
        alert.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        alert.getWindow().setGravity(CENTER);
        alert.getWindow().setLayout((Utils.scwidth_u - Utils.scwidth_u / 12), (int) (Utils.scheight_u / 2 - Utils.scheight_u / 15));
        openCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                alert.dismiss();

                openCamera();
            }
        });

        openGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alert.dismiss();
                openGallery();

            }
        });
    }

    public void openCamera() {

        File rootFile = new File(Environment.getExternalStorageDirectory() + File.separator);
        sdImageMainDirectory = new File(rootFile, "frame.jpg");

        outputFileUri = Uri.fromFile(sdImageMainDirectory);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        } else {
            File file = new File(outputFileUri.getPath());
            Uri photoUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(intent, camera_ReqCode);
        }

    }

    public void openGallery() {

        startActivityForResult(new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                gallery_ReqCode);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);

        if (arg0 == camera_ReqCode && arg1 == RESULT_OK) {
            Uri selectedImageURI = Uri.fromFile(sdImageMainDirectory);
            try {
                if (imggalcampic1chk2 == 1)
                    Utils.bmp_img1 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageURI);
                else
                    Utils.bmp_img2 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageURI);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Utils.isCameraImage = true;
            CropUtil.picpath = selectedImageURI;
            Intent i = new Intent(EditingActivity.this, CropImageActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("picchkpos", imggalcampic1chk2);
            i.putExtra("framepos", frmpos);
            i.putExtra("editmainchk", 1);
            startActivityForResult(i, 5);


        } else if (arg0 == gallery_ReqCode && arg1 == RESULT_OK) {

            try {
                Utils.isCameraImage = false;
                if (imggalcampic1chk2 == 1)
                    Utils.bmp_img1 = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), arg2.getData());
                else
                    Utils.bmp_img2 = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), arg2.getData());
                Intent i = new Intent(EditingActivity.this, CropImageActivity.class);
                i.putExtra("editmainchk", 1);
                i.putExtra("framepos", frmpos);
                i.putExtra("picchkpos", imggalcampic1chk2);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                CropUtil.picpath = arg2.getData();

                startActivityForResult(i, 5);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } else if (arg0 == 5) {
            String result = arg2.getStringExtra("redy");
            if (arg2.getIntExtra("picchkpos", 0) == 1) {
                resultpic1 = Utils.bmp_img1;
                userImagefrm1.setImageBitmap(Utils.bmp_img1);
                userImagefrm1.setVisibility(View.VISIBLE);
            } else {
                resultpic2 = Utils.bmp_img2;
                userImagefrm2.setImageBitmap(Utils.bmp_img2);
                userImagefrm2.setVisibility(View.VISIBLE);
            }

            frmpos = arg2.getIntExtra("framepos", 0);
            if (frmpos == 2) {
                parms1.weight = 1.1f;
                parms2.weight = 0.9f;
            } else if (frmpos == 10) {
                parms1.weight = 1.05f;
                parms2.weight = 0.95f;
            } else if (frmpos == 15) {
                parms1.weight = 1.15f;
                parms2.weight = 0.85f;
            } else if (frmpos == 17) {
                parms1.weight = 1.03f;
                parms2.weight = 0.97f;

            } else if (frmpos == 19) {
                parms1.weight = 1.07f;
                parms2.weight = 0.93f;
            } else {
                parms1.weight = 1;
                parms2.weight = 1;
            }

            rel_user_img1.setLayoutParams(parms1);
            rel_user_img2.setLayoutParams(parms2);


        }


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imgsave:
                setValAlphaclicks(5);
                save();
                break;
            case R.id.imgCameraGallery:
                optionlayout.setVisibility(View.INVISIBLE);
                effectsrecyclerview.setVisibility(View.GONE);
                textDialogLinear.setVisibility(View.INVISIBLE);
                framegallery.setVisibility(View.GONE);

                fabcheck();

                setValAlphaclicks(1);

                if (prfile_lay.getVisibility() == View.VISIBLE) {
                    prfile_lay.setVisibility(View.GONE);
                } else {
                    prfile_lay.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.imgFrame:
                fabcheck();

                optionlayout.setBackgroundColor(Color.TRANSPARENT);
                framegallery.setBackgroundColor(Color.TRANSPARENT);
                if (optionlayout.getVisibility() == View.VISIBLE) {
                    if (framegallery.getVisibility() == View.VISIBLE) {
                        optionlayout.setVisibility(View.INVISIBLE);
                        framegallery.setVisibility(View.GONE);
                        setValAlphaclicks(0);
                        textDialogLinear.setVisibility(View.INVISIBLE);
                        effectsrecyclerview.setVisibility(View.GONE);
                    } else {
                        optionlayout.setVisibility(View.VISIBLE);
                        effectsrecyclerview.setVisibility(View.GONE);
                        textDialogLinear.setVisibility(View.INVISIBLE);
                        framegallery.setVisibility(View.VISIBLE);
                        setValAlphaclicks(2);
                    }
                } else {
                    optionlayout.setVisibility(View.VISIBLE);
                    effectsrecyclerview.setVisibility(View.GONE);
                    textDialogLinear.setVisibility(View.INVISIBLE);
                    framegallery.setVisibility(View.VISIBLE);
                    setValAlphaclicks(2);
                }

                break;
            case R.id.imgEffect:
                fabcheck();
                optionlayout.setBackgroundColor(Color.TRANSPARENT);
                effectsrecyclerview.setBackgroundColor(Color.TRANSPARENT);
                if (optionlayout.getVisibility() == View.VISIBLE) {
                    if (effectsrecyclerview.getVisibility() == View.VISIBLE) {
                        optionlayout.setVisibility(View.INVISIBLE);
                        framegallery.setVisibility(View.GONE);
                        textDialogLinear.setVisibility(View.INVISIBLE);
                        effectsrecyclerview.setVisibility(View.GONE);
                        setValAlphaclicks(0);
                    } else {
                        optionlayout.setVisibility(View.VISIBLE);
                        framegallery.setVisibility(View.GONE);
                        textDialogLinear.setVisibility(View.INVISIBLE);
                        effectsrecyclerview.setVisibility(View.VISIBLE);
                        setValAlphaclicks(4);
                    }
                } else {
                    optionlayout.setVisibility(View.VISIBLE);
                    framegallery.setVisibility(View.GONE);
                    textDialogLinear.setVisibility(View.INVISIBLE);
                    effectsrecyclerview.setVisibility(View.VISIBLE);
                    setValAlphaclicks(4);
                }

                break;
            case R.id.imgText:
                textShow = false;

                color_tedt = Color.WHITE;
                txt_tedt = "";
                typeface_tedt = Typeface.DEFAULT;
                messageEditText.setText("");
                messageEditText.setCursorVisible(true);

                fabcheck();
                if (optionlayout.getVisibility() == View.VISIBLE) {
                    if (textDialogLinear.getVisibility() == View.VISIBLE) {
                        optionlayout.setVisibility(View.INVISIBLE);
                        framegallery.setVisibility(View.GONE);
                        textDialogLinear.setVisibility(View.INVISIBLE);
                        effectsrecyclerview.setVisibility(View.GONE);

                        setValAlphaclicks(0);
                    } else {
                        optionlayout.setVisibility(View.VISIBLE);
                        optionlayout.setBackgroundColor(Color.WHITE);
                        optionlayout.bringToFront();
                        textDialogLinear.setVisibility(View.VISIBLE);
                        framegallery.setVisibility(View.GONE);
                        effectsrecyclerview.setVisibility(View.GONE);

                        setValAlphaclicks(3);
                    }
                } else {
                    optionlayout.setVisibility(View.VISIBLE);
                    optionlayout.setBackgroundColor(Color.WHITE);
                    textDialogLinear.setVisibility(View.VISIBLE);
                    framegallery.setVisibility(View.GONE);
                    effectsrecyclerview.setVisibility(View.GONE);

                    setValAlphaclicks(3);
                }


                break;

            case R.id.lin_pick1_clk:

                fabcheck();
                imggalcampic1chk2 = 1;


                openCameraGallery();


                break;
            case R.id.lin_pick2_clk:

                fabcheck();
                imggalcampic1chk2 = 2;
                openCameraGallery();


                break;
            case R.id.lin_swap_clk:

                Bitmap bmp = resultpic1;
                resultpic1 = resultpic2;
                resultpic2 = bmp;

                Bitmap bmp2 = Utils.bmp_img1;
                Utils.bmp_img1 = Utils.bmp_img2;
                Utils.bmp_img2 = bmp2;


                try {
                    if (resultpic1 != null) {
                        userImagefrm1.setImageBitmap(resultpic1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (resultpic2 != null) {
                        userImagefrm2.setImageBitmap(resultpic2);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    public void setValAlphaclicks(int pos) {
        save_img_main.setAlpha(150);
        save_txt_main.setTextColor(getResources().getColor(R.color.white50));

        edit_img_main.setAlpha(150);
        edit_txt_main.setTextColor(getResources().getColor(R.color.white50));


        frams_img_main.setAlpha(150);
        frams_txt_main.setTextColor(getResources().getColor(R.color.white50));

        text_img_main.setAlpha(150);
        text_txt_main.setTextColor(getResources().getColor(R.color.white50));

        efct_img_main.setAlpha(150);
        efct_txt_main.setTextColor(getResources().getColor(R.color.white50));

        switch (pos) {
            case 1:
                edit_img_main.setAlpha(255);
                edit_txt_main.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                frams_img_main.setAlpha(255);
                frams_txt_main.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                text_img_main.setAlpha(255);
                text_txt_main.setTextColor(getResources().getColor(R.color.white));
                break;
            case 4:
                efct_img_main.setAlpha(255);
                efct_txt_main.setTextColor(getResources().getColor(R.color.white));
                break;
            case 5:
                save_img_main.setAlpha(150);
                save_txt_main.setTextColor(getResources().getColor(R.color.white50));
                break;
        }
    }


    public Bitmap textAsBitmap(String text, int textColor, Typeface typeface1) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(45);
        paint.setColor(textColor);
        paint.setTypeface(typeface1);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setShadowLayer(1, 0, 0, Color.BLACK);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // dialog_round1
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public void disableall() {
        // TODO Auto-generated method stub

        for (int i = 0; i < save_rel.getChildCount(); i++) {
            if (save_rel.getChildAt(i) instanceof ClipArt) {
                ((ClipArt) save_rel.getChildAt(i)).disableAll();
            }
        }
    }


    public void save() {

        disableall();
        save_rel.setDrawingCacheEnabled(true);
        tempbitmap = Bitmap.createBitmap(save_rel.getDrawingCache());
        save_rel.setDrawingCacheEnabled(false);
        filep = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/.pakkalocal");

        if (!filep.exists()) {
            filep.mkdirs();
        }

        f1 = new File(filep.getAbsolutePath(), "Image.jpg");

        if (!f1.exists()) {
            try {
                f1.createNewFile();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            tempbitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(f1));
            refreshGallery(f1);
            numcount++;
            if (Utils.isinternet) {

                if (SplashScreen.interstitialAd_entry != null && SplashScreen.interstitialAd_entry.isLoaded()) {
                    SplashScreen.interstitialAd_entry.show();
                    SplashScreen.interstitialAd_entry.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // TODO Auto-generated method stub
                            super.onAdLoaded();
                        }

                        public void onAdClosed() {
                            Log.e(" Ad loading ", "  ========   Save screen  ===========   ");
                            SplashScreen.interstitialAd_entry = null;
                            startIntent();
                        }

                        ;
                    });

                } else if (interstitialAd_entryonsave != null && interstitialAd_entryonsave.isLoaded()) {
                    interstitialAd_entryonsave.show();
                    interstitialAd_entryonsave.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // TODO Auto-generated method stub
                            super.onAdLoaded();
                        }

                        public void onAdClosed() {
                            interstitialAd_entryonsave = null;
                            startIntent();
                        }

                    });

                } else {
                    if (numcount >= 3 && numcount < 7) {
                        rateUsDialog();
                    } else
                        startIntent();
                }


            } else {
                startIntent();
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }


    }

    public void startIntent() {
        Intent intent = new Intent(context, ShowScrenActivity.class);
        intent.putExtra("imgsavechk", "no");
        intent.putExtra("final_image_path", f1.getAbsolutePath());
        startActivity(intent);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Utils.colorr = Color.WHITE;
        exitDialog();

    }

    public void exitDialog() {
        dialog = new Dialog(EditingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_title);
        dialog.getWindow().setLayout((int) (Utils.scwidth_u / 1.1), WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.setCancelable(false);
        txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        txt_title.setText("Are you sure you want to exit this page?");
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setText("No");
        btn_exit = (Button) dialog.findViewById(R.id.btn_exit);
        btn_exit.setText("Yes");
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ClipArt.image = null;
                Utils.bmp_img1 = null;
                Utils.bmp_img2 = null;

                EditingActivity.this.finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public static void setTimerCall() {
        handlerchk.removeCallbacks(runnablechk);
        handlerchk.postDelayed(runnablechk, 5000);
    }


    public class Asynic extends AsyncTask<String, Boolean, Boolean> {

        ProgressDialog progressDialog = new ProgressDialog(EditingActivity.this);

        Runnable runnable;

        @Override
        protected void onPreExecute() {

//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            try {
                progressDialog = new ProgressDialog(EditingActivity.this, R.style.MyAlertDialogStyle);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog = new ProgressDialog(EditingActivity.this);
            }
            progressDialog.setTitle("Loading Effect..!");
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            runnable = new Runnable() {

                @Override
                public void run() {
                }
            };
            switch (pos) {
                case 0:
                    resultpic1 = Utils.bmp_img1;
                    resultpic2 = Utils.bmp_img2;
                    break;

                case 1:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyShadingFilter(Utils.bmp_img1,
                                Color.GREEN);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyShadingFilter(Utils.bmp_img2,
                                Color.GREEN);
                    break;

                case 2:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyShadingFilter(Utils.bmp_img1,
                                Color.YELLOW);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyShadingFilter(Utils.bmp_img2,
                                Color.YELLOW);

                    break;
                case 3:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyBoostEffect(Utils.bmp_img1, 3, 67);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyBoostEffect(Utils.bmp_img2, 3, 67);

                    break;
                case 4:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyBoostEffect(Utils.bmp_img1, 1, 40);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyBoostEffect(Utils.bmp_img2, 1, 40);
                    break;

                case 5:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyMeanRemovalEffect(Utils.bmp_img1);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyMeanRemovalEffect(Utils.bmp_img2);
                    break;

                case 6:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyEngraveEffect(Utils.bmp_img1);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyEngraveEffect(Utils.bmp_img2);
                    break;
                case 7:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyShadingFilter(Utils.bmp_img1,
                                Color.CYAN);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyShadingFilter(Utils.bmp_img2,
                                Color.CYAN);
                    break;

                case 8:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyBrightnessEffect(Utils.bmp_img1, 80);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyBrightnessEffect(Utils.bmp_img2, 80);
                    runOnUiThread(runnable);
                    break;
                case 9:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyContrastEffect(Utils.bmp_img1, 40);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyContrastEffect(Utils.bmp_img2, 40);
                    runOnUiThread(runnable);
                    break;
                case 10:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyBlackFilter(Utils.bmp_img1);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyBlackFilter(Utils.bmp_img2);
                    break;

                case 11:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applySepiaToningEffect(Utils.bmp_img1,
                                10, 1.5, 0.6, 0.12);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applySepiaToningEffect(Utils.bmp_img2,
                                10, 1.5, 0.6, 0.12);

                    runOnUiThread(runnable);
                    break;

                case 12:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyBoostEffect(Utils.bmp_img1, 3, 25);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyBoostEffect(Utils.bmp_img2, 3, 25);
                    runOnUiThread(runnable);
                    break;
                case 13:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = ImageFilters.createSepiaToningEffect(Utils.bmp_img1,
                                100, 0.7, 0.3, 1.59);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = ImageFilters.createSepiaToningEffect(Utils.bmp_img2,
                                100, 0.7, 0.3, 1.59);
                    runOnUiThread(runnable);
                    break;
                case 14:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyTintEffect(Utils.bmp_img1, 100);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyTintEffect(Utils.bmp_img2, 100);
                    runOnUiThread(runnable);

                    break;

                case 15:
                    if (Utils.bmp_img1 != null)
                        resultpic1 = imageFilters.applyGaussianBlurEffect(Utils.bmp_img1);
                    if (Utils.bmp_img2 != null)
                        resultpic2 = imageFilters.applyGaussianBlurEffect(Utils.bmp_img2);
                    break;

            }


            return false;
        }

        protected void onPostExecute(Boolean res) {
            progressDialog.cancel();
            if (resultpic1 != null)
                userImagefrm1.setImageBitmap(resultpic1);
            if (resultpic2 != null)
                userImagefrm2.setImageBitmap(resultpic2);
            Handler handler = new Handler();
            handler.removeCallbacks(runnable);
            handler = null;
            super.onPostExecute(res);
        }

    }

    public void deleteCameraImage() {
        File rootFile = new File(Environment.getExternalStorageDirectory() + File.separator);
        sdImageMainDirectory = new File(rootFile, "frame.jpg");
        if (sdImageMainDirectory.exists())
            sdImageMainDirectory.delete();
    }


    public static void recyclerViewVisibilityChange() {
        framegallery.setVisibility(View.GONE);
        effectsrecyclerview.setVisibility(View.GONE);


    }


    static Runnable runnablechk = new Runnable() {
        @Override
        public void run() {

            if (optionlayout.getVisibility() == View.INVISIBLE && rel_btm_btns.getVisibility() == View.VISIBLE && prfile_lay.getVisibility() == View.GONE) {
                TranslateAnimation translation = new TranslateAnimation(1.0f, EditingActivity.rel_btm_btns.getWidth(), 1.0f, 1.0f);
                translation.setDuration(100);
                rel_btm_btns.startAnimation(translation);
                rel_btm_btns.setVisibility(View.INVISIBLE);
                prfile_lay.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    public void onResume() {
        if (rel_btm_btns != null) {

            RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
            parms.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            params.addRule(RelativeLayout.LEFT_OF, R.id.id_to_be_left_of);
            rel_btm_btns.setLayoutParams(parms);

            if (optionlayout != null) {
                RelativeLayout.LayoutParams parmopt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                parmopt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                parmopt.addRule(RelativeLayout.LEFT_OF, R.id.rel_btm_btns);
                rel_btm_btns.setLayoutParams(parms);
                optionlayout.setLayoutParams(parmopt);

            }
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();

    }

    @Override
    public void onStart() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onStart();
    }


    public void rateUsDialog() {
        final Dialog dialog22 = new Dialog(EditingActivity.this);
        dialog22.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog22.setContentView(R.layout.dialog_title);
        dialog22.getWindow().setLayout((int) (Utils.scwidth_u / 1.1), WindowManager.LayoutParams.WRAP_CONTENT);
        dialog22.show();
        dialog22.setCancelable(false);
        txt_title = (TextView) dialog22.findViewById(R.id.txt_title);
        txt_title.setText("Did you like this page?");
        btn_cancel = (Button) dialog22.findViewById(R.id.btn_cancel);
        btn_cancel.setText("Remind me later");
        btn_exit = (Button) dialog22.findViewById(R.id.btn_exit);
        btn_exit.setText("Like");
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numcount = 7;
                dialog22.cancel();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (Exception e) {
                    Log.e(" Eror Message", " Error " + e.getMessage());
                    startIntent();
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numcount = 0;
                dialog22.cancel();
                startIntent();
            }
        });

    }
    //////////

    public void showtextVisible_click() {
        rel_btm_btns.setVisibility(View.VISIBLE);
        fabcheck();
        optionlayout.setVisibility(View.VISIBLE);
        optionlayout.setBackgroundColor(Color.WHITE);
        textDialogLinear.setVisibility(View.VISIBLE);
        framegallery.setVisibility(View.GONE);
        effectsrecyclerview.setVisibility(View.GONE);

        setValAlphaclicks(3);

        textShow = true;
        for (int i = 0; i < save_rel.getChildCount(); i++) {
            if (save_rel.getChildAt(i) instanceof ClipArt) {
                if (((ClipArt) save_rel.getChildAt(i)).btndel.getVisibility() == View.VISIBLE) {
                    String dattxt = (((ClipArt) save_rel.getChildAt(i)).txt_t);
                    int color_u = (((ClipArt) save_rel.getChildAt(i)).color_t);
                    Typeface typese = (((ClipArt) save_rel.getChildAt(i)).typeface_t);

                    messageEditText.setText(dattxt);
                    messageEditText.setTypeface(typese);
                    messageEditText.setTextColor(color_u);

                    color_tedt = color_u;
                    txt_tedt = dattxt;
                    typeface_tedt = typese;
                    messageEditText.setCursorVisible(true);
                    messageEditText.setSelection(dattxt.length());
//                    messageEditText

                }
            }

        }


    }

    ////////////////////
    void check_Clipart(Bitmap bmp, String userInputString, int colorr, Typeface typeface) {
        for (int i = 0; i < save_rel.getChildCount(); i++) {
            if (save_rel.getChildAt(i) instanceof ClipArt) {
                if (((ClipArt) save_rel.getChildAt(i)).btndel.getVisibility() == View.VISIBLE) {

                    ((ClipArt) save_rel.getChildAt(i)).image.setImageBitmap(bmp);
                    ((ClipArt) save_rel.getChildAt(i)).txt_t = userInputString;
                    ((ClipArt) save_rel.getChildAt(i)).typeface_t = typeface;
                    ((ClipArt) save_rel.getChildAt(i)).color_t = colorr;
                    save_rel.invalidate();

                }
            }

        }
    }


    /////////////////////

}

package com.pinky.romanticphotoframes.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pinky.romanticphotoframes.R;

/**
 * Created by 2117 on 4/23/2018.
 */
public class WebShowActivity extends AppCompatActivity {
    Toolbar toolbar_web;

    WebView webvw_show;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_show_act);

        toolbar_web = (Toolbar) findViewById(R.id.toolbar_web);
        setSupportActionBar(toolbar_web);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_web.setTitle("EMI Calculator");
        toolbar_web.setTitleTextColor(Color.WHITE);
        toolbar_web.getOverflowIcon()
                .setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar_web.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                WebShowActivity.this.finish();

            }
        });

        webvw_show = (WebView) findViewById(R.id.webvw_show);

        webvw_show.setHorizontalScrollBarEnabled(false);
        startWebView("http://pakkalocalapps.com/privacy");
        webvw_show.setOnTouchListener(null);

        webvw_show.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webvw_show.setLongClickable(false);
        webvw_show.setHapticFeedbackEnabled(false);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webvw_show.canGoBack()) {
            webvw_show.goBack();
            this.finish();
        } else {
            // Let the system handle the back button
            this.finish();
        }


    }

    private void startWebView(String url) {
        webvw_show.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    try {
                        progressDialog = new ProgressDialog(WebShowActivity.this, R.style.MyAlertDialogStyle);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog = new ProgressDialog(WebShowActivity.this);
                    }
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog != null) {
                        progressDialog.cancel();
                    }
                    if (progressDialog.isShowing()) {
                        progressDialog.cancel();
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        webvw_show.getSettings().setJavaScriptEnabled(true);
        webvw_show.loadUrl(url);
    }


}

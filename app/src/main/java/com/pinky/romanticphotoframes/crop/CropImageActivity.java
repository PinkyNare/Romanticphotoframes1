/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pinky.romanticphotoframes.crop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.activities.EditingActivity;
import com.pinky.romanticphotoframes.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

public class CropImageActivity extends MonitoredActivity {
    View view_place;
    private static final boolean IN_MEMORY_CROP = Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1;
    private static final int SIZE_DEFAULT = 2048;
    private static final int SIZE_LIMIT = 4096;

    private final Handler handler = new Handler();

    private int aspectX;
    private int aspectY;

    // Output image size
    private int maxX;
    private int maxY;
    private int exifRotation;

    private Uri sourceUri;
    private Uri saveUri;

    private boolean isSaving;

    private int sampleSize;
    private RotateBitmap rotateBitmap;
    private CropImageView imageView;
    private HighlightView cropView;
    ImageView mycrop;

    private AdView adView;

    LinearLayout rotateLayout;
    ImageView left, right;
    int width, height;
    private int rotation = 0;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.crop_act);

        view_place = (View) findViewById(R.id.view_place);
        if (Utils.isinternet) {
            displayAd();
            view_place.setVisibility(View.VISIBLE);
        } else {
            view_place.setVisibility(View.GONE);
        }
        initViews();

        setupFromIntent();
        if (rotateBitmap == null) {
            finish();
            return;
        }
        startCrop();


    }

    @Override
    protected void onResume() {

        super.onResume();
        rotation = 0;


    }


    private void initViews() {
        DisplayMetrics mertics = this.getResources().getDisplayMetrics();
        width = mertics.widthPixels;
        height = mertics.heightPixels;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        mycrop = (ImageView) findViewById(R.id.mycrop);
        mycrop.setVisibility(View.GONE);
        rotateLayout = (LinearLayout) findViewById(R.id.rotateLayout);
        rotateLayout.getLayoutParams().height = height / 9;
        left = (ImageView) findViewById(R.id.left);
        left.getLayoutParams().width = width / 8;
        left.getLayoutParams().height = width / 8;
        right = (ImageView) findViewById(R.id.right);
        right.getLayoutParams().width = width / 8;
        right.getLayoutParams().height = width / 8;
        imageView = (CropImageView) findViewById(R.id.crop_image);
        imageView.context = this;
        imageView.setRecycler(new ImageViewTouchBase.Recycler() {
            @Override
            public void recycle(Bitmap b) {
                b.recycle();
                System.gc();
            }
        });
        left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                rotation -= 90;
                // rotation%=360;
                if (imageView != null) {
                    imageView.setRotation(rotation);
                }

                // Toast.makeText(getApplicationContext(), "Left rotation", 1000).show();
            }
        });
        right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rotation += 90;
                // rotation%=360;
                if (imageView != null) {
                    imageView.setRotation(rotation);
                }
                // TODO Auto-generated method stub
                // Toast.makeText(getApplicationContext(), "Right rotation", 1000).show();
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.crop_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        switch (id) {
            case R.id.btn_done:


                onSaveClicked(getIntent().getExtras().getInt("picchkpos"));

                if (Utils.isCameraImage)
                    deleteCameraImage();


                if (getIntent().getExtras().getInt("editmainchk") == 1) {
                    //1 edit
                    Intent ii = new Intent();
                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ii.putExtra("picchkpos",getIntent().getExtras().getInt("picchkpos"));

                    ii.putExtra("framepos",getIntent().getExtras().getInt("framepos"));

                    ii.putExtra("redy", "edit");
//                    i.putExtra("picchkpos",imggalcampic1chk2);
                    setResult(5, ii);

                    finish();
                } else {
                    //2 main
                    finish();
                    Intent i1 = new Intent(CropImageActivity.this,
                            EditingActivity.class);

                    i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i1);
                }


                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFromIntent() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            aspectX = extras.getInt(Crop.Extra.ASPECT_X);
            aspectY = extras.getInt(Crop.Extra.ASPECT_Y);
            maxX = extras.getInt(Crop.Extra.MAX_X);
            maxY = extras.getInt(Crop.Extra.MAX_Y);
            saveUri = extras.getParcelable(MediaStore.EXTRA_OUTPUT);
        }

        sourceUri = CropUtil.picpath;
        if (sourceUri != null) {
            exifRotation = CropUtil.getExifRotation(CropUtil.getFromMediaUri(
                    getContentResolver(), sourceUri));

            InputStream is = null;
            try {
                sampleSize = calculateBitmapSampleSize(sourceUri);
                is = getContentResolver().openInputStream(sourceUri);
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = sampleSize;
                rotateBitmap = new RotateBitmap(BitmapFactory.decodeStream(is,
                        null, option), exifRotation);
            } catch (IOException e) {
                setResultException(e);
            } catch (OutOfMemoryError e) {
                setResultException(e);
            } finally {
                CropUtil.closeSilently(is);
            }
        }
    }

    private int calculateBitmapSampleSize(Uri bitmapUri) throws IOException {
        InputStream is = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            is = getContentResolver().openInputStream(bitmapUri);
            BitmapFactory.decodeStream(is, null, options); // Just get image
            // size
        } finally {
            CropUtil.closeSilently(is);
        }

        int maxSize = getMaxImageSize();
        int sampleSize = 1;
        while (options.outHeight / sampleSize > maxSize
                || options.outWidth / sampleSize > maxSize) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    private int getMaxImageSize() {
        int textureLimit = getMaxTextureSize();
        if (textureLimit == 0) {
            return SIZE_DEFAULT;
        } else {
            return Math.min(textureLimit, SIZE_LIMIT);
        }
    }

    private int getMaxTextureSize() {
        // The OpenGL texture size is the maximum size that can be drawn in an
        // ImageView
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    private void startCrop() {
        if (isFinishing()) {
            return;
        }
        imageView.setImageRotateBitmapResetBase(rotateBitmap, true);
        imageView.setMaxHeight(500);
        imageView.setMaxWidth(500);
        CropUtil.startBackgroundJob(this, null,
                getResources().getString(R.string.crop__wait), new Runnable() {
                    public void run() {
                        final CountDownLatch latch = new CountDownLatch(1);
                        handler.post(new Runnable() {
                            public void run() {
                                if (imageView.getScale() == 1F) {
                                    imageView.center(true, true);
                                }
                                latch.countDown();
                            }
                        });
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        new Cropper().crop();
                    }
                }, handler);
    }

    private class Cropper {

        private void makeDefault() {
            if (rotateBitmap == null) {
                return;
            }

            HighlightView hv = new HighlightView(imageView);
            final int width = rotateBitmap.getWidth();
            final int height = rotateBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // Make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (aspectX != 0 && aspectY != 0) {
                if (aspectX > aspectY) {
                    cropHeight = cropWidth * aspectY / aspectX;
                } else {
                    cropWidth = cropHeight * aspectX / aspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(imageView.getUnrotatedMatrix(), imageRect, cropRect,
                    aspectX != 0 && aspectY != 0);
            imageView.add(hv);
        }

        public void crop() {
            handler.post(new Runnable() {
                public void run() {
                    makeDefault();
                    imageView.invalidate();
                    if (imageView.highlightViews.size() == 1) {
                        cropView = imageView.highlightViews.get(0);
                        cropView.setFocus(true);
                    }
                }
            });
        }
    }


    private void onSaveClicked(int picchkpos) {

        if (cropView == null || isSaving) {
            return;
        }
        isSaving = true;

        Bitmap croppedImage = null;
        Rect r = cropView.getScaledCropRect(sampleSize);
        int width = r.width();
        int height = r.height();

        int outWidth = width, outHeight = height;
        if (maxX > 0 && maxY > 0 && (width > maxX || height > maxY)) {
            float ratio = (float) width / (float) height;
            if ((float) maxX / (float) maxY > ratio) {
                outHeight = maxY;
                outWidth = (int) ((float) maxY * ratio + .5f);
            } else {
                outWidth = maxX;
                outHeight = (int) ((float) maxX / ratio + .5f);
            }
        }

        if (IN_MEMORY_CROP && rotateBitmap != null) {
            croppedImage = inMemoryCrop(rotateBitmap, croppedImage, r, width,
                    height, outWidth, outHeight);
            if (croppedImage != null) {
                imageView.setImageBitmapResetBase(croppedImage, true);
                imageView.center(true, true);
                imageView.highlightViews.clear();
            }
        } else {
            try {
                croppedImage = decodeRegionCrop(croppedImage, r);
            } catch (IllegalArgumentException e) {
                setResultException(e);
                finish();
                return;
            }

            if (croppedImage != null) {
                imageView.setImageRotateBitmapResetBase(new RotateBitmap(
                        croppedImage, exifRotation), true);
                imageView.center(true, true);
                imageView.highlightViews.clear();
            }
        }
        saveImage(croppedImage, picchkpos);
    }

    private void saveImage(Bitmap croppedImage, int picchkpos) {
        if (croppedImage != null) {
            if (picchkpos == 1)
                Utils.bmp_img1 = getRotatedBitmap(croppedImage);
            else
                Utils.bmp_img2 = getRotatedBitmap(croppedImage);
        }
    }


    private Bitmap getRotatedBitmap(Bitmap bitmap) {
        if (rotation % 360 == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation, bitmap.getWidth(),
                bitmap.getHeight());
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public Bitmap processImage(Bitmap bitmap) {
        Bitmap bmp;

        bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        Path oval = new Path();
        Matrix matrix = new Matrix();
        Region region = new Region();
        RectF ovalRect = new RectF(width / 8, 0, width - (width / 8), height);

        oval.addOval(ovalRect, Path.Direction.CW);
        matrix.postRotate(30, width / 2, height / 2);
        oval.transform(matrix, oval);
        region.setPath(oval, new Region((int) width / 2, 0, (int) width,
                (int) height));
        canvas.drawPath(region.getBoundaryPath(), paint);

        matrix.reset();
        oval.reset();
        oval.addOval(ovalRect, Path.Direction.CW);
        matrix.postRotate(-30, width / 2, height / 2);
        oval.transform(matrix, oval);
        region.setPath(oval, new Region(0, 0, (int) width / 2, (int) height));
        canvas.drawPath(region.getBoundaryPath(), paint);

        return bmp;
    }

    @SuppressLint("NewApi")
    private Bitmap decodeRegionCrop(Bitmap croppedImage, Rect rect) {
        // Release memory now
        clearImageView();

        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(sourceUri);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is,
                    false);
            final int width = decoder.getWidth();
            final int height = decoder.getHeight();

            if (exifRotation != 0) {
                // Adjust crop area to account for image rotation
                Matrix matrix = new Matrix();
                matrix.setRotate(-exifRotation);

                RectF adjusted = new RectF();
                matrix.mapRect(adjusted, new RectF(rect));

                // Adjust to account for origin at 0,0
                adjusted.offset(adjusted.left < 0 ? width : 0,
                        adjusted.top < 0 ? height : 0);
                rect = new Rect((int) adjusted.left, (int) adjusted.top,
                        (int) adjusted.right, (int) adjusted.bottom);
            }

            try {
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inSampleSize = 3;
                croppedImage = decoder.decodeRegion(rect,
                        op);

            } catch (IllegalArgumentException e) {
                // Rethrow with some extra information
                throw new IllegalArgumentException("Rectangle " + rect
                        + " is outside of the image (" + width + "," + height
                        + "," + exifRotation + ")", e);
            }

        } catch (IOException e) {
            finish();
        } catch (OutOfMemoryError e) {
            setResultException(e);
        } finally {
            CropUtil.closeSilently(is);
        }
        return croppedImage;
    }

    private Bitmap inMemoryCrop(RotateBitmap rotateBitmap, Bitmap croppedImage,
                                Rect r, int width, int height, int outWidth, int outHeight) {

        System.gc();

        try {
            croppedImage = Bitmap.createBitmap(outWidth, outHeight,
                    Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(croppedImage);
            RectF dstRect = new RectF(0, 0, width, height);

            Matrix m = new Matrix();
            m.setRectToRect(new RectF(r), dstRect, Matrix.ScaleToFit.FILL);
            m.preConcat(rotateBitmap.getRotateMatrix());
            canvas.drawBitmap(rotateBitmap.getBitmap(), m, null);
        } catch (OutOfMemoryError e) {
            setResultException(e);
            System.gc();
        }


        clearImageView();
        return croppedImage;
    }

    private void clearImageView() {
        imageView.clear();
        if (rotateBitmap != null) {
            rotateBitmap.recycle();
        }
        System.gc();
    }

    @SuppressWarnings("unused")
    private void saveOutput(Bitmap croppedImage) {
        if (saveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, 90,
                            outputStream);
                }
            } catch (IOException e) {
                setResultException(e);
            } finally {
                CropUtil.closeSilently(outputStream);
            }

            if (!IN_MEMORY_CROP) {
                // In-memory crop negates the rotation
                CropUtil.copyExifRotation(CropUtil.getFromMediaUri(
                        getContentResolver(), sourceUri), CropUtil
                        .getFromMediaUri(getContentResolver(), saveUri));
            }

            setResultUri(saveUri);
        }

        final Bitmap b = croppedImage;
        handler.post(new Runnable() {
            public void run() {
                imageView.clear();
                b.recycle();
            }
        });

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rotateBitmap != null) {
            rotateBitmap.recycle();
        }
    }

    @Override
    public boolean onSearchRequested() {
        return false;
    }

    public boolean isSaving() {
        return isSaving;
    }

    private void setResultUri(Uri uri) {
        setResult(RESULT_OK,
                new Intent().putExtra(MediaStore.EXTRA_OUTPUT, uri));
    }

    private void setResultException(Throwable throwable) {
        setResult(Crop.RESULT_ERROR,
                new Intent().putExtra(Crop.Extra.ERROR, throwable));
    }


    private void displayAd() {
        try {

            LinearLayout banner_lay = (LinearLayout) findViewById(R.id.banner_lay_cropLayout);
            banner_lay.setVisibility(View.VISIBLE);

            RelativeLayout bannerspace = (RelativeLayout) findViewById(R.id.view_place);
            bannerspace.getLayoutParams().height = Utils.dpToPx(5, getApplicationContext());

            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getResources().getString(R.string.Banner_Ad_id));
            FrameLayout linearLayout = (FrameLayout) findViewById(R.id.bannercrop);
            linearLayout.addView(adView);

            // Create an ad request. Check logcat output for the hashed device
            // ID to
            // get test ads on a physical device.
            AdRequest adRequest = new AdRequest.Builder().build();

            // Start loading the ad in the background.
            adView.loadAd(adRequest);
        } catch (Exception e) {

        }
    }

    public void deleteCameraImage() {
        File rootFile = new File(Environment.getExternalStorageDirectory() + File.separator);
        File tmpfile = new File(rootFile, "frame.jpg");

        if (tmpfile.exists())
            tmpfile.delete();
    }
}

package com.pinky.romanticphotoframes.touchusages;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.pinky.romanticphotoframes.activities.EditingActivity;
import com.pinky.romanticphotoframes.utils.Utils;


public class Touch implements OnTouchListener {

    // These matrices will be used to move and zoom image
    public static Matrix matrix = new Matrix();
    public static Matrix savedMatrix = new Matrix();
    long time_start, time_end;
    // We can be in one of these 3 states
    static int NONE = 0;
    static int DRAG = 1;
    static int ZOOM = 2;
    int mode = NONE;
    ImageView view;
    // Remember some things for zooming
    static PointF start = new PointF();
    static PointF mid = new PointF();
    static float oldDist = 1f;

    static float[] lastEvent = null;
    static float d = 0f;
    static float newRot = 0f;
    float m1, m2;

    EditingActivity editingActivit;

    public Touch(EditingActivity editingActivity) {

        this.editingActivit = editingActivity;

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        view = (ImageView) v;

        EditingActivity.optionlayout.setVisibility(View.INVISIBLE);
        EditingActivity.textDialogLinear.setVisibility(View.INVISIBLE);
        editingActivit.prfile_lay.setVisibility(View.GONE);
        EditingActivity.recyclerViewVisibilityChange();

        editingActivit.setValAlphaclicks(0);

        dumpEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                if (editingActivit.rel_btm_btns.getVisibility() == View.INVISIBLE || editingActivit.rel_btm_btns.getVisibility() == View.GONE) {
                    TranslateAnimation translation = new TranslateAnimation(EditingActivity.rel_btm_btns.getWidth(), 1.0f, 1.0f, 1.0f);
                    translation.setDuration(200);
                    EditingActivity.rel_btm_btns.startAnimation(translation);
                    EditingActivity.rel_btm_btns.setVisibility(View.VISIBLE);
                    EditingActivity.messageEditText.setText("");
                    EditingActivity.messageEditText.setCursorVisible(true);
                    EditingActivity.setTimerCall();

                }
                editingActivit.disableall();

                m1 = event.getX();
                m2 = event.getY();
                time_start = System.currentTimeMillis();
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                // start.set(0, event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);

                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_POINTER_UP:
                lastEvent = null;
                if (EditingActivity.context != null) {
//                  ;
                    EditingActivity edi = new EditingActivity();
                    edi.disableall();
                } else {
                    time_end = System.currentTimeMillis();
                    if (time_end - time_start < 300) {
                        matrix.reset();
                        matrix.postTranslate(m1 - Utils.scwidth_u / 8, m2 - Utils.scwidth_u / 8);
                    }
                }
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null) {
                        newRot = rotation(event);
                        float r1 = newRot - d;
                        matrix.postRotate(r1, view.getMeasuredWidth() / 2,
                                view.getMeasuredHeight() / 2);
                    }
                }
                break;
        }
        view.setImageMatrix(matrix);
        gestureDetector.onTouchEvent(event);
//        gestureDetectort.onTouchEvent(event);
        return true; // indicate event was handled
    }


    GestureDetector gestureDetector = new GestureDetector(EditingActivity.context, new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            matrix.reset();
            matrix.postTranslate(m1 - Utils.scwidth_u / 10, m2 - Utils.scwidth_u / 10);
            return super.onDoubleTap(e);
        }
    });


    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);

        return (float) Math.toDegrees(radians);
    }


    @SuppressWarnings("deprecation")
    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
    }

    /**
     * Determine the space between the first two fingers
     */
    @SuppressLint("FloatMath")
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * rotation
     **/
    float FindAngleDelta(float angle1, float angle2) {
        float From = ClipAngleTo0_360(angle2);
        float To = ClipAngleTo0_360(angle1);

        float Dist = To - From;

        if (Dist < -180.0f) {
            Dist += 360.0f;
        } else if (Dist > 180.0f) {
            Dist -= 360.0f;
        }

        return Dist;
    }

    float ClipAngleTo0_360(float Angle) {
        float Res = Angle;
        while (Angle < 0) {
            Angle += 360.0;
        }
        while (Angle >= 360.0) {
            Angle -= 360.0;
        }
        return Res;
    }
}
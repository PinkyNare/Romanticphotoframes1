package com.pinky.romanticphotoframes.fragments;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pinky.romanticphotoframes.R;
import com.pinky.romanticphotoframes.activities.EditingActivity;
import com.pinky.romanticphotoframes.textusage.CustomKeyboardView;
import com.pinky.romanticphotoframes.utils.Utils;

/**
 * Created by 2117 on 4/19/2018.
 */
public class TextKeyBoardFragment extends Fragment {

    CustomKeyboardView mykeyboardview;

    Keyboard mKeyboard;

    public TextKeyBoardFragment() {
    }


    public static TextKeyBoardFragment newInstance() {
        TextKeyBoardFragment fragment = new TextKeyBoardFragment();
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
        View view = inflater.inflate(R.layout.frg_keyboard, container, false);

        EditingActivity.messageEditText.clearFocus();
        EditingActivity.messageEditText.requestFocusFromTouch();

        mykeyboardview = (CustomKeyboardView) view.findViewById(R.id.mykeyboardview);

        selectKeyboardopnClick();


        EditingActivity.messageEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.messageEditText) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            selectKeyboardopnClick();
                            break;
                        case MotionEvent.ACTION_UP:
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;

                    }
                }
                return false;
            }
        });
        return view;
    }

    public void selectKeyboardopnClick() {
        mykeyboardview.setPreviewEnabled(false);
        try {
//            getView().getWindowToken()
            hideSoftKeyboard(getActivity());
            mKeyboard = new Keyboard(getActivity(), R.xml.kbd_hin1);

//            mKeyboard = new Keyboard(getActivity(), R.xml.kbd_tel1);

            mykeyboardview.setVisibility(View.VISIBLE);
            mykeyboardview.setKeyboard(mKeyboard);
//            mykeyboardview.setLayoutParams(new RelativeLayout.LayoutParams(Utils.scheight_u-(Utils.scwidth_u/4),ViewGroup.LayoutParams.FILL_PARENT));

            mykeyboardview.setOnKeyboardActionListener(new BasicOnKeyboardActionListener(
                    getActivity(),
                    EditingActivity.messageEditText,
                    mykeyboardview));


            EditingActivity.messageEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    selectKeyboardopnClick();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            Layout layout = ((EditText) v).getLayout();
                            float x = event.getX() + EditingActivity.messageEditText.getScrollX();
                            float y = event.getY() + EditingActivity.messageEditText.getScrollY();
                            int line = layout.getLineForVertical((int) y);

                            int offset = layout.getOffsetForHorizontal(line, x);
                            if (offset > 0)
                                if (x > layout.getLineMax(0))
                                    EditingActivity.messageEditText
                                            .setSelection(offset);     // touch was at end of text
                                else
                                    EditingActivity.messageEditText.setSelection(offset - 1);

                            EditingActivity.messageEditText.setCursorVisible(true);
                            break;
                    }
                    return true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            mykeyboardview.setVisibility(View.GONE);
            //Show Default Keyboard
            InputMethodManager imm =
                    (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(EditingActivity.messageEditText, 0);
            EditingActivity.messageEditText.setOnTouchListener(null);

        }

    }

    private void hideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(EditingActivity.messageEditText.getWindowToken(), 0);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EditingActivity.messageEditText.getWindowToken(), 0);

    }


    @Override
    public void onResume() {
        super.onResume();
//        mykeyboardview.setVisibility(View.VISIBLE);
//        selectKeyboardopnClick();
//        newInstance();


    }

    public class BasicOnKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {

        EditText editText;
        CustomKeyboardView displayKeyboardView;
        private Activity mTargetActivity;

        public BasicOnKeyboardActionListener(Activity targetActivity, EditText editText,
                                             CustomKeyboardView
                                                     displayKeyboardView) {
            mTargetActivity = targetActivity;
            this.editText = editText;
            this.displayKeyboardView = displayKeyboardView;
        }

        @Override
        public void swipeUp() {
            // TODO Auto-generated method stub

        }

        @Override
        public void swipeRight() {
            // TODO Auto-generated method stub

        }

        @Override
        public void swipeLeft() {
            // TODO Auto-generated method stub

        }

        @Override
        public void swipeDown() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onText(CharSequence text) {
            // TODO Auto-generated method stub
            int cursorPosition = editText.getSelectionEnd();
            String before = editText.getText().toString().substring(0, cursorPosition);
            String after = editText.getText().toString().substring(cursorPosition);
            editText.setText(before + text + after);
            editText.setSelection(cursorPosition + 1);
        }

        @Override
        public void onRelease(int primaryCode) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPress(int primaryCode) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            switch (primaryCode) {
                case 66:

                case 67:
                    long eventTime = System.currentTimeMillis();
                    KeyEvent event =
                            new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, primaryCode, 0, 0, 0, 0,
                                    KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE);

                    mTargetActivity.dispatchKeyEvent(event);


                    break;


                case -118:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_hin2));
//                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_tel2));

                    break;
                case -119:
//                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_tel1));
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_hin1));
                    break;

                case -120:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_eng1));
//                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_eng1));
                    break;
                case -121:
                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_eng2));
//                    displayKeyboardView.setKeyboard(new Keyboard(mTargetActivity, R.xml.kbd_eng2));
                    break;
                default:
                    break;
            }
        }
    }


}

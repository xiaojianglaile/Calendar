package com.jeek.calendar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jeek.calendar.R;

/**
 * Created by Jimmy on 2016/10/12 0012.
 */
public class SelectColorDialog extends Dialog implements View.OnClickListener {

    private OnSelectColorListener mOnSelectColorListener;
    private int mColor;
    private View[] mColorBorderView;
    private View[] mColorView;

    public SelectColorDialog(Context context, OnSelectColorListener onSelectColorListener) {
        super(context, R.style.DialogFullScreen);
        mOnSelectColorListener = onSelectColorListener;
        setContentView(R.layout.dialog_select_color);
        initView();
    }

    private void initView() {
        findViewById(R.id.tvCancel).setOnClickListener(this);
        findViewById(R.id.tvConfirm).setOnClickListener(this);
        mColorBorderView = new View[6];
        mColorView = new View[6];
        LinearLayout llTopColor = (LinearLayout) findViewById(R.id.llTopColor);
        LinearLayout llBottomColor = (LinearLayout) findViewById(R.id.llBottomColor);
        for (int i = 0; i < llTopColor.getChildCount(); i++) {
            RelativeLayout child = (RelativeLayout) llTopColor.getChildAt(i);
            mColorBorderView[i] = child.getChildAt(0);
            mColorView[i] = child.getChildAt(1);
            final int finalI = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeColor(finalI);
                }
            });
        }
        for (int i = 0; i < llBottomColor.getChildCount(); i++) {
            RelativeLayout child = (RelativeLayout) llBottomColor.getChildAt(i);
            mColorBorderView[i + 3] = child.getChildAt(0);
            mColorView[i + 3] = child.getChildAt(1);
            final int finalI = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeColor(finalI + 3);
                }
            });
        }
    }

    private void changeColor(int position) {
        mColor = position;
        for (int i = 0; i < mColorBorderView.length; i++) {
            mColorBorderView[i].setVisibility(position == i ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvConfirm:
                if (mOnSelectColorListener != null) {
                    mOnSelectColorListener.onSelectColor(mColor);
                }
                dismiss();
                break;
        }
    }

    public interface OnSelectColorListener {
        void onSelectColor(int color);
    }

}

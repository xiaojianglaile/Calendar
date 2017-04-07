package com.jeek.calendar.widget.calendar.schedule;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

/**
 * Created by Jimmy on 2017/4/7 0007.
 */
public class AutoMoveAnimation extends Animation {

    private View mView;
    private int mDistance;
    private float mPositionY;

    public AutoMoveAnimation(View view, int distance) {
        mView = view;
        mDistance = distance;
        setDuration(200);
        setInterpolator(new DecelerateInterpolator(1.5f));
        mPositionY = mView.getY();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        mView.setY(mPositionY + interpolatedTime * mDistance);
    }

}

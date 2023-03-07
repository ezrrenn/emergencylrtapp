package com.example.lrtproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.appcompat.widget.AppCompatImageView;

public class ZoomableImageView extends AppCompatImageView {
    private float mLastTouchX;
    private float mLastTouchY;


    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    public ZoomableImageView(Context context) {
        super(context);
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);

        // Get the x and y coordinates of the touch event.
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Remember the initial touch position.
                mLastTouchX = x;
                mLastTouchY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                // Calculate the distance moved.
                float dx = x - mLastTouchX;
                float dy = y - mLastTouchY;

                // Move the view.
                setTranslationX(getTranslationX() + dx);
                setTranslationY(getTranslationY() + dy);

                // Remember the new touch position.
                mLastTouchX = x;
                mLastTouchY = y;
                break;
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            setScaleX(mScaleFactor);
            setScaleY(mScaleFactor);
            return true;
        }
    }
}

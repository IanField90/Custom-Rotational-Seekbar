package com.ianfield.customviewexample.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ianfield.customviewexample.R;

/**
 * Created by Ian Field on 29/07/15.
 */
public class RotationalSeekView extends View {
    // Internal
    Paint mTextPaint;
    Paint mSeekbarPaint;

    private static int TEXTPOS_LEFT = 0;
    private static int TEXTPOS_RIGHT = 1;
    private static int TEXTPOS_CENTER = 2;

    private RectF mSeekBounds = new RectF();
    private RectF mRightTextBounds = new RectF();
    private RectF mLeftTextBounds = new RectF();
    private RectF mCenterTextBounds = new RectF();

    // Exposed
    private int mCurrentProgress = 0;
    private int mMaxProgress = 100;
    // Default center
    private int mTextPos = TEXTPOS_CENTER;
    private boolean mShowProgressText = true;
    private boolean mShowLengthText = true;

    private int mProgressColor = 0xffff00ff;
    private int mTrackColor = 0x33cccccc;

    public RotationalSeekView(Context context) {
        super(context);
        init();
    }

    public RotationalSeekView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RotationalSeekView,
                0, 0);

        try {
            mShowProgressText = a.getBoolean(R.styleable.RotationalSeekView_showProgressText, false);
            mTextPos = a.getInteger(R.styleable.RotationalSeekView_labelPosition, 2);
            mProgressColor = a.getColor(R.styleable.RotationalSeekView_progressColor, 0xffff00ff);
            mTrackColor = a.getColor(R.styleable.RotationalSeekView_trackColor, 0x33cccccc);
            mCurrentProgress = a.getInteger(R.styleable.RotationalSeekView_currentProgress, 0);
            mMaxProgress = a.getInteger(R.styleable.RotationalSeekView_maxProgress, 100);
        } finally {
            a.recycle();
        }
        init();
    }

    public RotationalSeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public boolean isShowProgressText() {
        return mShowProgressText;
    }

    public void setShowProgressText(boolean showProgressText) {
        this.mShowProgressText = showProgressText;
        invalidate();
        requestLayout();
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        if (currentProgress <= mMaxProgress) {
            this.mCurrentProgress = currentProgress;
            invalidate();
            requestLayout();
        }
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
        invalidate();
        requestLayout();
    }

    public int getTextPos() {
        return mTextPos;
    }

    public void setTextPos(int textPos) {
        this.mTextPos = textPos;
        invalidate();
        requestLayout();
    }

    public boolean isShowLengthText() {
        return mShowLengthText;
    }

    public void setShowLengthText(boolean showLengthText) {
        this.mShowLengthText = showLengthText;
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(dpToPx(20));

        mSeekbarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSeekbarPaint.setColor(0xffff00ff);
        mSeekbarPaint.setStyle(Paint.Style.STROKE);
        mSeekbarPaint.setStrokeWidth(dpToPx(10));
        mSeekbarPaint.setStrokeCap(Paint.Cap.ROUND);
        mSeekbarPaint.setFilterBitmap(true);
        mSeekbarPaint.setAntiAlias(true);
        mSeekbarPaint.setDither(true);
//        mSeekbarPaint.setXfermode(Xfermode)
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // track
        mSeekbarPaint.setColor(mTrackColor);
        mSeekbarPaint.setStrokeWidth(dpToPx(14));
        // Draw the track as a full circle
        canvas.drawArc(mSeekBounds, 0, 360, false, mSeekbarPaint);
//        canvas.drawCircle(mSeekBounds.centerX(), mSeekBounds.centerY(), mSeekBounds.width()/2, mSeekbarPaint);

        mSeekbarPaint.setStrokeWidth(dpToPx(14));
        mSeekbarPaint.setColor(mProgressColor);
        canvas.drawArc(mSeekBounds, -90, (360 * ((float) mCurrentProgress / (float) mMaxProgress)), false, mSeekbarPaint);

        if (mShowProgressText) {
            String progressText = getProgressAsTime();
            if (mTextPos == TEXTPOS_CENTER) {
                mCenterTextBounds = new RectF(mSeekBounds);
                // measure text width
                mCenterTextBounds.right = mTextPaint.measureText(progressText, 0, progressText.length());
                // measure text height
                mCenterTextBounds.bottom = mTextPaint.descent() - mTextPaint.ascent();
                mCenterTextBounds.left += (mSeekBounds.width() - mCenterTextBounds.right) / 2.0f;
                mCenterTextBounds.top += (mSeekBounds.height() - mCenterTextBounds.bottom) / 2.0f;
                canvas.drawText(progressText, mCenterTextBounds.left, mCenterTextBounds.top - mTextPaint.ascent(), mTextPaint);
            } else if (mTextPos == TEXTPOS_LEFT) {
                mLeftTextBounds = new RectF(mSeekBounds);
                // measure text width
                mLeftTextBounds.right = mTextPaint.measureText(progressText, 0, progressText.length());
                // measure text height
                mLeftTextBounds.bottom = mTextPaint.descent() - mTextPaint.ascent();
                mLeftTextBounds.left += dpToPx(10);
                mLeftTextBounds.top += (mSeekBounds.height() - mLeftTextBounds.bottom) / 2.0f;
                canvas.drawText(progressText, mLeftTextBounds.left, mLeftTextBounds.top - mTextPaint.ascent(), mTextPaint);
            } else if (mTextPos == TEXTPOS_RIGHT) {
                mRightTextBounds = new RectF(mSeekBounds);
                // measure text width
                mRightTextBounds.right = mTextPaint.measureText(progressText, 0, progressText.length());
                // measure text height
                mRightTextBounds.bottom = mTextPaint.descent() - mTextPaint.ascent();
                mRightTextBounds.left += (mSeekBounds.width() - mRightTextBounds.right) - dpToPx(10);
                mRightTextBounds.top += (mSeekBounds.height() - mRightTextBounds.bottom) / 2.0f;
                canvas.drawText(progressText, mRightTextBounds.left, mRightTextBounds.top - mTextPaint.ascent(), mTextPaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //
        // Set dimensions for text, pie chart, etc
        //
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        // Figure out how big we can make the pie.
        float diameter = Math.min(ww, hh);
        // Adjust for width of the paint stroke
        mSeekBounds = new RectF(
                0f,
                0f,
                diameter,
                diameter);
        mSeekBounds.offsetTo(getPaddingLeft(), getPaddingTop());
        // Prevent top left and right clipping
        mSeekBounds.inset(dpToPx(10), dpToPx(10));

        mRightTextBounds = new RectF(mSeekBounds);
        mLeftTextBounds = new RectF(mSeekBounds);
        mCenterTextBounds = new RectF(mSeekBounds);
    }


    private void setLayerToSW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void setLayerToHW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    private float dpToPx(int dp) {
        return (int) (dp * this.getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    private String getProgressAsTime() {
        return String.format("%d/%d", mCurrentProgress, mMaxProgress);
    }

    class GestureDetectorListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            MotionEvent.PointerCoords coords = new MotionEvent.PointerCoords();
            e.getPointerCoords(0, coords);
            Log.d("Coords", "(" + coords.getAxisValue(MotionEvent.AXIS_X) + ", " + coords.getAxisValue(MotionEvent.AXIS_Y) + ")");
            Log.d("Coords", "Is inside view bounds? " + mSeekBounds.contains(coords.getAxisValue(MotionEvent.AXIS_X), coords.getAxisValue(MotionEvent.AXIS_Y)));
            return true;
        }



        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            S = R * Theta

            MotionEvent.PointerCoords coords = new MotionEvent.PointerCoords();
            e2.getPointerCoords(0, coords);
            Log.d("Coords", "(" + coords.getAxisValue(MotionEvent.AXIS_X) + ", " + coords.getAxisValue(MotionEvent.AXIS_Y) + ")");


            return super.onScroll(e1, e2, distanceX, distanceY);
        }


    };

    private GestureDetector mGestureDetector = new GestureDetector(this.getContext(), new GestureDetectorListener());

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }
}

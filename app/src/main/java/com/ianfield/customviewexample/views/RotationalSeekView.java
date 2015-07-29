package com.ianfield.customviewexample.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.ianfield.customviewexample.R;

/**
 * Created by Ian Field on 29/07/15.
 */
public class RotationalSeekView extends View {
    // Internal
    Paint mTextPaint;
    Paint mSeekbarPaint;
    Paint mSeekbarBackgroundPaint;

    private static int TEXTPOS_LEFT = 0;
    private static int TEXTPOS_RIGHT = 1;
    private static int TEXTPOS_CENTER = 2;

    private RectF mSeekBounds = new RectF();



    // Exposed
//    String maxProgress = "10:51";
//    String currentProgress = "4:32";

    private int mCurrentProgress = 32;
    private int mMaxProgress = 100;
    // Default center
    private int mTextPos = 2;
    private boolean mShowProgressText = true;
    private boolean mShowLengthText = true;

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
        this.mCurrentProgress = currentProgress;
        invalidate();
        requestLayout();
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
        mTextPaint.setTextSize(40f);

        mSeekbarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSeekbarPaint.setColor(0xffff00ff);
        mSeekbarPaint.setStyle(Paint.Style.STROKE);
        mSeekbarPaint.setStrokeWidth(dpToPx(10));
        mSeekbarPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // track
        mSeekbarPaint.setColor(0x33cccccc);
        mSeekbarPaint.setStrokeWidth(dpToPx(10));
        canvas.drawArc(mSeekBounds, 0, 360, false, mSeekbarPaint);

        mSeekbarPaint.setColor(0xffff00ff);
        mSeekbarPaint.setStrokeWidth(dpToPx(10));
        canvas.drawArc(mSeekBounds, -90, (360 * ((float) mCurrentProgress / (float) mMaxProgress)), false, mSeekbarPaint);

        if (mTextPos == TEXTPOS_CENTER) {
            RectF bounds = new RectF(mSeekBounds);
            // measure text width
            bounds.right = mTextPaint.measureText("Hello", 0, "Hello".length());
            // measure text height
            bounds.bottom = mTextPaint.descent() - mTextPaint.ascent();
            bounds.left += (mSeekBounds.width() - bounds.right) / 2.0f;
            bounds.top += (mSeekBounds.height() - bounds.bottom) / 2.0f;
            canvas.drawText("Hello", bounds.left, bounds.top - mTextPaint.ascent(), mTextPaint);
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

        // Account for the label
//        if (mShowText) xpad += mTextWidth;

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        // Figure out how big we can make the pie.
        float diameter = Math.min(ww, hh);
        // Adjust for width of the paint stroke
//        diameter -= dpToPx(10);
        mSeekBounds = new RectF(
                0f,
                0f,
                diameter,
                diameter);
        mSeekBounds.offsetTo(getPaddingLeft(), getPaddingTop());
        mSeekBounds.inset(dpToPx(10), dpToPx(10));


//        setPivot(mSeekBounds.width() / 2, mSeekBounds.height() / 2);

//        mPointerY = mTextY - (mTextHeight / 2.0f);
//        float pointerOffset = mPieBounds.centerY() - mPointerY;
//
//        // Make adjustments based on text position
//        if (mTextPos == TEXTPOS_LEFT) {
//            mTextPaint.setTextAlign(Paint.Align.RIGHT);
//            if (mShowText) mPieBounds.offset(mTextWidth, 0.0f);
//            mTextX = mPieBounds.left;
//
//            if (pointerOffset < 0) {
//                pointerOffset = -pointerOffset;
//                mCurrentItemAngle = 225;
//            } else {
//                mCurrentItemAngle = 135;
//            }
//            mPointerX = mPieBounds.centerX() - pointerOffset;
//        } else {
//            mTextPaint.setTextAlign(Paint.Align.LEFT);
//            mTextX = mPieBounds.right;
//
//            if (pointerOffset < 0) {
//                pointerOffset = -pointerOffset;
//                mCurrentItemAngle = 315;
//            } else {
//                mCurrentItemAngle = 45;
//            }
//            mPointerX = mPieBounds.centerX() + pointerOffset;
//        }
//
//        mShadowBounds = new RectF(
//                mPieBounds.left + 10,
//                mPieBounds.bottom + 10,
//                mPieBounds.right - 10,
//                mPieBounds.bottom + 20);
//
//        // Lay out the child view that actually draws the pie.
//        mPieView.layout((int) mPieBounds.left,
//                (int) mPieBounds.top,
//                (int) mPieBounds.right,
//                (int) mPieBounds.bottom);
//        mPieView.setPivot(mPieBounds.width() / 2, mPieBounds.height() / 2);
//
//        mPointerView.layout(0, 0, w, h);
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


}

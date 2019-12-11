package com.example.qiang.fireWorks;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.qiang.R;


public class FireworkView extends AppCompatImageView {

    private float mWidth;
    private float mHeight;

    private FireworkDrawable mImageDrawable;
    private Drawable mBackgroundDrawable;
    private String TAG = FireworkView.class.getSimpleName();
    private boolean mShowBg;
    private int mFireworkCount;
    private boolean mShowFirework;

    public FireworkView(Context context) {
        this(context, null);
//        init(context);
    }

    public FireworkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
//        init(context);

    }

    public FireworkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(attrs);
        init(context);
    }

    private void initAttributeSet(@Nullable AttributeSet attrs) {
        if (null == attrs) return;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FireworkView);
        if (null == typedArray) return;
        mShowBg = typedArray.getBoolean(R.styleable.FireworkView_showBg, true);
        mFireworkCount = typedArray.getInt(R.styleable.FireworkView_fireworkCount, 7);
        mShowFirework = typedArray.getBoolean(R.styleable.FireworkView_showFirework, false);
        typedArray.recycle();
    }

    private void init(Context context) {
        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        Ratio.SCREEN_WIDTH = mWidth;
        mHeight = context.getResources().getDisplayMetrics().heightPixels;
        setWillNotDraw(false);
        mImageDrawable = new FireworkDrawable(mWidth, mHeight,mFireworkCount);
//        mImageDrawable.setFireworkCount(mFireworkCount);
        mImageDrawable.setShowFirework(mShowFirework);
        if (mShowBg) {
            mBackgroundDrawable = new NightDrawable(mWidth, mHeight);
            setBackgroundDrawable(mBackgroundDrawable);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        setBackgroundDrawable(mBackgroundDrawable);
        setImageDrawable(mImageDrawable);
        Log.i(TAG, "onAttachedToWindow: ");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure: ");
        if (mWidth > 0 && mHeight > 0) {
            Log.i(TAG, "setMeasuredDimension: ");
            setMeasuredDimension((int) mWidth, (int) (mHeight));
        }
    }


    public void showFirework() {
        mImageDrawable.showFirework();
    }

    public void setOnDismissListener(FireworkDrawable.OnDismissListener listener) {
        mImageDrawable.setOnDismissListener(listener);
    }

    public void setFireworkCount(int fireworkCount) {
        mImageDrawable.setFireworkCount(fireworkCount);
    }
}

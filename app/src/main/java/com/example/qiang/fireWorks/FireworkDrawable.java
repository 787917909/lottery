package com.example.qiang.fireWorks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class                                                                                        FireworkDrawable extends Drawable {
    private float w;
    private float h;
    private float groundH;
    private float boomMaxH;
    private float boomMinH;
    Paint mPaint = new Paint();
    private int fireworkCount = 7;
    //    private int fireworkCount = 1;
    private List<Firework> list = new ArrayList<>();
    private float gravityEffect = 10;
    private boolean isShow = true;
    private boolean mShowFirework;
    private String TAG = FireworkDrawable.class.getSimpleName();

    public FireworkDrawable(float w, float h,int fireworkCount) {
        this.w = w;
        this.h = h;
        groundH = h / 8 * 7;
        boomMaxH = h / 4;
        boomMinH = h / 3;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.fireworkCount = fireworkCount;
        for (int i = 0; i < fireworkCount; i++) {
            list.add(createFirework());
        }

    }

    private Firework createFirework() {
        float startX = MathUtil.randomFloat(w / 8, w / 8 * 7);
        float startY = h / 8 * 7;
        float boomX = startX;
        float boomY = MathUtil.randomFloat(boomMaxH, boomMinH);
        PointF startPoint = new PointF(startX, startY);
        PointF boomPoint = new PointF(boomX, boomY);
        float initSpeed = (groundH - boomMaxH) / 0.8f;
        gravityEffect = initSpeed / 3;
        Firework firework = new Firework(startPoint, boomPoint, initSpeed, gravityEffect);
        return firework;
    }

//    private List<Firework> removeList = new ArrayList<>();

    @Override
    public void draw(@NonNull Canvas canvas) {
//        Log.i(TAG, "removeList.size: " + removeList.size());
//        removeList.clear();
//        for (Firework firework : list) {
//            if (firework.isBoom()) {
//                if (firework.getList().size() == 0) {
//                    removeList.add(firework);
//                }
//            } else {
//                if (firework.getRealPoint().y < h / 6 || firework.getRealPoint().y > groundH) {
//                    removeList.add(firework);
//                }
//            }
//        }

//        if (removeList.size() > 0) {
//            list.removeAll(removeList);
//            for (int i = 0; i < removeList.size(); i++) {
//                Firework firework = createFirework();
//                list.add(firework);
//            }
//        }

        if (list.size() > 0 && mShowFirework) {
            for (Firework firework : list) {
                if (!firework.isDispersed()) {
                    drawFirework(canvas, firework);
                    invalidateSelf();
                }
            }
        }
    }

    private void drawFirework(Canvas canvas, Firework firework) {
        if (firework.isBoom()) {
            List<Particle> removeList = new ArrayList<>();
            List<Particle> remainList = firework.getList();
            for (Particle particle : remainList) {
                int alpha = particle.getAlpha();
                if (alpha < 50) {
                    removeList.add(particle);
                }
            }
            ;
            remainList.removeAll(removeList);
            for (Particle particle : remainList) {
                particle.refresh();
                PointF trailStart = particle.getTrailStart();
                PointF realPoint = particle.getRealPoint();
                float initRadius = particle.getInitRadius();
                float moveAngle = particle.getMoveAngle();
                float verticalAngle = moveAngle + (float) Math.PI / 2;
                LinearGradient linearGradient = new LinearGradient(realPoint.x, realPoint.y, trailStart.x, trailStart.y,
                        Color.WHITE, particle.getInitColor(), Shader.TileMode.MIRROR);
                mPaint.setShader(linearGradient);
                mPaint.setStrokeWidth(2 * particle.getInitRadius() * 0.9f);
                mPaint.setColor(particle.getInitColor());
                Path path = new Path();
                path.moveTo(trailStart.x, trailStart.y);
                path.lineTo(realPoint.x + initRadius * (float) Math.cos(verticalAngle), realPoint.y + initRadius * (float) Math.sin(verticalAngle));
                path.lineTo(realPoint.x + initRadius * (float) Math.cos(verticalAngle + Math.PI), realPoint.y + initRadius * (float) Math.sin(verticalAngle + Math.PI));
                path.close();
                canvas.drawPath(path, mPaint);
                mPaint.setShader(null);
                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(0);
                canvas.drawCircle(realPoint.x, realPoint.y, initRadius, mPaint);
            }

            if (remainList.size() == 0) {
                firework.setDispersed(true);
                if (list.indexOf(firework) == list.size() - 1) {
                    Log.i(TAG, "drawFirework: 烟花散尽了啊----");
                    if (mOnDismissListener != null) {
                        mOnDismissListener.onDismiss(null);
                    }
                }
            }

        } else {
            firework.onRefresh();
            PointF pointF = firework.getRealPoint();
            RadialGradient radialGradient = new RadialGradient(
                    pointF.x, pointF.y, firework.getInitRadius() * 2,
                    new int[]{Color.WHITE, Color.WHITE, firework.getInitColor()}, new float[]{0f, 0.4f, 1f},
                    Shader.TileMode.MIRROR
            );
            mPaint.setShader(radialGradient);
            canvas.drawCircle(pointF.x, pointF.y, firework.getInitRadius() * 2, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) h;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) w;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    public boolean isShow() {
        return isShow;
    }

    public void showFirework() {
//        isShow = show;
        if (mShowFirework){
            list.clear();
        }
        if (!mShowFirework) {
            mShowFirework = true;
        }
        for (int i = 0; i < fireworkCount; i++) {
            list.add(createFirework());
        }
        invalidateSelf();
    }

    /**
     * Interface used to allow the creator of a Firework to run some code when the
     * Firework is dismissed.
     */
    public interface OnDismissListener {
        /**
         * This method will be invoked when the Firework is dismissed.
         *
         * @param fireworkDrawable the fireworkView that was dismissed will be passed into the
         *                         method
         */
        void onDismiss(FireworkDrawable fireworkDrawable);
    }

    private OnDismissListener mOnDismissListener;

    public void setOnDismissListener(OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    public void setFireworkCount(int fireworkCount) {
        this.fireworkCount = fireworkCount;
    }

    public void setShowFirework(boolean showFirework) {
        this.mShowFirework = showFirework;
    }
}

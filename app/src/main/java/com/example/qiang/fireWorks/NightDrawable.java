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
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NightDrawable extends Drawable {
    private float w;
    private float h;
    private float moonX;
    private float moonY;
    private float moonR;
    private float starR;
    private float textX;
    private float textY;
    private int moonCenterColor = Color.argb(255, 255, 249, 177);
    private int moonLightColor = Color.argb(0, 255, 249, 177);
    private int skyColor = Color.argb(255,0,31,86);

    Paint mPaint = new Paint();


    public NightDrawable(float w, float h) {
        this.w = w;
        this.h = h;
//        this.moonX = w / 4 * 3;
        this.moonX = w / 10 * 9;
//        this.moonY = h / 6;
        this.moonY = h / 16;
//        this.moonR = h / 20;
        this.moonR = h / 25;
        this.textX = w/2;
        this.textY = h/3*2;
        this.starR = moonR/3;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        drawNight(canvas);
        drawMoon(canvas);
        drawStars(canvas);
        //drawText(canvas);
    }

    private void drawMoon(Canvas canvas) {
        RadialGradient radialGradient = new RadialGradient(
                moonX, moonY, moonR,
                new int[]{moonCenterColor, moonCenterColor,moonLightColor}, new float[]{0f, 0.3f, 1f},
                Shader.TileMode.MIRROR
        );
        mPaint.setShader(radialGradient);
        canvas.drawCircle(moonX, moonY, moonR, mPaint);
    }

    private void drawNight(Canvas canvas) {
        LinearGradient linearGradient = new LinearGradient(
                w/2,0,w/2,h,
                new int[]{skyColor,skyColor,Color.BLACK},new float[]{0,0.2f,1f}, Shader.TileMode.MIRROR
        );
        mPaint.setShader(linearGradient);
        canvas.drawRect(0, 0, w, h, mPaint);
    }

    private void drawStars(Canvas canvas){
        PointF pointF1 = new PointF(w/4,h/9);
        PointF pointF2 = new PointF(w/3,h/5);
        PointF pointF3 = new PointF(w/2,h/9);
        drawStar(pointF1,MathUtil.randomFloat(starR/2,starR),canvas);
        drawStar(pointF2,MathUtil.randomFloat(starR/2,starR),canvas);
        drawStar(pointF3,MathUtil.randomFloat(starR/2,starR),canvas);
    }

    private void drawStar(PointF pointF,float scale,Canvas canvas){
        float centerX = pointF.x;
        float centerY = pointF.y;
        float saddleP = scale/8;
        float halfScale = scale/2;
        Path path = new Path();
        RadialGradient radialGradient = new RadialGradient(
                centerX, centerY, scale,
                new int[]{Color.WHITE, Color.WHITE,Color.TRANSPARENT}, new float[]{0f, 0.1f, 1f},
                Shader.TileMode.MIRROR
        );
        path.moveTo(centerX - halfScale,centerY);
        path.quadTo(centerX - saddleP,centerY - saddleP,centerX,centerY - scale);
        path.quadTo(centerX + saddleP,centerY - saddleP,centerX+halfScale,centerY);
        path.quadTo(centerX + saddleP,centerY + saddleP,centerX,centerY+scale);
        path.quadTo(centerX - saddleP,centerY + saddleP,centerX-halfScale,centerY);
        path.close();
        mPaint.setShader(radialGradient);
        canvas.drawPath(path,mPaint);
    }

    private void drawText(Canvas canvas){
        String text = "HAPPY NEW YEAR";
        mPaint.setStrokeWidth(105.8f);
        mPaint.setTextSize(w/10.58f);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setShader(null);
        RectF rectF = new RectF(textX - w/4,textY - w/4,textX + w/4,textY + w/4);
        Path path = new Path();
        path.arcTo(rectF,180,180);
        //canvas.drawPath(path,mPaint);
        canvas.drawTextOnPath(text,path,0,0,mPaint);
    }


    @Override
    public void setAlpha(int i) {

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

}

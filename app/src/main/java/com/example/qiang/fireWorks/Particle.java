package com.example.qiang.fireWorks;

import android.graphics.PointF;
import android.os.SystemClock;
import android.util.Log;

public class Particle {
    private int index;
    private int scaleMode;
    private int colorMode;
    private int initAngle;
    private int initColor;
    private PointF boomPoint;
    private PointF realPoint;
    private PointF trailStart;
    private int initAlpha;
    private int alpha;
    private float initRadius;
    private float radius;
    private float center;
    private float initSpeed;
    private int speed;
    private float alphaDecay;
    private float scaleDecay;
    private float gravityEffect;
    private long startTime;
    private float moveAngle;

    public Particle() {
    }

    public Particle(PointF boomPoint, int scaleMode, int initColor) {
        this.scaleMode = scaleMode;
        this.colorMode = colorMode;
        this.boomPoint = boomPoint;
        this.realPoint = new PointF();
        this.trailStart = new PointF();
        this.initColor = initColor;
        this.startTime = SystemClock.uptimeMillis();
        this.initAlpha = MathUtil.randomInt(200, 254);
        this.initAngle = MathUtil.randomInt(0, 360);
        this.alphaDecay = MathUtil.randomFloat(0.7f, 0.8f);
        this.scaleDecay = MathUtil.randomFloat(0.85f, 0.95f);
        this.gravityEffect = MathUtil.randomFloat(2f, 2.5f);
        this.alpha = initAlpha;
        switch (scaleMode) {
            case 1:
                this.initSpeed = MathUtil.randomFloat(0, Ratio.SCREEN_WIDTH / 6);
                this.initRadius = MathUtil.randomFloat(3, 5);
                break;
            case 2:
                this.initSpeed = MathUtil.randomFloat(0, Ratio.SCREEN_WIDTH / 5);
                this.initRadius = MathUtil.randomFloat(4, 7);
                break;
            case 3:
                this.initSpeed = MathUtil.randomFloat(0, Ratio.SCREEN_WIDTH / 4);
                this.initRadius = MathUtil.randomFloat(6, 10);
                break;
            default:
                this.initSpeed = MathUtil.randomFloat(0, Ratio.SCREEN_WIDTH / 4);
                this.initRadius = MathUtil.randomFloat(3, 5);
                break;
        }
    }

    private int refreshCount;

    public void refresh() {
        long currentMillis = SystemClock.uptimeMillis();
        long endDuration = currentMillis - startTime;
        long startDuration = endDuration / 2;
        float endSecondF = ((float) endDuration) / 500f;
        float startSecondF = ((float) startDuration) / 500f;
        alpha = (int) (initAlpha * (float) (Math.pow(alphaDecay, endSecondF)));
        radius = (int) (initRadius * (float) (Math.pow(scaleDecay, endSecondF)));
        speed = (int) (initSpeed * (float) (Math.pow(0.5, endSecondF)));
        trailStart = calculateByDuration(startSecondF);
        realPoint = calculateByDuration(endSecondF);
        float xOffset = realPoint.x - trailStart.x;
        float yOffset = realPoint.y - trailStart.y;
        if (xOffset == 0) {
            moveAngle = (float) Math.PI / 2;
        } else {
            moveAngle = (float) Math.atan(yOffset / xOffset);
        }
        refreshCount++;
        Log.i("refreshCount", "refreshCount: "+refreshCount);
    }

    private PointF calculateByDuration(float duration) {
        float gravityDisplacement = 0.5f * (float) (Math.pow(gravityEffect, duration));
        float boomDisplacement = initSpeed * (float) Math.pow(0.4, duration) / -0.9f + initSpeed / 0.9f;
        float x = boomPoint.x + (float) Math.cos(Math.PI * initAngle / 180) * boomDisplacement;
        float y = boomPoint.y + (float) Math.sin(Math.PI * initAngle / 180) * boomDisplacement + gravityDisplacement;
        PointF pointF = new PointF();
        pointF.x = x;
        pointF.y = y;
        return pointF;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public float getInitRadius() {
        return initRadius;
    }

    public float getRadius() {
        return radius;
    }

    public float getCenter() {
        return center;
    }

    public void setCenter(float center) {
        this.center = center;
    }

    public PointF getRealPoint() {
        return realPoint;
    }

    public PointF getTrailStart() {
        return trailStart;
    }

    public void setTrailStart(PointF trailStart) {
        this.trailStart = trailStart;
    }

    public int getInitColor() {
        return initColor;
    }

    public void setInitColor(int initColor) {
        this.initColor = initColor;
    }

    public float getMoveAngle() {
        return moveAngle;
    }

    public void setMoveAngle(float moveAngle) {
        this.moveAngle = moveAngle;
    }
}

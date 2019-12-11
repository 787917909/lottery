package com.example.qiang.fireWorks;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.SystemClock;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class Firework {
    private String TAG = Firework.class.getSimpleName();
    List<Particle> list = new ArrayList<>();
    private int scaleMode;
    private int colorMode;
    private boolean isBoom = false;
    private PointF realPoint;
    private PointF boomPoint;
    private PointF startPoint;
    private long startMillis;
    private float gravityEffect;
    private float initSpeed;
    private int initColor;
    private float initRadius;
    private boolean isDispersed;//散尽

    public boolean isDispersed() {
        return isDispersed;
    }

    public void setDispersed(boolean dispersed) {
        isDispersed = dispersed;
    }

    public Firework() {

    }

    public Firework(PointF startPoint, PointF boomPoint, float initSpeed, float gravityEffect) {
        this.startPoint = new PointF(startPoint.x, startPoint.y);
        this.realPoint = new PointF(startPoint.x, startPoint.y);
        this.boomPoint = boomPoint;
        this.initSpeed = initSpeed;
        this.gravityEffect = gravityEffect;
        this.startMillis = SystemClock.uptimeMillis();
        this.scaleMode = MathUtil.randomInt(1, 3);
        this.colorMode = MathUtil.randomInt(1, 3);

        switch (scaleMode) {
            case 1:
                this.initRadius = MathUtil.randomFloat(3, 5);
                break;
            case 2:
                this.initRadius = MathUtil.randomFloat(4, 7);
                break;
            case 3:
                this.initRadius = MathUtil.randomFloat(6, 10);
                break;
            default:
                this.initRadius = MathUtil.randomFloat(3, 5);
                break;
        }

        switch (colorMode) {
            case 1:
                this.initColor = Color.argb(255, 255, 50, 50);
                break;
            case 2:
                this.initColor = Color.argb(255, 50, 255, 50);
                break;
            case 3:
                this.initColor = Color.argb(255, 50, 50, 255);
                break;
            default:
                this.initColor = Color.argb(255, 255, 50, 50);
                break;
        }
    }

    private void onBoom() {
        isBoom = true;
        for (int i = 0; i < 120; i++) {
            list.add(new Particle(realPoint, scaleMode, initColor));
        }
        Log.i(TAG, "onBoom: ");
    }

    public void onRefresh() {
        if (isBoom) {
            return;
        }
        if (realPoint.y < boomPoint.y || realPoint.y - boomPoint.y < 10f) {
            onBoom();
            return;
        }
        long currentMillis = SystemClock.uptimeMillis();
        float duration = (float) (currentMillis - startMillis) / 1000f;
        realPoint.y = startPoint.y - (initSpeed * duration - 0.5f * gravityEffect * (float) Math.pow(duration, 2));
        Log.i(TAG, "onRefresh: ");
    }

    public List<Particle> getList() {
        return list;
    }

    public int getInitColor() {
        return initColor;
    }

    public float getInitRadius() {
        return initRadius;
    }

    public boolean isBoom() {
        return isBoom;
    }

    public PointF getRealPoint() {
        return realPoint;
    }

}

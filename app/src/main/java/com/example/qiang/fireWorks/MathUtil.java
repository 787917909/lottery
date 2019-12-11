package com.example.qiang.fireWorks;
public class MathUtil {
    public static float randomFloat(float min, float max) {
        return min + (float) Math.random() * (max - min);
    }
    public static int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}

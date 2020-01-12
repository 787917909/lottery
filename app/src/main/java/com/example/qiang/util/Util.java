package com.example.qiang.util;

import android.content.Context;
import android.widget.Toast;

public class Util {
//    static final String ip = "223.255.255.194";
    static final String ip = "47.98.152.199";

    static final String port = "8887";


//    public static final String ws = "ws://10.132.212.167:2444";//websocket测试地址

    public static final String ws = String.format("ws://%s:%s",ip,port);//websocket测试地址

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}

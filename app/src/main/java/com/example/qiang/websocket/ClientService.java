package com.example.qiang.websocket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.qiang.util.Util;

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class ClientService extends Service {
    private static final String TAG = "ClientService";

    private ClientBinder mBinder = new ClientBinder();
    public Client client;

    public class ClientBinder extends Binder{

    public ClientService getService(){
        return ClientService.this;
    }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            initSocketClient();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        closeConnect();
        super.onDestroy();
    }
    public void initSocketClient() throws URISyntaxException {
        URI uri = new URI(Util.ws);
        client = new Client(uri,new Draft_17()) {
            @Override
            public void onMessage(String message) {
                Log.e("JWebSocketClientService", "收到的消息：" + message);
                Intent intent = new Intent();
                intent.setAction("com.xch.servicecallback.content");
                intent.putExtra("message",message);
                sendBroadcast(intent);
//                checkLockAndShowNotification(message);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                Log.e("JWebSocketClientService", "websocket连接成功");
            }
        };
        connect();
    }
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        if (null != client) {
            Log.e("JWebSocketClientService", "发送的消息：" + msg);
            client.send(msg);
        }
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        Log.d(TAG, "closeConnect: ");
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }

}

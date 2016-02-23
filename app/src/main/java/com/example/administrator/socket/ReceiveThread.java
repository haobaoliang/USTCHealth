package com.example.administrator.socket;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by HBL on 2015/12/24.
 */
public class ReceiveThread extends Thread {
    private final static String TAG = ReceiveThread.class.getSimpleName();

    private InputStream inStream = null;

    TcpSocket receTcpSocket = TcpSocket.getInstance(); // 从主线程中传过来
    byte recvBuf[] = new byte[10000];
    private String str = null;
    public boolean isRece = true;

    @Override
    public void run() {
        Log.e(TcpSocket.TAG, "ReceiveThread run!!");
        while (isRece) {
            Log.e(TcpSocket.TAG, "start receive data!!");
            try {
                inStream = receTcpSocket.mSocket.getInputStream();
                inStream.read(recvBuf);
                str = new String(recvBuf, "GB2312").trim();
                Log.e(TcpSocket.TAG, "receive data finish!!");
                Log.e(TcpSocket.TAG, "data=" + str);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TcpSocket.TAG, "receive data failed!!");
            }
        }
    }


}

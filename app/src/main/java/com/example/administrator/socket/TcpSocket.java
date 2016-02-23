package com.example.administrator.socket;

import android.util.Log;


import com.example.administrator.datainterface.InterfaceLogin;
import com.example.administrator.datainterface.NetPack;
import com.example.administrator.utils.UseData;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


/**
 * Created by HBL on 2015/12/24.
 * 参考博客：http://blog.csdn.net/pkuyjxu/article/details/23941765
 */
public class TcpSocket {
    public final static String TAG = TcpSocket.class.getSimpleName();
    public Socket mSocket;
    public DataOutputStream mOutputStream;

    //静态工厂方法
    private static class LazyHolder {
        private static final TcpSocket INSTANCE = new TcpSocket();
    }

    public static TcpSocket getInstance() {
        return LazyHolder.INSTANCE;
    }

    public boolean initSocket() {
        Log.d(TAG, "started initSocket!!");
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(UseData.SERVER_IP);
            if (mSocket == null)
                mSocket = new Socket(addr, UseData.SERVER_PORT);
            Log.e(TAG, "initSocket success!");
            return true;
        } catch (IOException e) {
            Log.e(TAG, "initSocket failed!!");
            e.printStackTrace();
            return false;
        }
    }


    public void sendUserinfo(String username, byte[] password, int flag) {

        InterfaceLogin user = new InterfaceLogin(username, password, flag);
        NetPack p = new NetPack(-1, InterfaceLogin.size, UseData.LoginUP,
                user.getBuf());
        p.size = NetPack.infoSize + InterfaceLogin.size;
        p.CalCRC();
        sendPack(p);
        Log.e(TAG,"start send data!!");
      /*  byte[] msgBuffer = new byte[100];

        byte temp[];

        try {
            temp = username.getBytes("GBK");
            System.arraycopy(temp, 0, msgBuffer, 0, temp.length);
            temp = password.getBytes("GBK");
            System.arraycopy(temp, 0, msgBuffer, 20, temp.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            mOutputStream = new DataOutputStream(mSocket.getOutputStream());
            mOutputStream.write(msgBuffer);
            Log.e(TAG, "send success!!");
        } catch (IOException e) {
            Log.e(TAG, "mOutputStream init failed!!");
            e.printStackTrace();
        }*/
    }

    // 发送数据包sendPack
    public boolean sendPack(NetPack data) {
        int flag = data.getM_Start();
        // 循环发送

        int retVal;

        retVal = sendMsg(mSocket, data.getBuf(), data.size, flag);
        if (retVal != 1) {
            Log.e(TAG, "SendPack: " + retVal);
            return false;
        }
        Log.e(TAG, "Yes__SendPack: " + retVal);
        return true;

    }

    private int sendMsg(Socket socket, byte[] data, int len, int flag) {
        int resultNumber = 1;
        if (socket == null) {
            Log.e(TAG, "SendMsg: +ErrorNo.SOCKET_NULL");
            return 0;
        }
        if (data == null) {
            Log.e(TAG, "SendMsg: +ErrorNo.DATA_NULL");
            return 0;
        }
        if (len < 0) {
            Log.e(TAG, "SendMsg: +ErrorNo.MINUSVALUE");
            return 0;
        }

        int available = 0;
        try {
            /*if ((available = socket.getInputStream().available()) >= 0) {
                socket.getInputStream().skip(available);
            }
*/
            socket.getOutputStream().write(data);
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "SendMsg: " + resultNumber);
        Log.e(TAG, "len----------------->: " + len);
        return resultNumber;
    }
}

package com.example.administrator.datainterface;

import com.example.administrator.utils.DataToData;

import java.io.UnsupportedEncodingException;

/**
 * Created by HBL on 2015/12/25.
 */
public class InterfaceLogin {
    private String username; // char[20];
    private byte[] password = new byte[20]; // char[20];
    private int flag;
    //数据包长度
    public static int size = 20 + 20 + 4;
    private byte[] buf = new byte[size];

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] key) {
        this.password = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InterfaceLogin() {
        username = "";
        for (int i = 0; i < password.length; i++) {
            password[i] = 0;
        }
    }

    // 构造并转化
    public InterfaceLogin(String username, byte[] password, int flag) {
        this.username = username;
        this.password = password;
        this.flag = flag;
        byte temp[];
        try {
            // username
            temp = username.getBytes("GBK");
            System.arraycopy(temp, 0, buf, 0, temp.length);
            // password
            temp = password;
            System.arraycopy(temp, 0, buf, 20, temp.length);
            // flag
            temp = DataToData.intTobyte(flag);
            System.arraycopy(temp, 0, buf, 40, temp.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // byte数组转化为类对象
    public static InterfaceLogin getInterfaceLoginInfo(byte[] buf) {
        String username = "";
        byte password[] = new byte[20];
        int flag = 0;

        try {
            // username
            byte[] tempStr20 = new byte[20];
            System.arraycopy(buf, 0, tempStr20, 0, 20);
            username = new String(tempStr20, "GBK");
            // password
            System.arraycopy(buf, 20, password, 0, 20);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
// flag
        byte[] temp = new byte[4];
        System.arraycopy(buf, 40, temp, 0, 4);
        flag = DataToData.byteToint(temp);
        return new InterfaceLogin(username, password,flag);
    }

    // 返回要发送的byte数组
    public byte[] getBuf() {
        return buf;
    }
}

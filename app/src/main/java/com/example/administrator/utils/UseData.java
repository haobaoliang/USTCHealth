package com.example.administrator.utils;

/**
 * Created by HBL on 2015/12/25.
 */
public class UseData {

    //服务器IP地址与端口号
    public static final int SERVER_PORT = 1002;
    public static final String SERVER_IP = "172.20.24.1"; // "114.214.166.134";
    // 数据包相关
    public static final String AES_KEY = "Wsn406";
    public static final int MAX_PACKBUFFER_SIZE = 8 * 1024; // 包体大小
    public static final int PACK_START_FLAG = 0x9202; // 包头标志

    // 登录相关
    public static final int LoginUP = 0x0800; // 登陆信息 密码和账号
    public static final int USER_LOGIN_FLAG = 0x0739; // 初始页面登录


}

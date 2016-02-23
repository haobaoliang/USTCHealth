package com.example.administrator.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.register.RegisterActivity;
import com.example.administrator.socket.ReceiveThread;
import com.example.administrator.socket.TcpSocket;
import com.example.administrator.ustc_health.R;
import com.example.administrator.utils.CRC4;
import com.example.administrator.utils.CStrictMode;
import com.example.administrator.utils.UseData;


/**
 * Created by HBL on 2015/11/28.
 */
public class LoginActivity extends Activity {

    private final static String TAG = LoginActivity.class.getSimpleName();
    Button login_button, sign_button;
    InputMethodLayout layout01;
    TextView textView;
    EditText login_email, login_password;
    FrameLayout.LayoutParams layoutParams;
    LinearLayout linearLayout;
    FrameLayout frameLayout;
    //发送socket
    TcpSocket login_Socket;
    //接受数据线程
    ReceiveThread login_Receive;
    public static Handler loginHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置严厉模式
        CStrictMode.strictMode();

        login_Socket = TcpSocket.getInstance();

        initView();

        initEvent();

        changeLayout();

    }

    public void changeLayout() {
        //软键盘弹出时改变布局
        layout01.setOnkeyboarddStateListener(new InputMethodLayout.onKeyboardsChangeListener() {// 监听软键盘状态

            @Override
            public void onKeyBoardStateChange(int state) {
                // TODO Auto-generated method stub
                switch (state) {
                    case InputMethodLayout.KEYBOARD_STATE_SHOW:
                        login_button.setVisibility(View.INVISIBLE);
                        frameLayout.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case InputMethodLayout.KEYBOARD_STATE_HIDE:
                        linearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        login_button.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void initView() {
        linearLayout = (LinearLayout) findViewById(R.id.layout_lin01);
        frameLayout = (FrameLayout) findViewById(R.id.layout_fra01);
        login_button = (Button) findViewById(R.id.login_button);
        sign_button = (Button) findViewById(R.id.login_sign);
        layout01 = (InputMethodLayout) findViewById(R.id.layout_login01);
        textView = (TextView) findViewById(R.id.layout_textview01);
        layoutParams = (FrameLayout.LayoutParams) textView.getLayoutParams();
        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_pass);
    }

    private void initEvent() {
        sign_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    public boolean login() {
        // 初始化socket
        login_Socket.initSocket();

        // 启动接收线程
        if (login_Receive == null) {
            Log.e(TcpSocket.TAG, "ReceiveThread new!!");
            login_Receive = new ReceiveThread();
            login_Receive.start();
        }
        String username = login_email.getText().toString().trim();
        String pwd = login_password.getText().toString().trim();

        // 用户名和密码为空
        if (username.length() == 0 || pwd.length() == 0) {
            Toast toast = Toast.makeText(this, "请输入账号或密码", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            // 密码加密
            String str = pwd;
            byte crcPwd[] = str.getBytes();
            for (int i = 0; i < crcPwd.length; i++)
                crcPwd[i] = 0;
            byte strb[] = str.getBytes();
            for (int i = 0; i < str.length(); i++) {
                crcPwd[i] = strb[i];
            }
            CRC4 crc = new CRC4();
            byte b[] = UseData.AES_KEY.getBytes();
            crc.Encrypt(crcPwd, b);
          //  login_Socket.sendUserinfo(username,crcPwd,UseData.USER_LOGIN_FLAG);
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (login_Receive != null) {
            login_Receive.interrupt();
        }
    }


}

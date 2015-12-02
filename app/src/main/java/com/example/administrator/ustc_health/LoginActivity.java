package com.example.administrator.ustc_health;

import android.app.Activity;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.FrameLayout;

import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * Created by HBL on 2015/11/28.
 */
public class LoginActivity extends Activity {


    Button login_button;
    private InputMethodLayout layout01;

    TextView textView;
    FrameLayout.LayoutParams layoutParams;
    LinearLayout linearLayout;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linearLayout = (LinearLayout) findViewById(R.id.layout_lin01);
        frameLayout = (FrameLayout) findViewById(R.id.layout_fra01);
        login_button = (Button) findViewById(R.id.login_button);
        layout01 = (InputMethodLayout) findViewById(R.id.layout_login01);
        textView = (TextView) findViewById(R.id.layout_textview01);
        layoutParams = (FrameLayout.LayoutParams) textView.getLayoutParams();
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


}

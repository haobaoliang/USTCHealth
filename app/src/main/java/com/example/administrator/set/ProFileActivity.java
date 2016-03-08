package com.example.administrator.set;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.administrator.ble.DeviceListActivity;
import com.example.administrator.ustc_health.R;

/**
 * Created by HBL on 2016/2/23.
 */
public class ProFileActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearLayout, ly_profile_myweight, ly_profile_sport,ly_profile_mydevice;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("个人资料");
        linearLayout = (LinearLayout) findViewById(R.id.ly_profile_my);
        linearLayout.setOnClickListener(this);
        ly_profile_myweight = (LinearLayout) findViewById(R.id.ly_profile_myweight);
        ly_profile_myweight.setOnClickListener(this);
        ly_profile_sport = (LinearLayout) findViewById(R.id.ly_profile_sport);
        ly_profile_sport.setOnClickListener(this);
        ly_profile_mydevice=(LinearLayout)findViewById(R.id.ly_profile_mydevice);
        ly_profile_mydevice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ly_profile_my:
                Intent intent = new Intent(ProFileActivity.this, MyProFileActivity.class);
                startActivity(intent);
                break;
            case R.id.ly_profile_mydevice:
                intent=new Intent(ProFileActivity.this, DeviceListActivity.class);
                startActivity(intent);
                break;
            case R.id.ly_profile_myweight:
                break;
            case R.id.ly_profile_sport:
                AlertDialog.Builder customDia=new AlertDialog.Builder(ProFileActivity.this);
                customDia.setIcon(R.mipmap.ic_mode_edit_black);
                final View viewDia= LayoutInflater.from(ProFileActivity.this).inflate(R.layout.activity_custom_dialog_sport, null);
                customDia.setTitle("请输入运动目标");
                customDia.setView(viewDia);
                customDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                customDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                customDia.create().show();
                break;
        }
    }
}

package com.example.administrator.set;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.ble.BluetoothLeService;
import com.example.administrator.ble.DeviceControlService;
import com.example.administrator.ble.SampleGattAttributes;
import com.example.administrator.ustc_health.R;

import java.util.UUID;

/**
 * Created by Administrator on 2015/10/28.
 */
public class SetActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout linearLayout;
    BluetoothLeService mBluetoothLeService=DeviceControlService.mBluetoothLeService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.set_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("个人设置");

        linearLayout=(LinearLayout)findViewById(R.id.ly_setting_th);
        linearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ly_setting_th:
                mBluetoothLeService.readMessage(UUID.fromString(SampleGattAttributes.POWER));//
                break;

            default:
                break;
        }

    }
}

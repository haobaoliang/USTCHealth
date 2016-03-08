package com.example.administrator.set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ble.BluetoothLeService;
import com.example.administrator.ble.DeviceControlService;
import com.example.administrator.ble.SampleGattAttributes;
import com.example.administrator.ustc_health.MainActivity;
import com.example.administrator.ustc_health.R;
import com.example.administrator.utils.ComFormu;
import com.example.administrator.utils.DataToData;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Administrator on 2015/10/28.
 */

public class SetActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    LinearLayout lv_setting_power, lv_setting_xinlv, ly_setting_time, ly_setting_testxinlv;
    BluetoothLeService mBluetoothLeService = DeviceControlService.mBluetoothLeService;
    TextView tv_setting_power, tv_setting_xinlv;
    byte[] p = new byte[4];//这个用来int转换byte的暂存数组
    final byte[] test = new byte[2];

    private final static String TAG = SetActivity.class
            .getSimpleName();
    SwitchCompat switchCompat;
    boolean istestxinlv = false, istestxinlv_open = false, istestxinlv_close = false;

    boolean checkflag2=false;
    boolean checkflag3=false;
//Button a= (Button) findViewById(R.id.button);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.set_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("个人设置");
        lv_setting_power = (LinearLayout) findViewById(R.id.ly_setting_power);
        lv_setting_power.setOnClickListener(this);
        lv_setting_xinlv = (LinearLayout) findViewById(R.id.ly_setting_xinlv);
        lv_setting_xinlv.setOnClickListener(this);
        ly_setting_time = (LinearLayout) findViewById(R.id.ly_setting_time);
        ly_setting_time.setOnClickListener(this);
        ly_setting_testxinlv = (LinearLayout) findViewById(R.id.ly_setting_testxinlv);
        ly_setting_testxinlv.setOnClickListener(this);

        tv_setting_power = (TextView) findViewById(R.id.tv_setting_power);
        tv_setting_xinlv = (TextView) findViewById(R.id.tv_setting_xinlv);

        switchCompat = (SwitchCompat) findViewById(R.id
                .sc_settin_testxinlv);
        switchCompat.setSwitchPadding(40);
        switchCompat.setOnCheckedChangeListener(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        String device_power = sharedPreferences.getString("DEVICE_POWER", "未检测");
        tv_setting_power.setText(device_power);
        tv_setting_xinlv.setText(sharedPreferences.getString("DEVICE_HEART_PERIOD", "2次/时"));

     /**   if ("false".equals(sharedPreferences.getString("DEVICE_HEART", "false"))) {
      switchCompat.setChecked(false);
      } else
      switchCompat.setChecked(true);
      */
        if (SampleGattAttributes.checkflag==false) {
            switchCompat.setChecked(false);
            System.out.print(SampleGattAttributes.checkflag);} else{
            switchCompat.setChecked(true);
        System.out.print(SampleGattAttributes.checkflag);}

        if (!mBluetoothLeService.initialize()) {//引用服务类的初始化
        }
        mBluetoothLeService.disconnect();
        //连接服务
        if (!mBluetoothLeService.connect(MainActivity.mDeviceAddress)) {
            Toast.makeText(SetActivity.this, "没连接上", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(SetActivity.this, "已经连接", Toast.LENGTH_LONG).show();
        }
        //注册广播接收器
        registerReceiver(mGattUpdateReceiver01, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothLeService.disconnect();
        unregisterReceiver(mGattUpdateReceiver01);
        istestxinlv = false;
        istestxinlv_close = false;
        istestxinlv_open = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_setting_power:
                istestxinlv = false;
                mBluetoothLeService.readMessage(UUID.fromString(SampleGattAttributes.POWER));//
                break;
            case R.id.ly_setting_xinlv:
                istestxinlv = false;
                xinlv();
                break;
            case R.id.ly_setting_time:
                istestxinlv = false;
                timeSyn();
                Toast.makeText(SetActivity.this, "时间同步", Toast.LENGTH_LONG).show();
                break;
            case R.id.ly_setting_testxinlv:

            default:
                break;
        }
    }

    //选择测心率的周期
    public void xinlv() {
        final String[] mList = {"2时/次", "1次/时", "2次/时", "5次/时", "1次/时", "20次/时"};

        AlertDialog.Builder sinChosDia = new AlertDialog.Builder(SetActivity.this);
        sinChosDia.setTitle("选择测心率的周期");
        sinChosDia.setSingleChoiceItems(mList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                int measuretimes = 12;
                switch (which) {
                    case 0:
                        measuretimes = 12;
                        break;
                    case 1:
                        measuretimes = 24;
                        break;
                    case 2:
                        measuretimes = 48;
                        break;
                    case 3:
                        measuretimes = 120;
                        break;
                    case 4:
                        measuretimes = 240;
                        break;
                    case 5:
                        measuretimes = 480;
                        break;
                }
                SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("DEVICE_HEART_PERIOD", mList[which]);
                editor.commit();
                tv_setting_xinlv.setText(mList[which]);
                SampleGattAttributes.trim(p, measuretimes);
                byte[] data = {p[3], p[2]};
                mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.HEARTRATEPERIODRW), data);////写入
                dialog.dismiss();
            }
        });
        sinChosDia.show();
    }

    //时间同步
    public void timeSyn() {
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        //int mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);
        int msecond = c.get(Calendar.SECOND);
        SampleGattAttributes.trim(p, mYear);
        System.out.println("年" + mYear + "月份" + mMonth + "时" + mHour + "分" + mMinute + "秒" + msecond);
        byte[] data = {(byte) msecond, (byte) mMinute, (byte) mHour, (byte) mDay,
                (byte) mMonth, p[3], p[2]};
        mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.TIMEADJUSTRW), data);//
        //

    }

    //自定义广播
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.UpData);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_WIRTE_SUCCESS);
        return intentFilter;
    }

    //定义一个广播接收器
    private final BroadcastReceiver mGattUpdateReceiver01 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            //已经连接上BLE
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
            } else if (BluetoothLeService.ACTION_GATT_WIRTE_SUCCESS.equals(action)) {
                if (istestxinlv) {
                    if (istestxinlv_open) {
                        Toast.makeText(SetActivity.this, "打开成功！", Toast.LENGTH_LONG).show();
                    SampleGattAttributes.checkflag=true;
                        checkflag2=true;//告诉listener不是人操作
                        switchCompat.setChecked(true);
                    }

                    if (istestxinlv_close) {
                        Toast.makeText(SetActivity.this, "关闭成功！", Toast.LENGTH_LONG).show();
                        SampleGattAttributes.checkflag=false;
                        checkflag2=true;
                        switchCompat.setChecked(false);
                    }
                } else {
                    Toast.makeText(SetActivity.this, "修改成功！", Toast.LENGTH_LONG).show();
                }
            }
            //没有连接上BLE
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
                    .equals(action)) {

            } else if (BluetoothLeService.UpData.equals(action)) {

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                if (intent.getStringExtra("DataType").equals("SevenDayData")) {

                } else if (intent.getStringExtra("DataType").equals("CollectPeriod")) {

                } else if (intent.getStringExtra("DataType").equals("Power")) {
                    Toast.makeText(SetActivity.this, "电量查询：" + SampleGattAttributes.dataGetter(intent.getByteArrayExtra("data"), 0, 1) + "%", Toast.LENGTH_LONG).show();
                    tv_setting_power.setText(SampleGattAttributes.dataGetter(intent.getByteArrayExtra("data"), 0, 1) + "%");
                    SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("DEVICE_POWER", SampleGattAttributes.dataGetter(intent.getByteArrayExtra("data"), 0, 1) + "%");
                    editor.commit();
                } else if (intent.getStringExtra("DataType").equals("TimeAdjust")) {
                }
            }
        }
    };
public void close(View view){istestxinlv_close = true;
    istestxinlv_open = false;
    test[1] = 0x55;
    test[0] = 0x02;
    switchCompat.setChecked(true);
    mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
};
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        istestxinlv = true;
        switch (compoundButton.getId()) {
            case R.id.sc_settin_testxinlv:
                if (b) {
                    if(checkflag2==true)
                    {
                        System.out.println("llcheckflag2=false;");    checkflag2=false;
                        return;
                    }

                    if(SampleGattAttributes.checkflag==true)
                    {
                        System.out.println("ll00");

                        return;
                    }

                    istestxinlv_open = true;

                    istestxinlv_close = false;
                    //checkflag3=true;
                    test[1] = 0x55;
                    test[0] = 0x01;
                    switchCompat.setChecked(false);
                    System.out.println("ll开始测试5");
                             mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);//
                    System.out.println("ll开始测试");
                    Log.d(TAG, "连续测试的开始");

                /**    SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("DEVICE_HEART", "true");
                    editor.commit();*/
                    //  switchCompat.setChecked(false);
                } else {System.out.println("到else了");
                    if(checkflag2==true)
                    {System.out.println("ll1");
                        checkflag2=false;
                        return;
                    }
                    if(checkflag3==true)
                    {
                        checkflag3=false;//还原
                        System.out.println("ll2");
                            return;
                    }
                    System.out.println("llyaoguanbi");

                      //  checkflag3=true;
                    istestxinlv_close = true;
                    istestxinlv_open = false;
                    test[1] = 0x55;
                    test[0] = 0x02;
                    switchCompat.setChecked(true);
                    System.out.println("不错啊");
                    mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);
                    Log.d(TAG, "连续测试的关闭");
                 //   SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
                   // SharedPreferences.Editor editor = sharedPreferences.edit();
                    //editor.putString("DEVICE_HEART", "false");
                    //editor.commit();
                    // switchCompat.setChecked(true);
                }
                break;
        }
    }
}

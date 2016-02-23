package com.example.administrator.ustc_health;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ble.BluetoothLeService;
import com.example.administrator.database.DBManager;
import com.example.administrator.utils.ComFormu;
import com.example.administrator.utils.DataToData;
import com.example.administrator.view.ChartView;
import com.example.administrator.view.CircleProgressBar;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/10/22.
 */
public class MyInforFragment extends Fragment implements View.OnClickListener {
    private TextView textView_01, textView_02, textView_03, textView_04;
    byte[] scan;
    public static String str01 = "0", str02 = "0", str03 = "0.00";
    LinearLayout ly_heartrate, ly_stepnum, ly_kcal,ly_amount;
    //private PopupWindow popupWindow;

    // private ChartView mChartView;
    ImageView imageView;
    //数据库
    DBManager dbManager;
    private final static String TAG = MyInforFragment.class
            .getSimpleName();

    public static MyInforFragment newInstance() {
        MyInforFragment f = new MyInforFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_myinfor, container, false);
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        //打开数据库
        dbManager = DBManager.getInstance(getContext());
        dbManager.openDatabase();
        dbManager.queryNewBleData();
        ly_heartrate = (LinearLayout) view.findViewById(R.id.ly_myinfo_heartrate);
        ly_stepnum = (LinearLayout) view.findViewById(R.id.ly_myinfo_stepnum);
        ly_kcal = (LinearLayout) view.findViewById(R.id.ly_myinfo_kcal);
        ly_amount=(LinearLayout)view.findViewById(R.id.ly_myinfo_amnt);

        ly_stepnum.setOnClickListener(this);
        ly_heartrate.setOnClickListener(this);
        ly_kcal.setOnClickListener(this);
        ly_amount.setOnClickListener(this);

        textView_01 = (TextView) view.findViewById(R.id.myinfo_textView_01);
        textView_02 = (TextView) view.findViewById(R.id.myinfo_textView_02);
        textView_03 = (TextView) view.findViewById(R.id.myinfo_textView_03);
        textView_04 = (TextView) view.findViewById(R.id.myinfo_textView_04);
        str01 = MainActivity.str01;
        str02 = MainActivity.str02;

        textView_01.setText(str01);
        textView_02.setText(str02);
        DecimalFormat df = new DecimalFormat("###.00");
        str03 = df.format(ComFormu.getKcal(Integer.valueOf(str02))).toString();
        textView_03.setText(str03);

        textView_04.setText("0");
        MyhealthActivity.steunumDetail();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadcastManager.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    //自定义广播
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.UpData);
        return intentFilter;
    }

    //定义一个广播接收器
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            //已经连接上BLE
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

            }
            //没有连接上BLE
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
                    .equals(action)) {

            } else if (BluetoothLeService.UpData.equals(action)) {
                scan = intent.getByteArrayExtra("data");
                //  System.out.print("~~~~");
                str01 = String.valueOf(scan[19] & 0xff);
                str02 = String.valueOf((scan[26] & 0xff) * 256 + (scan[25] & 0xff));

                DecimalFormat df = new DecimalFormat("###.00");
                str03 = df.format(ComFormu.getKcal(Integer.valueOf(str02))).toString();
                MainActivity.str01 = str01;
                MainActivity.str02 = str02;
                textView_01.setText(str01);
                textView_02.setText(str02);
                textView_03.setText(str03);
                dbManager.insertBleData(Integer.valueOf(str02), Integer.valueOf(str01), DataToData.getCurrentTime());
                Log.d(TAG, "insert success!");
                dbManager.query();

                Log.d(TAG, "query success!");
              /*  int i;
                for (i = 0; i <= 31; i++)
                    Log.e(TAG, Integer.toHexString(scan[i] & 0x000000ff) + "");  */  // Sets up UI references.*/


            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //这块进行分类解析
                /**数据头//UINT几 是INT型占几位的新结构类型
                 0x336699DB(4Bytes)+年（2Bytes）+月（1Byte）+日（1Byte）
                 +步数（2Bytes）+心率数据（10Bytes）           */

                if (intent.getStringExtra("DataType").equals("SevenDayData")) {


                } else if (intent.getStringExtra("DataType").equals("CollectPeriod")) {

                } else if (intent.getStringExtra("DataType").equals("Power")) {

                } else if (intent.getStringExtra("DataType").equals("TimeAdjust")) {

                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // dbManager.closeDatabase();
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.ly_myinfo_heartrate:
                Toast.makeText(getActivity(), "This is 心率", Toast.LENGTH_LONG).show();
                MyhealthActivity.heartrateDetail();
                break;
            case R.id.ly_myinfo_stepnum:
                Toast.makeText(getActivity(), "This is 计步", Toast.LENGTH_LONG).show();
                MyhealthActivity.steunumDetail();
                break;
            case R.id.ly_myinfo_kcal:
                Toast.makeText(getActivity(), "This is 卡路里", Toast.LENGTH_LONG).show();
                MyhealthActivity.kcalDetail();
                break;
            case R.id.ly_myinfo_amnt:
                Toast.makeText(getActivity(), "This is 运动量", Toast.LENGTH_LONG).show();
                MyhealthActivity.amountDetail();
                break;
            default:
                break;
        }
    }
}

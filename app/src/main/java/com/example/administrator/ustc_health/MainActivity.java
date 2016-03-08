package com.example.administrator.ustc_health;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.ble.DeviceControlService;
import com.example.administrator.ble.ScanBleActivity;
import com.example.administrator.file.FileActivity;
import com.example.administrator.friends.MyFriendsActivity;
import com.example.administrator.login.LoginActivity;
import com.example.administrator.set.ProFileActivity;
import com.example.administrator.set.SetActivity;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout menu_my;
    private LinearLayout menu_file;
    private LinearLayout menu_friends;

    private ImageView iv_my;
    private ImageView iv_file;
    private ImageView iv_friends;

    private TextView tv_my;
    private TextView tv_file;
    private TextView tv_friends;

    private Fragment myFragment;
    private Fragment fileFragment;
    private Fragment friendsFragment;

    private final static String TAG = MainActivity.class
            .getSimpleName();
    byte[] scan;
    public static String mDeviceName;
    public static String mDeviceAddress;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    //BLe控制服务
    private ServiceConnection serCon;
    DeviceControlService devConService;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    public static String str01 = "0", str02 = "0";

    private static TextView tv_toolbar_state;
    CircleImageView myImage;
    static ImageView iv_toolbar_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //判断是否支持ble
        initBle();
        final Intent intent = getIntent();
        if (intent != null) {
            mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
            mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
            scan = intent.getByteArrayExtra("scan");
            if (scan != null) {
                str01 = String.valueOf(scan[19] & 0xff);
                str02 = String.valueOf((scan[26] & 0xff) * 256 + (scan[25] & 0xff));
            }
            Log.d(TAG, "EXTRAS_DEVICE_NAME:" + mDeviceName + " EXTRAS_DEVICE_ADDRESS:" + mDeviceAddress);
        }
        //绑定服务
        initService();
        // 初始化控件
        initView();
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        initFragment(0);
        //检查程序是否为第一次运行
        if (checkIsFirstRun()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.mipmap.ic_warning_amber);
            builder.setTitle("第一次运行");
            builder.setMessage("此设备为第一次运行，是否扫描BLE？");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intDet = new Intent();
                    intDet.setClass(MainActivity.this, ScanBleActivity.class);
                    startActivity(intDet);
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "不扫描", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();

        } else {
            SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
            String device_name = sharedPreferences.getString("DEVICE_NAME", "");
            Log.d(TAG, device_name);
            if (device_name.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.mipmap.ic_warning_amber);
                builder.setTitle("是否扫描绑定BLE");
                builder.setMessage("此设备还没绑定BLE，是否前去绑定？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intDet = new Intent();
                        intDet.setClass(MainActivity.this, ScanBleActivity.class);
                        startActivity(intDet);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "不绑定", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            } else {
                mDeviceAddress = device_name;
            }
        }
    }

    //检查程序是否为第一次运行
    private boolean checkIsFirstRun() {
        // 通过检查程序中的缓存文件判断程序是否是第一次运行
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.d(TAG, "第一次运行");
            //   Toast.makeText(MainActivity.this, "第一次运行！", Toast.LENGTH_LONG).show();
            editor.putBoolean("isFirstRun", false);
            editor.commit();

        } else {
            Log.d(TAG, "不是第一次运行！");
            //   Toast.makeText(MainActivity.this, "不是第一次运行！", Toast.LENGTH_LONG).show();
        }

        return isFirstRun;
    }

    private void initService() {
        serCon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                devConService = ((DeviceControlService.LocalBinder) service).getService();
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        Intent devConServiceIntent = new Intent(this, DeviceControlService.class);
        boolean isbind = bindService(devConServiceIntent, serCon,
                BIND_AUTO_CREATE);//绑定
        if (isbind) {
            Log.d(TAG, "bindService success!!");
        } else {
            Log.d(TAG, "bindService failed!!");
        }
    }
    //是否支持BLE
    private boolean initBle() {
        //1.android:required="false",判断系统是否支持BLE
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "此设备不支持BLE", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.mipmap.ic_warning_amber);
            builder.setTitle("退出对话框");
            builder.setMessage("此设备不支持BLE，是否退出？");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "此设备支持BLE", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "此设备不支持BLE", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }
        //2.获取BluetoothAdapter
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //3.判断是否支持蓝牙，并打开蓝牙
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return true;
        }
        return false;
    }

    private void initView() {
        //Toolbar初始化
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 底部菜单5个Linearlayout
        menu_my = (LinearLayout) findViewById(R.id.ly_my);
        menu_file = (LinearLayout) findViewById(R.id.ly_file);
        menu_friends = (LinearLayout) findViewById(R.id.ly_friends);

        // 底部菜单5个ImageView
        iv_my = (ImageView) findViewById(R.id.iv_my);
        iv_file = (ImageView) findViewById(R.id.iv_file);
        iv_friends = (ImageView) findViewById(R.id.iv_friends);

        // 底部菜单5个菜单标题
        tv_my = (TextView) findViewById(R.id.tv_my);
        tv_file = (TextView) findViewById(R.id.tv_file);
        tv_friends = (TextView) findViewById(R.id.tv_friends);

        tv_toolbar_state = (TextView) findViewById(R.id.tv_toolbar_state);
        myImage = (CircleImageView) findViewById(R.id.iv_toolbar_my);
        iv_toolbar_refresh = (ImageView) findViewById(R.id.iv_toolbar_refresh);
        iv_toolbar_refresh.setVisibility(View.GONE);
    }

    // 设置按钮监听
    private void initEvent() {

        menu_my.setOnClickListener(this);
        menu_friends.setOnClickListener(this);
        menu_file.setOnClickListener(this);
        myImage.setOnClickListener(this);
        iv_toolbar_refresh.setOnClickListener(this);

    }

    private void initFragment(int index) {
        // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (myFragment == null) {
                    myFragment = new MyhealthActivity();
                    transaction.add(R.id.frame_content, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;
            case 3:
                if (fileFragment == null) {
                    fileFragment = new FileActivity();
                    transaction.add(R.id.frame_content, fileFragment);
                } else {
                    transaction.show(fileFragment);
                }
                break;
            case 4:
                if (friendsFragment == null) {
                    friendsFragment = new MyFriendsActivity();
                    transaction.add(R.id.frame_content, friendsFragment);
                } else {
                    transaction.show(friendsFragment);
                }
                break;
            default:
                break;
        }

        // 提交事务
        transaction.commit();

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
        if (friendsFragment != null) {
            transaction.hide(friendsFragment);
        }
        if (fileFragment != null) {
            transaction.hide(fileFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetActivity.class);
                startActivity(intent);
                break;

            case R.id.scan:
                intent = new Intent();
                intent.setClass(MainActivity.this, ScanBleActivity.class);
                startActivity(intent);
                break;

            case R.id.reg:
                intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //重置按钮颜色
    private void restartBotton() {
        // ImageView置为灰色
        iv_my.setImageResource(R.mipmap.me1);
        iv_file.setImageResource(R.mipmap.find1);
        iv_friends.setImageResource(R.mipmap.contact1);

        // TextView置为灰色
        tv_my.setTextColor(0xffA6A6A6);
        tv_file.setTextColor(0xffA6A6A6);
        tv_friends.setTextColor(0xffA6A6A6);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ly_my:
                restartBotton();
                iv_my.setImageResource(R.mipmap.me);
                tv_my.setTextColor(0xff008000);
                initFragment(0);
                break;
            case R.id.ly_file:
                restartBotton();
                iv_file.setImageResource(R.mipmap.find);
                tv_file.setTextColor(0xff008000);
                initFragment(3);
                break;
            case R.id.ly_friends:
                restartBotton();
                iv_friends.setImageResource(R.mipmap.contact);
                tv_friends.setTextColor(0xff008000);
                initFragment(4);
                break;
            case R.id.iv_toolbar_my:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProFileActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serCon);//解除绑定，否则会报异常
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    //service通知Activity扫描
    public static class MyBroadcastReceiver01 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Toast.makeText(context, "已经连接到设备", Toast.LENGTH_SHORT).show();
            tv_toolbar_state.setText("已连接");
            iv_toolbar_refresh.setVisibility(View.GONE);

        }
    }
    public static class MyBroadcastReceiver02 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Toast.makeText(context, "已经连接到设备", Toast.LENGTH_SHORT).show();
            tv_toolbar_state.setText("未连接");
            //iv_toolbar_refresh.setVisibility(View.VISIBLE);
            iv_toolbar_refresh.setVisibility(View.GONE);

        }
    }

}




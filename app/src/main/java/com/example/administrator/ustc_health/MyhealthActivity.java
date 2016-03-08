package com.example.administrator.ustc_health;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.baidumap.MapActivity;

import com.example.administrator.database.DBManager;
import com.example.administrator.viewfrag.DayAmountFragActivity;
import com.example.administrator.viewfrag.DayHeartFragActivity;
import com.example.administrator.viewfrag.DayKcalFragActivity;
import com.example.administrator.viewfrag.DayStepFragActivity;
import com.example.administrator.viewfrag.MonthHeartFragActivity;
import com.example.administrator.viewfrag.MonthStepFragActivity;
import com.example.administrator.viewfrag.WeekHeartFragActivity;
import com.example.administrator.viewfrag.WeekStepFragActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/10/21.
 */
public class MyhealthActivity extends Fragment {
    private final static String TAG = MyhealthActivity.class
            .getSimpleName();
    View view;
    ViewPager mViewPager;
    TabLayout tabLayout;
    static String str01 = "0", str02 = "0";
    FragmentManager fragmentManager;
    //数据库
    DBManager dbManager;

    private Fragment dayFragment;
    private Fragment weekFragment;
    private Fragment monthFragment;

    private Fragment dayHeartFrag;
    private Fragment dayStepFrag;
    private Fragment dayKcalFrag;
    private Fragment dayAmountFrag;

    private Fragment weekHeartFrag;
    private Fragment weekStepFrag;

    private Fragment monthHeartFrag;
    private Fragment monthStepFrag;
    Button mapButton;

    public final static String ACTION_VIEW_STEP = "com.example.bluetooth.le.ACTION_VIEW_STEP";
    public final static String ACTION_VIEW_HEART = "com.example.bluetooth.le.ACTION_VIEW_HEART";
    public final static String ACTION_VIEW_KCAL = "com.example.bluetooth.le.ACTION_VIEW_KCAL";
    public final static String ACTION_VIEW_AMOUNT = "com.example.bluetooth.le.ACTION_VIEW_AMOUNT";

    public final static String ACTION_VIEW_LINE_HEART = "com.example.bluetooth.le.ACTION_VIEW_LINE_HEART";
    public final static String ACTION_VIEW_COLUMN_HEART = "com.example.bluetooth.le.ACTION_VIEW_LINE_COLUMN_HEART";

    public final static String ACTION_VIEW_SLIDELINE_HEART = "com.example.bluetooth.le.ACTION_VIEW_SLIDELINE_HEART";
    public final static String ACTION_VIEW_SLIDECOLUMN_HEART = "com.example.bluetooth.le.ACTION_VIEW_SLIDECOLUMN_HEART";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myhealth, container, false);
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        initView(view);
        //打开数据库
        dbManager = DBManager.getInstance(getContext());
        dbManager.openDatabase();
        dbManager.queryNewBleData();

        mapButton = (Button) view.findViewById(R.id.bt_myhealth_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });
        dayHeartFrag = new DayHeartFragActivity();


        fragmentManager = getChildFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.myhealth_frame_content, dayHeartFrag);
        transaction.commit();
        List<String> titles = new ArrayList<>();
        titles.add("日");
        titles.add("周");
        titles.add("月");
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
        List<Fragment> fragments = new ArrayList<>();
        dayFragment = DayInforFragment.newInstance();
        weekFragment = WeekInforFragment.newInstance();
        monthFragment = MonthInforFragment.newInstance();
        fragments.add(dayFragment);
        fragments.add(weekFragment);
        fragments.add(monthFragment);
        FragmentAdapter adapter =
                new FragmentAdapter(getChildFragmentManager(), titles, fragments);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);


        return view;
    }

    public void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (MyhealthActivity.ACTION_VIEW_STEP.equals(action)) {
                if (dayStepFrag == null) {
                    dayStepFrag = new DayStepFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, dayStepFrag);
                transaction.commit();
                dayStepFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_HEART.equals(action)) {
                if (dayFragment == null) {
                    dayFragment = new DayHeartFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, dayHeartFrag);
                transaction.commit();
                dayFragment = null;
            } else if (MyhealthActivity.ACTION_VIEW_AMOUNT
                    .equals(action)) {
                if (dayAmountFrag == null) {
                    dayAmountFrag = new DayAmountFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, dayAmountFrag);
                transaction.commit();
                dayAmountFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_KCAL.equals(action)) {
                if (dayKcalFrag == null) {
                    dayKcalFrag = new DayKcalFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, dayKcalFrag);
                transaction.commit();
                dayKcalFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_LINE_HEART.equals(action)) {
                if (weekHeartFrag == null) {
                    weekHeartFrag = new WeekHeartFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, weekHeartFrag);
                transaction.commit();
                weekHeartFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_COLUMN_HEART.equals(action)) {
                if (weekStepFrag == null) {
                    weekStepFrag = new WeekStepFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, weekStepFrag);
                transaction.commit();
                weekStepFrag = null;
            } else if (MyhealthActivity.ACTION_VIEW_SLIDELINE_HEART.equals(action)) {
                if (monthHeartFrag == null) {
                    monthHeartFrag = new MonthHeartFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, monthHeartFrag);
                transaction.commit();
                monthHeartFrag = null;
            }
            else if(MyhealthActivity.ACTION_VIEW_SLIDECOLUMN_HEART.equals(action))
            {
                if (monthStepFrag == null) {
                    monthStepFrag = new MonthStepFragActivity();
                }
                transaction.replace(R.id.myhealth_frame_content, monthStepFrag);
                transaction.commit();
                monthStepFrag = null;
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_AMOUNT);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_HEART);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_KCAL);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_STEP);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_COLUMN_HEART);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_LINE_HEART);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_SLIDELINE_HEART);
        intentFilter.addAction(MyhealthActivity.ACTION_VIEW_SLIDECOLUMN_HEART);
        return intentFilter;
    }
    class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;
        private List<String> mTitles;
        FragmentManager fm;

        public FragmentAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments) {
            super(fm);
            this.fm = fm;
            mTitles = titles;
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "position= " + position);
           /* Fragment fgment = dayFragment;
            FragmentTransaction transaction = fm.beginTransaction();

            switch (position) {
                case 0:
                    Log.d(TAG,"position=0");
                    transaction.remove(dayFragment);
                    fgment = dayFragment;
                    Log.d(TAG,"position=0");
                    break;
                case 1:
                    Log.d(TAG,"position=1");
                    transaction.remove(weekFragment);
                    fgment = weekFragment;
                    Log.d(TAG,"position=1");
                    break;
                case 2:
                    Log.d(TAG,"position=2");
                    transaction.remove(monthFragment);
                    fgment = monthFragment;
                    Log.d(TAG,"position=2");
                    break;
                default:
                    break;
            }
     //       View v = LayoutInflater.from(getActivity()).inflate(R.layout.activity_myinfor, null);
           // TextView ly = (TextView) v.findViewById(R.id.info_ly);
           // transaction.addSharedElement(ly, "shareLy");
            transaction.commit();*/
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }

}

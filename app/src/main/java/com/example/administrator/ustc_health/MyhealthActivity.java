package com.example.administrator.ustc_health;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.heartview.HeartView;
import com.example.administrator.set.ProFileActivity;
import com.example.administrator.utils.ComFormu;
import com.example.administrator.view.CircleProgressBar;
import com.example.administrator.view.KcalView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/10/21.
 */
public class MyhealthActivity extends Fragment {

    View view;
    ViewPager mViewPager;
    TabLayout tabLayout;
    HeartView heartview;

    static CircleProgressBar mCircleBar;
    static KcalView mkcaiView;

    private TextView tv_toolbar_state;
    static TextView tepnumText,kcalText,heartrateText;

    private Fragment dayFragment;
    private Fragment weekFragment;
    private Fragment monthFragment;

    static View heartV,stepV,kcalV,amountV;

    CircleImageView myImage;
    Button mapButton,heartrateButton;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myhealth, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        heartview = (HeartView) view.findViewById(R.id.heart_view);

        heartV=view.findViewById(R.id.include_heartrate_detail);
        stepV=view.findViewById(R.id.include_stepnum_detail);
        kcalV=view.findViewById(R.id.include_kcal_detail);
        amountV=view.findViewById(R.id.include_amount_detail);

        mCircleBar = (CircleProgressBar)
                view.findViewById(R.id.ly_stepnum_detail);
        mkcaiView=(KcalView)view.findViewById(R.id.ly_kcal_detail) ;

        tepnumText=(TextView)view.findViewById(R.id.tv_stepnum_detaile_num) ;
        kcalText=(TextView)view.findViewById(R.id.tv_kcal_detaile_num);
        heartrateText=(TextView)view.findViewById(R.id.tv_heartrate_detaile_num) ;

        tv_toolbar_state=(TextView)view.findViewById(R.id.tv_toolbar_state);
        tv_toolbar_state.setText("已连接");

        myImage=(CircleImageView) view.findViewById(R.id.iv_toolbar_my);
        myImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),ProFileActivity.class);
                startActivity(intent);
            }
        });
        heartrateButton=(Button)view.findViewById(R.id.bt_heartrate_detail_button) ;
        heartrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"This is 心率图!",Toast.LENGTH_LONG).show();
            }
        });
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
       ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        heartrateDetail();
        List<String> titles = new ArrayList<>();
        titles.add("日");
        titles.add("周");
        titles.add("月");
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
        List<Fragment> fragments = new ArrayList<>();

        dayFragment = MyInforFragment.newInstance();

        weekFragment = MyWeekInforFragment.newInstance();

        monthFragment = MyWeekInforFragment.newInstance();

        fragments.add(dayFragment);
        fragments.add(weekFragment);
        fragments.add(monthFragment);
        FragmentAdapter adapter =
                new FragmentAdapter(getChildFragmentManager(), titles);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        return view;
    }


    public static void  steunumDetail()
    {
        kcalV.setVisibility(View.GONE);
        heartV.setVisibility(View.GONE);
        amountV.setVisibility(View.GONE);
        stepV.setVisibility(View.VISIBLE);
        mCircleBar.setMax(6000);
        mCircleBar.setProgress(Integer.valueOf(MyInforFragment.str02), 700);
        tepnumText.setText(MyInforFragment.str02);

    }
    public static void  heartrateDetail()
    {
        heartV.setVisibility(View.VISIBLE);
        heartrateText.setText(MyInforFragment.str01);
        stepV.setVisibility(View.GONE);
        kcalV.setVisibility(View.GONE);
        amountV.setVisibility(View.GONE);
    }
    public static void amountDetail()
    {
        kcalV.setVisibility(View.GONE);
        heartV.setVisibility(View.GONE);
        stepV.setVisibility(View.GONE);
        amountV.setVisibility(View.VISIBLE);
    }
    public static void kcalDetail()
    {
        kcalV.setVisibility(View.VISIBLE);
        heartV.setVisibility(View.GONE);
        stepV.setVisibility(View.GONE);
        amountV.setVisibility(View.GONE);
        mkcaiView.setMax((int)ComFormu.getKcal(Integer.valueOf(MyInforFragment.str02)));
        DecimalFormat df = new DecimalFormat("###.00");
        mkcaiView.setProgress((int)ComFormu.getKcal(Integer.valueOf(MyInforFragment.str02)), 700);
        kcalText.setText(MyInforFragment.str03);

    }
    class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;
        private List<String> mTitles;
        FragmentManager fm;

        public FragmentAdapter(FragmentManager fm, List<String> titles) {
            super(fm);
            this.fm = fm;
            // mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fgment = dayFragment;
            FragmentTransaction transaction = fm.beginTransaction();

            switch (position) {
                case 0:
                    transaction.remove(dayFragment);

                    fgment = dayFragment;

                    break;
                case 1:
                    transaction.remove(weekFragment);
                    fgment = weekFragment;
                    break;

                case 2:
                    transaction.remove(monthFragment);
                    fgment = monthFragment;
                    break;
                default:
                    break;

            }
            View v=LayoutInflater.from(getActivity()).inflate(R.layout.activity_myinfor,null);
            TextView ly=(TextView)v.findViewById(R.id.info_ly);
            transaction.addSharedElement(ly,"shareLy");
            transaction.commit();


            return fgment;
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

package com.example.administrator.viewfrag;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.ustc_health.DayInforFragment;
import com.example.administrator.ustc_health.MainActivity;
import com.example.administrator.ustc_health.R;

/**
 * Created by HBL on 2016/3/8.
 */
public class DayHeartFragActivity extends Fragment {
    View view;
    TextView heartrateText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_heartrate_detail, container, false);
        heartrateText = (TextView) view.findViewById(R.id.tv_heartrate_detaile_num);
        heartrateText.setText( MainActivity.str01);
        return view;
    }
}

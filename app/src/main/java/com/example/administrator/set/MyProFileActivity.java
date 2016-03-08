package com.example.administrator.set;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.administrator.ustc_health.R;

/**
 * Created by HBL on 2016/2/24.
 */
public class MyProFileActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("我");
    }

    @Override
    public void onClick(View view) {

    }
}

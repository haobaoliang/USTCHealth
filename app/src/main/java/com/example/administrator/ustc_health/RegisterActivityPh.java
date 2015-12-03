package com.example.administrator.ustc_health;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by HBL on 2015/12/3.
 */
public class RegisterActivityPh extends AppCompatActivity {
    private Button nextbutton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_ph);
        nextbutton=(Button)findViewById(R.id.reg_ph_button);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.reg_ph_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");
        nextbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RegisterActivityPh.this,RegisterActivityFin.class);
                startActivity(intent);
            }
        });
    }
}

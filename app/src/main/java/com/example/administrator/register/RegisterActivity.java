package com.example.administrator.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.administrator.ustc_health.R;

/**
 * Created by HBL on 2015/12/2.
 */
public class RegisterActivity extends AppCompatActivity {

    private Button nextbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        nextbutton=(Button)findViewById(R.id.reg_button);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.reg_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");
        nextbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RegisterActivity.this,RegisterActivityPh.class);
                startActivity(intent);
            }
        });
    }
}

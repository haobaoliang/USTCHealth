package com.example.administrator.ble;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ustc_health.R;
import com.example.administrator.view.CircleProgressBar;

/**
 * Created by HBL on 2015/11/10.
 */
public class DeviceListActivity extends Activity {

    /*LinearLayout layout;
    private PopupWindow popupWindow;
    ImageView imageView;
    Toolbar toolbar;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
       /* layout=(LinearLayout)findViewById(R.id.ly_loc);
        layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(DeviceListActivity.this,"请选择城市",Toast.LENGTH_LONG).show();
            }
        });
        toolbar=(Toolbar)findViewById(R.id.id_toolbar) ;
        imageView=(ImageView)findViewById(R.id.iv_toolbar_set) ;
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater;
                View popup;
                layoutInflater = LayoutInflater.from(DeviceListActivity.this);
                popup = layoutInflater.inflate(R.layout.activity_toolbar_menu, null);
                popupWindow = new PopupWindow(popup, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, false);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0));

               TextView menu1 = (TextView)popup.findViewById(R.id.tv_menu_01);
                menu1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DeviceListActivity.this, "私人医生...", Toast.LENGTH_SHORT).show();
                    }
                });
                popupWindow.showAsDropDown(imageView);
                popupWindow.update();
            }
        });
*/
    }
}

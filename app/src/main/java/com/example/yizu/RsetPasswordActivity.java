package com.example.yizu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class RsetPasswordActivity extends Activity {
    private ImageView rps_back1;
    private EditText oldpassord;
    private EditText newpassword;
    private EditText newpassword_r;
    private Button sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rset_password);
        rps_back1=(ImageView)findViewById(R.id.rps_back1);
        oldpassord=(EditText) findViewById(R.id.oldpassword);
        newpassword=(EditText) findViewById(R.id.newpsaaword);
        newpassword_r=(EditText) findViewById(R.id.newpsaaword_r);
        sure=(Button)findViewById(R.id.ps_sure);
        rps_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

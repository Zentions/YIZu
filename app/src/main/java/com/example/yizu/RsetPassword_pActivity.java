package com.example.yizu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RsetPassword_pActivity extends Activity {
    private ImageView rps_back2;
    private EditText yanzhengma;
    private EditText newpassword1;
    private EditText newpassword_r1;
    private Button sure1;
    private Button btn_yanzhengma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rset_password_p);
        rps_back2=(ImageView)findViewById(R.id.rps_back2);
        yanzhengma=(EditText) findViewById(R.id.yanzhengma);
        newpassword1=(EditText) findViewById(R.id.newpassword1);
        newpassword_r1=(EditText) findViewById(R.id.newpsaaword_r1);
        sure1=(Button)findViewById(R.id.ps_sure1);
        btn_yanzhengma=(Button)findViewById(R.id.btn_yanzhengma);
        rps_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

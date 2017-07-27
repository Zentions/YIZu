package com.example.yizu;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SetActivity extends Activity {

    private ImageView set_back;
    private TextView clear_number;
    private String cache;
    private Integer cache_number;
    private final int REQUEST_CODE = 0x1001;
    private final  static String phonenumer="17854262835";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_activity);
        set_back=(ImageView)findViewById(R.id.set_back);
        clear_number=(TextView)findViewById(R.id.clear_number);

        cache_number=5;
        cache=cache_number.toString()+"M";
        clear_number.setText(cache);

        set_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mima:
                final String[] items = getResources().getStringArray(R.array.product_status);
                new AlertDialog.Builder(SetActivity.this)
                        .setTitle("请选择修改方式")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (items[which].toString().equals("通过旧密码")) {
                                    Intent intent=new Intent(SetActivity.this,RsetPasswordActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent=new Intent(SetActivity.this,RsetPassword_pActivity.class);
                                    startActivity(intent);
                                }

                            }
                        }).show();
                break;
            case R.id.phonenumber:
                new AlertDialog.Builder(SetActivity.this)
                        .setTitle("您确定拨打电话？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        testCallPhone();

                                    }
                                })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

                break;
            case R.id.about:
                Intent intent=new Intent(SetActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.qingchu:
                Toast.makeText(SetActivity.this,"缓存清除成功", Toast.LENGTH_SHORT).show();
                cache_number=0;
                clear_number.setText(cache_number.toString()+"M");
                break;
            default:
                break;

        }

    }
    private void testCallPhone() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (PermissionChecker.checkSelfPermission(SetActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE);
            } else {
                callPhone(phonenumer);
            }

        } else {
            callPhone(phonenumer);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && PermissionChecker.checkSelfPermission(SetActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            callPhone(phonenumer);
        }
    }
    //直接拨号
    private void callPhone(String phoneNum) {
        Uri uri = Uri.parse("tel:" + phoneNum);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }




}
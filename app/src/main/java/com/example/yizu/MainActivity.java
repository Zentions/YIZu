package com.example.yizu;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yizu.bean.User;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.yizu.tool.PictureTool;
import com.example.yizu.tool.ShareStorage;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.id.icon1;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private CoordinatorLayout right;
    private NavigationView left;
    private boolean isDrawer=false;
    CircleImageView user_picture;
    private TextView title;
    public LocationClient mLocationClient;
    private TextView positionText;
    static  String c_sheng,c_shi,c_qu;
    private User user;
    TextView myNum,myName;
    private String Path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        right = (CoordinatorLayout) findViewById(R.id.right1);
        left = (NavigationView) findViewById(R.id.nav_view);
        title = (TextView)findViewById(R.id.MianTitle);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        user_picture = (CircleImageView) headerView.findViewById(R.id.roundImageView);
        myName = (TextView) headerView.findViewById(R.id.myName);
        myNum = (TextView) headerView.findViewById(R.id.myNum);
        positionText = (TextView) findViewById(R.id.position);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isDrawer){
                    return left.dispatchTouchEvent(motionEvent);
                }else{
                    return false;
                }
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.icon_location:
                        ChangeAddressPopwindow mChangeAddressPopwindow = new ChangeAddressPopwindow(MainActivity.this);
                        mChangeAddressPopwindow.setAddress("广东", "深圳", "福田区");
                        mChangeAddressPopwindow.showAtLocation(positionText, Gravity.BOTTOM, 0, 0);
                        mChangeAddressPopwindow.setAddresskListener(new ChangeAddressPopwindow.OnAddressCListener() {
                            @Override
                            public void onClick(String province, String city, String area) {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "您选择了:"+province + "-" + city + "-" + area, Toast.LENGTH_LONG).show();
                                positionText.setText(area);
                            }
                            @Override
                            public void onClick() {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "当前定位:"+c_sheng +"-" + c_shi + "-" + c_qu, Toast.LENGTH_LONG).show();
                                positionText.setText(c_qu);
                            }

                        });
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
        user_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserMessageActivity.class);
                intent.putExtra("mime",user);
                intent.putExtra("path",Path);
                startActivity(intent);
            }
        });
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer=true;
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left 左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                right.layout(left.getRight(), 0, left.getRight() + display.getWidth(), display.getHeight());
            }
            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer=false;
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        String ObjectId = ShareStorage.getShareString(this,"ObjectId");
        queryUser(ObjectId);
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    public void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    public void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            final String  sheng=location.getProvince().toString();
            final String  shi=location.getCity().toString();
            final String qu=location.getDistrict().toString();
            c_sheng=sheng;
            c_shi=shi;
            c_qu=qu;
            pop(sheng,shi,qu);


        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }

    }
    public void pop(String x1,String x2,final String x3)
    {
        Toast.makeText(this,"当前定位为:"+x1+"-"+x2+"-"+x3, Toast.LENGTH_SHORT).show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                positionText.setText(x3);
            }
        });
    }
    private void queryUser(String ObjectID){//根据id查询用户
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(ObjectID, new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if(e==null){
                    user = object;
                    BmobFile bmobfile = user.getTouXiang();
                    if(bmobfile!= null){
                        bmobfile.download(new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                show();
                                if(e==null) {
                                    user_picture.setImageBitmap(PictureTool.decodeSampledBitmapFromResource(s,300,300));
                                    Path = s;
                                }
                                else  Toast.makeText(MainActivity.this,e.getErrorCode(),Toast.LENGTH_LONG);
                            }
                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });
                    }
                }else{
                    Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG);
                }
            }

        });
    }
    private void show(){
        if(user.getName()!=null) myName.setText(user.getName());
        myNum.setText(user.getPhoneNumber());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String ObjectId = ShareStorage.getShareString(this,"ObjectId");
        queryUser(ObjectId);
    }
}

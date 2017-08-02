package com.example.yizu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yizu.adapter.RecommendAdapter;
import com.example.yizu.bean.Goods;
import com.example.yizu.bean.ShareModel;
import com.example.yizu.bean.User;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.yizu.control.ChangeAddressPopwindow;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.PictureTool;
import com.example.yizu.tool.ShareStorage;
import com.example.yizu.tool.ShowDialog;
import com.example.yizu.tool.ToastShow;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import de.hdodenhof.circleimageview.CircleImageView;

import cn.sharesdk.framework.PlatformActionListener;



import android.os.Handler.Callback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,PlatformActionListener,Callback {


    private static  final String text = "这是YI租的下载地址";
    private static  final String imageurl = "http://139.199.224.112/logo.png";
    private static  final String title = "江苏华漫";
    private static  final String url = "http://139.199.224.112/YI.apk";
    public static final String SHARE_APP_KEY = "21b0f35691b8";
    private SharePopupWindow share;

    private CoordinatorLayout right;
    private NavigationView left;
    private boolean isDrawer=false;
    CircleImageView user_picture;
    private LinearLayout title1;
    public LocationClient mLocationClient;
    private TextView positionText;
    static  String c_sheng,c_shi,c_qu;
    private User user;
    TextView myNum,myName;
    DrawerLayout drawer;
    private String Path=null;
    private long exitTime;
    ///////////////////////////
    private TwinklingRefreshLayout twinklingRefreshLayout;
    private TextView skill,electronic,home,study,clothes,transportation,place,service;
    private List<Goods> recommendList=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecommendAdapter recommendAdapter;
    private Goods goods1=new Goods(),goods2=new Goods(),goods3=new Goods(),goods4=new Goods(),goods5=new Goods(),goods6=new Goods();
    ///////////////////
    private ToastShow toastShow = new ToastShow(this);
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBar();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);

        ShareSDK.initSDK(this);

        ActivityCollecter.addActivty(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        right = (CoordinatorLayout) findViewById(R.id.right1);
        left = (NavigationView) findViewById(R.id.nav_view);
        title1 = (LinearLayout) findViewById(R.id.MianTitle);
        ////////////////////////////////
        skill=(TextView)findViewById(R.id.skill);
        electronic=(TextView)findViewById(R.id.electronic);
        home=(TextView)findViewById(R.id.home);
        study=(TextView)findViewById(R.id.study);
        clothes=(TextView)findViewById(R.id.clothes);
        transportation=(TextView)findViewById(R.id.transportation);
        place=(TextView)findViewById(R.id.place);
        service=(TextView)findViewById(R.id.service);
        recyclerView=(RecyclerView)findViewById(R.id.recommendRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        goods1.setClassification("电子产品");
        goods1.setGoodsName("123");
        goods1.setStarRating(7.8);
        goods1.setMoneyPer(5.0);
        goods2.setClassification("dhhsbq");
        goods2.setGoodsName("jxsx3");
        goods2.setStarRating(9.8);
        goods2.setMoneyPer(7.0);
        goods3.setClassification("电子产品");
        goods3.setGoodsName("123");
        goods3.setStarRating(7.8);
        goods3.setMoneyPer(5.0);
        goods4.setClassification("dhhsbq");
        goods4.setGoodsName("jxsx3");
        goods4.setStarRating(9.8);
        goods4.setMoneyPer(7.0);
        goods5.setClassification("电子产品");
        goods5.setGoodsName("123");
        goods5.setStarRating(7.8);
        goods5.setMoneyPer(5.0);
        goods6.setClassification("dhhsbq");
        goods6.setGoodsName("jxsx3");
        goods6.setStarRating(9.8);
        goods6.setMoneyPer(7.0);
        recommendList.add(goods1);
        recommendList.add(goods2);
        recommendList.add(goods3);
        recommendList.add(goods4);
        recommendList.add(goods5);
        recommendList.add(goods6);

        twinklingRefreshLayout = (TwinklingRefreshLayout)findViewById(R.id.recommendRefresh);
        twinklingRefreshLayout.setEnableLoadmore(true);
        // twinklingRefreshLayout.setOverScrollBottomShow(false);
        twinklingRefreshLayout.setEnableRefresh(false);
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recommendAdapter = new RecommendAdapter(recommendList);
        recyclerView.setAdapter(recommendAdapter);
        skill.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ShowDialog.showZhuceDialog(MainActivity.this,"33333");
                //Intent intent=new Intent(MainActivity.this,PhoneVerificationActivity.class);//跳转到技能分类的页面
                //startActivityForResult(intent,1);
            }
        });
        electronic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,PhoneVerificationActivity.class);//跳转到数码产品分类的页面
                startActivityForResult(intent,1);
            }
        });
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,PhoneVerificationActivity.class);//跳转到家居分类的页面
                startActivityForResult(intent,1);
            }
        });
        study.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,PhoneVerificationActivity.class);//跳转到学习用品分类的页面
                startActivityForResult(intent,1);
            }
        });
        clothes.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,PhoneVerificationActivity.class);//跳转到服饰分类的页面
                startActivityForResult(intent,1);
            }
        });
        transportation.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,PhoneVerificationActivity.class);//跳转到交通工具分类的页面
                startActivityForResult(intent,1);
            }
        });
        place.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,PhoneVerificationActivity.class);//跳转到场地分类的页面
                startActivityForResult(intent,1);
            }
        });
        service.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,PhoneVerificationActivity.class);//跳转到服务分类的页面
                startActivityForResult(intent,1);
            }
        });
/////////////////////////////////////
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
                                ShareStorage.setShareString(MainActivity.this,"mainShi",city+"市","mainQu",area+"区");
                            }
                            @Override
                            public void onClick() {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "当前定位:"+c_sheng +"-" + c_shi + "-" + c_qu, Toast.LENGTH_LONG).show();
                                ShareStorage.setShareString(MainActivity.this,"mainShi",c_shi,"mainQu",c_qu);
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
                if(Path==null)intent.putExtra("path","null");
                else intent.putExtra("path",Path);
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
        title1.setOnClickListener(new View.OnClickListener() {
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
            Intent intent = new Intent(this,OrderActivity.class);
            intent.putExtra("flag","1");
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this,OrderActivity.class);
            intent.putExtra("flag","2");
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this,UserGoodsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this,SetActivity.class);
            startActivity(intent);

        }else if(id ==R.id.nav_release){
            Intent intent = new Intent(this,CreateMessageActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_share) {
            share = new SharePopupWindow(MainActivity.this);
            share.setPlatformActionListener(MainActivity.this);
            ShareModel model = new ShareModel();
            model.setImageUrl(imageurl);
            model.setText(text);
            model.setTitle(title);
            model.setUrl(url);
            share.initShareParams(model);
            share.showShareWindow();
            // 显示窗口 (设置layout在PopupWindow中显示的位置)
            share.showAtLocation(
                    MainActivity.this.findViewById(R.id.drawer_layout),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        }
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
            ShareStorage.setShareString(MainActivity.this,"mainShi",c_shi,"mainQu",c_qu);
            ShareStorage.setShareString(MainActivity.this,"Shi",c_shi,"Qu",c_qu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
        ShareSDK.stopSDK(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return true;
        }
        else{
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
            {

                if((System.currentTimeMillis()-exitTime) > 2000) {
                    toastShow.toastShow ("再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                }
                else {
                    moveTaskToBack(false);
                }

                return true;
            }
            return super.onKeyDown(keyCode, event);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void changeStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //获取样式中的属性值
            TypedValue typedValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
            int[] attribute = new int[]{android.R.attr.colorPrimary};
            TypedArray array = this.obtainStyledAttributes(typedValue.resourceId, attribute);
            int color = array.getColor(0, Color.TRANSPARENT);
            array.recycle();
            window.setStatusBarColor(color);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (share != null) {
            share.dismiss();
        }
    }



    @Override
    public void onCancel(Platform arg0, int arg1) {
        Message msg = new Message();
        msg.what = 0;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onComplete(Platform plat, int action,
                           HashMap<String, Object> res) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        Message msg = new Message();
        msg.what = 1;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        if (what == 1) {
            Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
        }
        if (share != null) {
            share.dismiss();
        }
        return false;
    }
}

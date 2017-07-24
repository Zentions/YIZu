package com.example.yizu;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yizu.bean.Goods;
import com.example.yizu.dialog.NumberAddSubView;
import com.example.yizu.tool.PictureTool;

import java.io.File;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class CreateMessageActivity extends AppCompatActivity {
    private Button subMessage;
    EditText goodsnameEdit;
    EditText goodsMessageEdit;
    NumberAddSubView ZUJIN;
    NumberAddSubView YAJIN;
    Spinner fenlei;
    String cardNumber;
    String goodsName;
    String description;

    Double zujin;
    Double yajin;
    int flag=0;
    int currentIB=0;//当前的点击的图片按钮
    private ImageButton img[];//图片按钮
    public static final int TAKE_PHOTO = 11;
    public static final int CHOOSE_PHOTO = 12;
    private Uri[] imageUri = new Uri[3];
    private String UriPath[] = new String[3];//图片路径
    public static final int CONTEXT_NULL = 0;
    public  static final int SUCCESS = 4;
    public static final int CONTEXT_NULL_1 = 1;
    public static final int CONTEXT_NULL_2 = 2;
    public static final int CONTEXT_NULL_3 = 3;
    public static final int CONTEXT_NULL_4 = 5;
    int j=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
        goodsnameEdit=(EditText)findViewById(R.id.goodsname);
        goodsMessageEdit=(EditText)findViewById(R.id.goodsMessage);
        ZUJIN=(NumberAddSubView)findViewById(R.id.zujin1);
        YAJIN=(NumberAddSubView)findViewById(R.id.yajin1);
        fenlei = (Spinner)findViewById(R.id.spinner);
        fenlei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardNumber = fenlei.getSelectedItem().toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               flag=1;

            }
        });
        img = new ImageButton[3];
        img[0] = (ImageButton)findViewById(R.id.choose1);
        img[1] = (ImageButton)findViewById(R.id.choose2);
        img[2] = (ImageButton)findViewById(R.id.choose3);
        for(int i =0;i<3;i++){
            UriPath[i]="";
        }
        subMessage = (Button)findViewById(R.id.rightmessage);
        subMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        for(int i = 0;i < 3;i++){
            final int finalI = i;
            img[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIB = finalI;
                    showDialog();
                }
            });
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){/////2222
                case CONTEXT_NULL:
                    Toast.makeText(CreateMessageActivity.this,"商品名称不能为空！！",Toast.LENGTH_SHORT).show();
                    break;
                case CONTEXT_NULL_1:
                    Toast.makeText(CreateMessageActivity.this,"详细信息不能为空！！",Toast.LENGTH_SHORT).show();
                    break;
                case CONTEXT_NULL_2:
                    Toast.makeText(CreateMessageActivity.this,"您还可以提交"+j+"张照片！！",Toast.LENGTH_SHORT).show();
                    break;
                case CONTEXT_NULL_3:
                    Toast.makeText(CreateMessageActivity.this,"未选择分类！！",Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    saveGoods();
                    break;
                default:break;
            }
        }
    };

    private void sendMessage(){
        Message message = new Message();
        //判断
        goodsName = goodsnameEdit.getText().toString().trim();
        description=goodsMessageEdit.getText().toString().trim();
        zujin=new Double(ZUJIN.getValue());
        yajin=new Double(YAJIN.getValue());
        if(TextUtils.isEmpty(goodsName)){
            message.what = CONTEXT_NULL;
            handler.handleMessage(message);////111
            return;
        }
        if(TextUtils.isEmpty(description)){
            message.what = CONTEXT_NULL_1;
            handler.handleMessage(message);
            return;
        }
        if(cardNumber.equals("未选择分类"))
        {
            message.what=CONTEXT_NULL_3;
            handler.handleMessage(message);
            return;
        }

        j=0;
        for(int i =0;i<3;i++){
            if(TextUtils.isEmpty(UriPath[i])) j++;
        }
        if(j>0){
            message.what = CONTEXT_NULL_2;
            handler.handleMessage(message);
            return;
        }
        if(flag==1)
        {
            message.what=CONTEXT_NULL_3;
            handler.handleMessage(message);
            return;
        }
        message.what = SUCCESS;
        handler.handleMessage(message);
    }
    void saveGoods(){//保存商品信息
        final Goods goods = new Goods();
        goods.setDescription(description);
        goods.setDeposit(yajin);
        goods.setMoneyPer(zujin);
        //设置属性
        goods.setGoodsName(goodsName);
        goods.setClassification(cardNumber);
        goods.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    goods.setObjectId(s);
                    Toast.makeText(CreateMessageActivity.this,"提交成功！！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CreateMessageActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }
    //申请权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri[currentIB]));

                        img[currentIB].setImageBitmap(PictureTool.zoomBimtap(bitmap,500,500));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    PictureTool tool = new PictureTool();
                    UriPath[currentIB] = tool.getPicFromUri(this,data.getData());
                    displayImage(UriPath[currentIB]);
                }
                break;
            default:
                break;
        }
    }
    //展示图片
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            img[currentIB].setImageBitmap(PictureTool.zoomBimtap(bitmap,500,500));
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
    public void showDialog() {
        final View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.photo_choose_dialog, null);
        //初始化控件
        Button picture = (Button) view.findViewById(R.id.Picture_Map);
        Button camera = (Button) view.findViewById(R.id.Picture_Camera);
        Button cancel = (Button) view.findViewById(R.id.Picture_Cancel);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateMessageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateMessageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                dialog.dismiss();

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getExternalCacheDir(), currentIB+"output.jpg");
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UriPath[currentIB] = file.getPath();
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri[currentIB] = FileProvider.getUriForFile(CreateMessageActivity.this, "com.example.yizu", file);
                } else {
                    imageUri[currentIB] = Uri.fromFile(file);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri[currentIB]);
                startActivityForResult(intent, TAKE_PHOTO);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });}



    }




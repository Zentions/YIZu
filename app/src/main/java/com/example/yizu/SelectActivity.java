package com.example.yizu;

        import android.Manifest;
        import android.app.Activity;
        import android.content.ContentUris;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Picture;
        import android.net.Uri;
        import android.os.Build;
        import android.provider.DocumentsContract;
        import android.provider.MediaStore;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.content.FileProvider;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.example.yizu.tool.PictureTool;

        import java.io.ByteArrayInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileNotFoundException;

public class SelectActivity extends AppCompatActivity {
    public static final int Take_Photo = 1;
    public static final int Choose_Photo = 2;
    private ImageView picture;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        picture = (ImageView)findViewById(R.id.picture);
        Button button = (Button)findViewById(R.id.take_photo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getExternalCacheDir(), "output.jpg");
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(SelectActivity.this, "com.example.GAO", file);
                } else {
                    imageUri = Uri.fromFile(file);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, Take_Photo);
            }
        });
        Button ChooseAlbum = (Button)findViewById(R.id.choose_photo);
        ChooseAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SelectActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SelectActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });

    }
    public static Bitmap zoomBimtap(Bitmap src, int width, int height) {
        return Bitmap.createScaledBitmap(src, width, height, true);
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, Choose_Photo); // 打开相册
    }

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
            case Take_Photo:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

                        picture.setImageBitmap(zoomBimtap(bitmap,500,500));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Choose_Photo:
                if (resultCode == RESULT_OK) {
                    PictureTool tool = new PictureTool();
                    displayImage(tool.getPicFromUri(this,data.getData()));
                }
                break;
            default:
                break;
        }
    }



    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(zoomBimtap(bitmap,500,500));
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}
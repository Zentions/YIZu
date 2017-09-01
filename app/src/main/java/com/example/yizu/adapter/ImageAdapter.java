package com.example.yizu.adapter;

/**
 * Created by yikuai on 2017/7/24.
 */

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yizu.R;
import com.example.yizu.bean.Goods;
import com.example.yizu.tool.PictureTool;

import java.io.File;
import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

public class ImageAdapter extends PagerAdapter {//上栏
    //private Context context;
    private LruCache<String, Bitmap> lruCache;
    private ArrayList<View> mImageViewList;
    //item的个数
    private Context context;
    private BmobFile[] files;
    public ImageAdapter( ArrayList<View> mImageViewList,Context context,BmobFile[] files){

        this.mImageViewList=mImageViewList;
        this.context = context;
        this.files = files;
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }
        };
    }



   public int getCount() {
        return mImageViewList.size();
    }

   @Override
    public boolean isViewFromObject(View view, Object object) {
       return view == object;

    }

//    //初始化item布局
    @Override
   public Object instantiateItem(ViewGroup container, int position) {
        View view = mImageViewList.get(position);
        ImageView goodsPic = (ImageView)view.findViewById(R.id.imageView);
        setItemBitmaps(files[position],goodsPic);
        container.addView(view);
        return view;
    }

   //销毁item
   @Override
   public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
   }
    public Bitmap getBitmapFromMemoryCache(String url) {
        return lruCache.get(url);
    }
    public void setItemBitmaps(final BmobFile bmobfile, final ImageView imageView) {
        if(bmobfile!=null){
            File saveFile = new File(context.getExternalFilesDir(null), bmobfile.getFilename());
            Bitmap cache = getBitmapFromMemoryCache(bmobfile.getUrl());
            if (cache != null) {//内存缓存
                imageView.setImageBitmap(cache);
            } else {
                String filePath = saveFile.getPath();
                final Bitmap localCache = PictureTool.showImage(filePath);
                if (localCache != null) {//本地缓存
                    imageView.setImageBitmap(localCache);
                    lruCache.put(bmobfile.getUrl(), localCache);
                } else {//请求网络
                    bmobfile.download(saveFile, new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                //  holder.articleImage.setImageBitmap(PictureTool.decodeSampledBitmapFromResource(goods.getPath(0), 300, 300));
                                Bitmap bitmap = PictureTool.showImage(s);
                                imageView.setImageBitmap(bitmap);
                                lruCache.put(bmobfile.getUrl(), bitmap);
                            } else
                                Toast.makeText(context, e.getErrorCode(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                }
            }
        }
    }
}
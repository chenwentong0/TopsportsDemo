package com.example.common.utils.imageutil.glide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.common.net.subscriber.BaseObjectSubscriber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * Created by wentong.chen on 18/7/11.
 * 功能：图片下载
 */
public class DownLoadImageService {
    private String url;
    private Context context;
    private BaseObjectSubscriber<Bitmap> callBack;
    private File currentFile;
    private String fileName;

    private boolean isSetMediaStore;

    public DownLoadImageService(Context context, String url, boolean isSetMediaStore , String fileName, BaseObjectSubscriber<Bitmap> callBack) {
        this.url = url;
        this.callBack = callBack;
        this.context = context;
        this.isSetMediaStore = isSetMediaStore;
        this.fileName = fileName;
    }

    public void loadBitmap() {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                Bitmap bitmap = Glide.with(context)
                            .asBitmap()
                            .load(url)
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    if (bitmap != null){
                        // 在这里执行图片保存方法
                        saveImageToGallery(context,bitmap);
                    } else {
                        e.onError(new IOException("下载图片失败"));
                    }
                    if (bitmap != null && currentFile.exists()) {
                        callBack.onSuccess(bitmap);
                    } else {
                        e.onError(new IOException("下载图片成功， 但是文件没有找到"));
                    }
            }
        })
        .subscribe();
    }

    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径

        File appDir = new File(file ,fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        fileName = System.currentTimeMillis() + ".jpg";
        currentFile = new File(appDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(isSetMediaStore){
            setMediaDtore(fileName);
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));
    }

    /**
     * 加入到系统图库
     * @param fileName
     */
    public void setMediaDtore(String fileName){
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    currentFile.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package com.example.chenwentong.helloworld.ui.two;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
 
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
 
/**
 * Created by slack
 * on 17/5/12 下午4:07
 * 外部 调用 无他相机 拍照 返回照片
 * 1.有读写外部文件权限
 */
 
public class ImageCapture {

    public static final ImageCapture IMAGE_CAPTURE = new ImageCapture();

    private ImageCapture(){}

    public Uri mUri = null;

    public File mFile = null;

    private boolean mNeedImageCapture = false;

    public boolean isEmpty(){
        return !mNeedImageCapture;
    }

    public void updateCaptureAction(Intent intent){
        if (intent.getAction() != null) {
            if (intent.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE)) {
                mNeedImageCapture = true;
                Uri fileUri = null;
                Bundle bundle = intent.getExtras();
                if(bundle != null) {
                    fileUri = bundle.getParcelable(MediaStore.EXTRA_OUTPUT);
                }
                updateUri(fileUri);
            }
        }
    }

    private void updateUri(Uri uri) {
        mUri = uri;
        if(uri != null){
            mFile = new File(uri.getPath());
            // 缓存空间
            if (mFile.getAbsolutePath().equals("/scrapSpace")) {
                mFile = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/mms/scrapSpace/.temp.jpg");
            }
            Log.i("slack", "ImageCapture uri:" + uri + " file mkdirs " +
                    (mFile.mkdirs() ? "success" : "fail") + ", file path:" + mFile.getAbsolutePath());
        }
    }

    /**
     * call on main thread
     */
    public void onObtainBitmap(final Activity activity, final Bitmap bitmap){
        Toast.makeText(activity,"正在生成图片...",Toast.LENGTH_LONG).show();
        if(mUri != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    if (mFile.exists()) {
                        mFile.delete();
                    }
                    OutputStream out;
                    try {
                        out = activity.getContentResolver().openOutputStream(mUri);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        bitmap.recycle();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                    Log.i("slack", "ImageCapture saveImageToAlbum file exists:" + mFile.exists() + " file:" + mFile.getAbsolutePath());
                }
            }.execute();
        } else {
            // 此处只返回缩略图 Intent 传输数据 < 1M
            Intent intent = new Intent();
            intent.setAction("inline-data");
            intent.putExtra("data", reSampleBitmap(bitmap));
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }
    /**
     * 暂定拍照数据 1/16   TODO 修改这个大小
     * @param bitmap
     * @return
     */
    private Bitmap reSampleBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.25f, 0.25f);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        Log.i("slack", "压缩后图片的大小" + (result.getByteCount() / 1024f / 1024f)
                + "M宽度为" + result.getWidth() + "高度为" + result.getHeight());
        return result;
    }
    public void clear(){
        mNeedImageCapture = false;
        mFile = null;
        mUri = null;
    }
 
}
package com.example.chenwentong.helloworld.ui.two;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chenwentong.helloworld.R;
import com.example.chenwentong.helloworld.base.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Date 2018/8/27
 * Time 23:48
 *
 * @author wentong.chen
 */
public class TwoActivity extends BaseActivity {
    CameraView mCameraView;
    private ImageView mIvShowPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否 image capture
        ImageCapture.IMAGE_CAPTURE.updateCaptureAction(this.getIntent());

        setContentView(R.layout.activity_two);
        mCameraView = (CameraView) findViewById(R.id.slack_camera_view);
        mIvShowPicture = findViewById(R.id.iv_show);
    }

    public void takePhoto(View view) {
        openCamera_1();
    }

    private CameraView.PictureCallback mPictureCallback = new CameraView.PictureCallback() {
        @Override
        public void onPictureTaken(Bitmap bitmap) {
            if(ImageCapture.IMAGE_CAPTURE.isEmpty()){
                // do normal things
                Toast.makeText(getBaseContext(),"normal click",Toast.LENGTH_SHORT).show();
            }else {
                ImageCapture.IMAGE_CAPTURE.onObtainBitmap(TwoActivity.this,bitmap);
//                ImageCapture.IMAGE_CAPTURE.saveBitmapToFile(TwoActivity.this,bitmap);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageCapture.IMAGE_CAPTURE.clear();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_two;
    }

    private static final int REQUEST_CAMERA_1 = 1;
    private static final int REQUEST_CAMERA_2 = 1;
    private static final int REQUEST_CAMERA_3 = 1;

    private String mFilePath;
    // 拍照并显示图片
    private void openCamera_1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        startActivityForResult(intent, REQUEST_CAMERA_1);
    }

    // 拍照后存储并显示图片
    private void openCamera_2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        Uri photoUri = Uri.fromFile(new File(mFilePath)); // 传递路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径
        startActivityForResult(intent, REQUEST_CAMERA_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回数据
            if (requestCode == REQUEST_CAMERA_1 && data != null) { // 判断请求码是否为REQUEST_CAMERA,如果是代表是这个页面传过去的，需要进行获取
                Bundle bundle = data.getExtras(); // 从data中取出传递回来缩略图的信息，图片质量差，适合传递小图片
                Bitmap bitmap = (Bitmap) bundle.get("data"); // 将data中的信息流解析为Bitmap类型
                mIvShowPicture.setImageBitmap(bitmap);// 显示图片
            } else if (requestCode == REQUEST_CAMERA_2) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFilePath); // 根据路径获取数据
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    mIvShowPicture.setImageBitmap(bitmap);// 显示图片
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();// 关闭流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

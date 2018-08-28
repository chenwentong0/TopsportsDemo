package com.example.chenwentong.helloworld.ui.two;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
 
import java.io.IOException;
 
/**
 * Created by slack
 * on 17/5/12 下午6:06
 * 主要的surfaceView，负责展示预览图片，camera的开关
 */
 
public class CameraView extends SurfaceView implements SurfaceHolder.Callback{
 
    private SurfaceHolder mHolder = null;
 
    private Camera mCamera;
 
    private final int mDegree = 90;
 
    public CameraView(Context context) {
        this(context,null);
    }
 
    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
 
        mHolder = this.getHolder();
        mHolder.addCallback(this);
    }
 
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        try {
            //设置camera预览的角度，因为默认图片是倾斜90度的
            mCamera.setDisplayOrientation(mDegree);
            //设置holder主要是用于surfaceView的图片的实时预览，以及获取图片等功能，可以理解为控制camera的操作..
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
            e.printStackTrace();
        }
    }
 
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        /**
         * 如果使用正常拍照进行拍照，设置了这个 Rotation, 拍得的图片总是竖直的
         */
        parameters.setRotation(mDegree);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }
 
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
 
    public void takePicture(PictureCallback callback){
        mCamera.takePicture(null, null, new CameraPictureCallback(callback));
    }
 
    // 回调用的picture，实现里边的onPictureTaken方法，其中byte[]数组即为照相后获取到的图片信息
    private class CameraPictureCallback implements Camera.PictureCallback {
 
        private PictureCallback pictureCallback;
        public CameraPictureCallback(PictureCallback callback) {
            pictureCallback = callback;
        }
 
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap b = null;
            if(null != data){
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                mCamera.stopPreview();
            }
 
            //保存图片到sdcard
            if(null != b)
            {
                if(pictureCallback != null){
                    pictureCallback.onPictureTaken(b);
                }
                //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                //图片竟然不能旋转了，故这里要旋转下
//                Bitmap rotaBitmap = getRotateBitmap(b, mDegree);
//                b.recycle();
//                if(pictureCallback != null){
//                    pictureCallback.onPictureTaken(rotaBitmap);
//                }
            }
            //再次进入预览
            mCamera.startPreview();
        }
 
    };
 
    private Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }
 
    public interface PictureCallback {
        void onPictureTaken(Bitmap bitmap);
    }
 
}
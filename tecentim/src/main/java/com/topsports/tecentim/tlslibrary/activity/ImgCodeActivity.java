package com.topsports.tecentim.tlslibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.topsports.tecentim.tlslibrary.helper.MResource;
import com.topsports.tecentim.tlslibrary.service.AccountLoginService;
import com.topsports.tecentim.tlslibrary.service.Constants;
import com.topsports.tecentim.tlslibrary.service.PhonePwdLoginService;
import com.topsports.tecentim.tlslibrary.service.TLSService;

public class ImgCodeActivity extends Activity implements View.OnClickListener{

    private final static String TAG = "ImgCodeActivity";
    private static ImageView imgcodeView;

    private EditText imgcodeEdit;
    private TLSService tlsService;
    private int login_way;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MResource.getIdByName(this, "layout", "tencent_tls_ui_activity_img_code"));

        tlsService = TLSService.getInstance();

        imgcodeEdit = (EditText) findViewById(MResource.getIdByName(this, "id", "txt_checkcode"));
        imgcodeView = (ImageView) findViewById(MResource.getIdByName(this, "id", "imagecode"));
        imgcodeView.setOnClickListener(this);

        Intent intent = getIntent();
        byte[] picData = intent.getByteArrayExtra(Constants.EXTRA_IMG_CHECKCODE);
        login_way = intent.getIntExtra(Constants.EXTRA_LOGIN_WAY, Constants.NON_LOGIN);

        fillImageview(picData);
        findViewById(MResource.getIdByName(this, "id", "btn_verify")).setOnClickListener(this);
        findViewById(MResource.getIdByName(this, "id", "btn_cancel")).setOnClickListener(this);
        findViewById(MResource.getIdByName(this, "id", "refreshImageCode")).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == MResource.getIdByName(this, "id", "btn_verify")) {
            String imgcode = imgcodeEdit.getText().toString();
            if (login_way == Constants.PHONEPWD_LOGIN) {
                tlsService.TLSPwdLoginVerifyImgcode(imgcode, PhonePwdLoginService.pwdLoginListener);
            } else if (login_way == Constants.USRPWD_LOGIN) {
                tlsService.TLSPwdLoginVerifyImgcode(imgcode, AccountLoginService.pwdLoginListener);
            }
            finish();
        } else if (v.getId() == MResource.getIdByName(this, "id", "imagecode")
                || v.getId() == MResource.getIdByName(this, "id", "refreshImageCode")) { // 刷新验证码
            tlsService.TLSPwdLoginReaskImgcode(AccountLoginService.pwdLoginListener);
        } if (v.getId() == MResource.getIdByName(this, "id", "btn_cancel")) {
            finish();
        }
    }

    public static void fillImageview(byte[] picData) {
        if (picData == null) {
            return;
        }
        Bitmap bm = BitmapFactory.decodeByteArray(picData, 0, picData.length);
        Log.e(TAG, "w " + bm.getWidth() + ", h " + bm.getHeight());
        imgcodeView.setImageBitmap(bm);
    }
}
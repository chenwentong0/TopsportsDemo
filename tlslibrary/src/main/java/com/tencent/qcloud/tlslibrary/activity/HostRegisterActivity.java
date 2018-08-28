package com.tencent.qcloud.tlslibrary.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tencent.qcloud.tlslibrary.R;
import com.tencent.qcloud.tlslibrary.helper.MResource;
import com.tencent.qcloud.tlslibrary.helper.SmsContentObserver;
import com.tencent.qcloud.tlslibrary.service.Constants;
import com.tencent.qcloud.tlslibrary.service.TLSService;


public class HostRegisterActivity extends Activity {

    private TLSService tlsService;
    private SmsContentObserver smsContentObserver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tencent_tls_ui_activity_host_register);

        tlsService = TLSService.getInstance();
        tlsService.initSmsRegisterService(this,
                (EditText) findViewById(R.id.selectCountryCode_hostRegister),
                (EditText) findViewById(R.id.phoneNumber_hostRegister),
                (EditText) findViewById(R.id.checkCode_hostRegister),
                (Button) findViewById(R.id.btn_requireCheckCode_hostRegister),
                (Button) findViewById(R.id.btn_hostRegister)
        );

        // 设置返回按钮
        findViewById(R.id.returnHostLoginActivity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HostRegisterActivity.this.onBackPressed();
                    }
                });

/*        smsContentObserver = new SmsContentObserver(new Handler(),
                this,
                (EditText) findViewById(MResource.getIdByName(this, "id", "checkCode_hostRegister")),
                Constants.SMS_REGISTER_SENDER);
        //注册短信变化监听
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContentObserver);*/
    }

    protected void onDestroy() {
        super.onDestroy();
        if (smsContentObserver != null) {
            this.getContentResolver().unregisterContentObserver(smsContentObserver);
        }
    }
}

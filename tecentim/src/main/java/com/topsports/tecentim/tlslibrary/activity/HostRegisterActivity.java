package com.topsports.tecentim.tlslibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.topsports.tecentim.R;
import com.topsports.tecentim.tlslibrary.helper.MResource;
import com.topsports.tecentim.tlslibrary.helper.SmsContentObserver;
import com.topsports.tecentim.tlslibrary.service.TLSService;


public class HostRegisterActivity extends Activity {

    private TLSService tlsService;
    private SmsContentObserver smsContentObserver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tencent_tls_ui_activity_host_register);

        tlsService = TLSService.getInstance();
        tlsService.initSmsRegisterService(this,
                (EditText) findViewById(MResource.getIdByName(this, "id", "selectCountryCode_hostRegister")),
                (EditText) findViewById(MResource.getIdByName(this, "id", "phoneNumber_hostRegister")),
                (EditText) findViewById(MResource.getIdByName(this, "id", "checkCode_hostRegister")),
                (Button) findViewById(MResource.getIdByName(this, "id", "btn_requireCheckCode_hostRegister")),
                (Button) findViewById(MResource.getIdByName(this, "id", "btn_hostRegister"))
        );

        // 设置返回按钮
        findViewById(MResource.getIdByName(this, "id", "returnHostLoginActivity"))
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

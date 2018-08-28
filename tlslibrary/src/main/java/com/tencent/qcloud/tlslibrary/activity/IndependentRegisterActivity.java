package com.tencent.qcloud.tlslibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.qcloud.tlslibrary.R;
import com.tencent.qcloud.tlslibrary.helper.MResource;
import com.tencent.qcloud.tlslibrary.service.TLSService;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSHelper;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;


public class IndependentRegisterActivity extends Activity {

    public final static String TAG = "IndependentRegisterActivity";
    private TLSService tlsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tencent_tls_ui_activity_independent_register);

        tlsService = TLSService.getInstance();
        tlsService.initAccountRegisterService(this,
                (EditText) findViewById(R.id.username),
                (EditText) findViewById(R.id.password),
                (EditText) findViewById(R.id.repassword),
                (Button) findViewById(R.id.btn_register)
        );

        // 设置返回按钮
        findViewById(R.id.returnIndependentLoginActivity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IndependentRegisterActivity.this.onBackPressed();
                    }
                });
    }
}

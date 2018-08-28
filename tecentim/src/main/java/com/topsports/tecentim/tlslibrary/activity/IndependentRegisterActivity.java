package com.topsports.tecentim.tlslibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.topsports.tecentim.tlslibrary.helper.MResource;
import com.topsports.tecentim.tlslibrary.service.TLSService;


public class IndependentRegisterActivity extends Activity {

    public final static String TAG = "IndependentRegisterActivity";
    private TLSService tlsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MResource.getIdByName(this, "layout", "tencent_tls_ui_activity_independent_register"));

        tlsService = TLSService.getInstance();
        tlsService.initAccountRegisterService(this,
                (EditText) findViewById(MResource.getIdByName(this, "id", "username")),
                (EditText) findViewById(MResource.getIdByName(this, "id", "password")),
                (EditText) findViewById(MResource.getIdByName(this, "id", "repassword")),
                (Button) findViewById(MResource.getIdByName(this, "id", "btn_register"))
        );

        // 设置返回按钮
        findViewById(MResource.getIdByName(this, "id", "returnIndependentLoginActivity"))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IndependentRegisterActivity.this.onBackPressed();
                    }
                });
    }
}

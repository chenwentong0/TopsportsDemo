package com.example.chenwentong.helloworld.ui.splash;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.example.chenwentong.helloworld.MainActivity;
import com.example.chenwentong.helloworld.R;
import com.example.chenwentong.helloworld.base.BaseActivity;
import com.example.chenwentong.helloworld.common.UserConstants;
import com.example.chenwentong.helloworld.model.UserInfo;
import com.example.common.net.subscriber.BaseObjectSubscriber;
import com.example.common.utils.RxJavaUtil;
import com.example.common.utils.ToastUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.tlslibrary.activity.HostLoginActivity;
import com.tencent.qcloud.tlslibrary.activity.IndependentLoginActivity;
import com.tencent.qcloud.tlslibrary.activity.IndependentRegisterActivity;
import com.tencent.qcloud.tlslibrary.service.AccountLoginService;
import com.tencent.qcloud.tlslibrary.service.TLSService;

import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import io.reactivex.Flowable;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSHelper;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;


/**
 * @author wentong.chen
 * on 2018.8.24
 */
public class SplashActivity extends BaseActivity implements TIMCallBack {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    private void initRegister() {
        Log.d(TAG, "TIMManager 注册版本号是：" + TIMManager.getInstance().getVersion());
        TLSHelper tlsHelper = TLSHelper.getInstance();
        tlsHelper.TLSStrAccReg("imtest1", "11111111", new TLSStrAccRegListener() {
            @Override
            public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {
                Log.d(TAG, "注册成功");
            }

            @Override
            public void OnStrAccRegFail(TLSErrInfo tlsErrInfo) {
                Log.d(TAG, "注册失败" + tlsErrInfo.Msg + tlsErrInfo.ErrCode);
            }

            @Override
            public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo) {
                Log.d(TAG, "注册超时" + tlsErrInfo.Msg + tlsErrInfo.ErrCode);
            }
        });
    }

    @OnClick({R.id.btn_login, R.id.btn_register, R.id.btn_go_home})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //进入账号密码登录页面
                startActivity(IndependentLoginActivity.class);
                break;
            case R.id.btn_register:
                startActivity(IndependentRegisterActivity.class);
                break;
            case R.id.btn_go_home:
                if (isUserLogin()) {
                    startActivity(MainActivity.class);
                } else {

                }
                break;
            default:
        }
    }

    private void defaultAccount() {
        TLSService.getInstance().TLSPwdLogin("chenwentong", "11111111", new TLSPwdLoginListener() {
            @Override
            public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {
                String id = TLSService.getInstance().getLastUserIdentifier();
                UserInfo.getInstance().setId(tlsUserInfo.identifier);
                UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(tlsUserInfo.identifier));
                navToHome();
            }

            @Override
            public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {

            }

            @Override
            public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {

            }

            @Override
            public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {

            }

            @Override
            public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {

            }
        });
    }

    @Override
    protected void initData() {
//        initRegister();
        //引导页面延迟1秒打开
        String id = TLSService.getInstance().getLastUserIdentifier();
        UserInfo.getInstance().setId(id);
        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
        Flowable.timer(1000, TimeUnit.MILLISECONDS)
                .compose(RxJavaUtil.<Long>IO2Main())
                .subscribe(new BaseObjectSubscriber<Long>() {
                    @Override
                    public void onSuccess(Long aLong) {
                        if (isUserLogin()) {
                            navToHome();
                        } else {
                            defaultAccount();
//                            navToLogin();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        navToLogin();
                    }
                });
    }

    /**
     * 登录成功之后跳转到主页
     */
    public void navToHome() {
        LogUtils.d("登录成功 navToHome()");
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Log.d(TAG, "receive force offline message");
                ToastUtil.showLongToast("被踢下线，重新登录");
            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                ToastUtil.showLongToast("票据过期，需要重新登录");
            }
        })
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        Log.i(TAG, "onConnected");
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                        Log.i(TAG, "onDisconnected");
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                        Log.i(TAG, "onWifiNeedAuth");
                    }
                });

        //设置刷新监听
        RefreshEvent.getInstance().init(userConfig);
        userConfig = FriendshipEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);
        //登录IM，
//        UserInfo.getInstance().setUserSig("eJx1kEFPgzAYhu-8CtLrjGmBZtZkB2oI6JjCtqxzl4ZBIXWClXYiGv*7BE3k4nd9nuR5831atm2Dbby5zPL85dwYbnolgH1tgzlxwMUfVkoWPDPcbYsRIw8Oh7DrTizxrmQreFYa0Y6Wg4kzaBNFFqIxspS-Qt4ZhNCE6*LEx9j-FS2rEa6C9ObWD6OjqmADqbdmEZszr877is72p4cPsoT0bvsUBsk*YX3sS0r910OwzMIu3umVZhh39FA2z7N1dH9*xGlCMAnfUn11rP3FYpI0sv75yjAFux70sAusL*sbGnlVsg__");
        LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), this);
        startActivity(MainActivity.class);
    }

    /**
     * 未登录，跳转注册页面
     */
    public void navToLogin() {
        LogUtils.d("未登录，进入注册页面");
        startActivity(HostLoginActivity.class);
    }

    /**
     * 判断用户时候已经登录过了
     *
     * @return
     */
    public boolean isUserLogin() {
        return UserInfo.getInstance().getId() != null && (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d("requestCode =" + requestCode + "resultCode =" + resultCode);
        Log.d(TAG, "onActivityResult code:" + requestCode);
        if (UserConstants.LOGIN_RESULT_CODE == requestCode) {
            Log.d(TAG, "login error no " + TLSService.getLastErrno());
            if (0 == TLSService.getLastErrno()) {
                String id = TLSService.getInstance().getLastUserIdentifier();
                UserInfo.getInstance().setId(id);
                UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
                navToHome();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    public void onError(int errorCode, String s) {
        LogUtils.d("登录失败回调了 errorCode = " + errorCode);
    }

    @Override
    public void onSuccess() {
        LogUtils.d("登录成功回调了");
    }
}

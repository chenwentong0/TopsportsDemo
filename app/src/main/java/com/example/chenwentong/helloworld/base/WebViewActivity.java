package com.example.chenwentong.helloworld.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.apkfuns.logutils.LogUtils;
import com.example.chenwentong.helloworld.R;
import com.example.common.net.subscriber.BaseObjectSubscriber;
import com.example.common.utils.RxJavaUtil;
import com.example.common.utils.ToastUtil;
import com.example.common.widget.TitleBar;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Flowable;

/**
 * @author wentong.chen
 * on 16/2/22.
 * 通用的webview页面
 */
public class WebViewActivity extends BaseActivity {
    public static final String KEY_TITLE = "title";
    public static final String KEY_URL = "url";
    public static final String KEY_CONTENT = "content";

    @BindView(R.id.webView)
    protected WebView webView;

    @BindView(R.id.pb)
    protected ProgressBar pb;
    protected TitleBar mTitleView;

    protected WebSettings settings;
    protected String url;
    private boolean toastHasShow;
    private String title;

    @Override
    protected void initView() {
        title = getIntent().getStringExtra(KEY_TITLE);
        String url =  getIntent().getStringExtra(KEY_URL);
        String content = getIntent().getStringExtra(KEY_CONTENT);
        getTitleView().setTitle(title);
        getTitleView().setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {  //表示按返回键
                    webView.goBack();   //后退
                    LogUtils.d(TAG, "webview goback");
                } else {
                    finish();
                }
            }
        });

        settings = webView.getSettings();

        WebViewClient client = createWebViewClient();
        if (client != null) {
            webView.setWebViewClient(createWebViewClient());
        }
        webView.setWebChromeClient(createWebChromeClient());
        initWebview();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (!TextUtils.isEmpty(url)) {
            loadUrl(url);
        }

        if (!TextUtils.isEmpty(content)) {
            LogUtils.d("content: " + content);
            settings.setDefaultTextEncodingName("UTF-8") ;
            webView.loadData(content,"text/html","UTF-8");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public TitleBar getTitleView() {
        if (mTitleView == null) {
            mTitleView = findViewById(R.id.title_bar);
        }
        return mTitleView;
    }

    protected void initWebview() {
        //根据主题设置背景色
//        webView.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_windows));
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.requestFocusFromTouch();
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
    }

    protected WebViewClient createWebViewClient() {
        return new DefaultWebViewClient();
    }

    protected WebChromeClient createWebChromeClient() {
        return new DefaultWebChromeClient();
    }

    public void loadUrl(String url) {
        if (webView != null) {
            pb.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
        }
        this.url = url;

        LogUtils.d(">>load Url: " + url);
    }

    protected class DefaultWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Flowable.timer(200, TimeUnit.MILLISECONDS)
                    .compose(mLifecycleProvider.<Long>bindToLifecycle())
                    .compose(RxJavaUtil.<Long>IO2Main())
                    .subscribe(new BaseObjectSubscriber<Long>() {
                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(Long response) {
                            if (pb != null) {
                                pb.setVisibility(View.INVISIBLE);
                            }
                            webView.setVisibility(View.VISIBLE);
                        }
                    });
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    protected class DefaultWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 100) {
                // 网页加载完成
                LogUtils.e(">> progress: 100");
                pb.setVisibility(View.INVISIBLE);
//                if(!AndroidUtil.isNetworkAvailable(WebViewActivity.this) && !toastHasShow) {
//                    ToastUtil.showLongToast("当前网络不可用， 请检查网络连接");
//                    toastHasShow = true;
//                }
            }
            LogUtils.e(">>>>>> progress: " + newProgress);
        }

        @Override
        public Bitmap getDefaultVideoPoster() {
            if (Build.VERSION.SDK_INT < 24) {
                return super.getDefaultVideoPoster();
            }
            Bitmap result = super.getDefaultVideoPoster();
            if (result == null) {
                //TODO 视频播放图片替换
                Bitmap poster = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher);
                // The icon is transparent so we need to draw it
                // on a gray background.
                result = Bitmap.createBitmap(poster.getWidth(),
                        poster.getHeight(),
                        poster.getConfig());
                result.eraseColor(Color.GRAY);
                Canvas canvas = new Canvas(result);
                canvas.drawBitmap(poster, 0f, 0f, null);
            }
            return result;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                LogUtils.d(TAG, "webview goback");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        LogUtils.d("BridgeWebViewActivity: finish: ");
        webView.loadUrl("javascript:window.ytx_destroy()");

        super.finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }

            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();

            try {
                webView.destroy();
            } catch (Throwable ex) {

            }
        }
        super.onDestroy();
    }

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }

}

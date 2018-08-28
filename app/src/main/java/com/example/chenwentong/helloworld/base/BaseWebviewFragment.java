package com.example.chenwentong.helloworld.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ProgressBar;

import com.apkfuns.logutils.LogUtils;
import com.example.chenwentong.helloworld.R;
import com.example.common.net.subscriber.BaseObjectSubscriber;
import com.example.common.utils.RxJavaUtil;
import com.example.common.widget.webview.X5WebView;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Flowable;

/**
 * Date 2018/7/16
 * Time 14:10
 *
 * @author wentong.chen
 */
public abstract class BaseWebviewFragment extends BaseFragment {
    @BindView(R.id.webView)
    X5WebView webView;
    private WebSettings settings;
    @BindView(R.id.pb)
    ProgressBar pb;
    private String url;
    private boolean toastHasShow;

    @Override
    protected void initView() {
        initWebview();
    }

    protected void initWebview() {
        //根据主题设置背景色
//        webView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_windows));
        WebViewClient client = createWebViewClient();
        if (client != null) {
            webView.setWebViewClient(createWebViewClient());
        }
        webView.setWebChromeClient(createWebChromeClient());

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.requestFocusFromTouch();
        this.settings = webView.getSettings();
        this.settings.setJavaScriptEnabled(true);
        this.settings.setLoadWithOverviewMode(true);
        this.settings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
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
                            if (webView != null) {
                                webView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }

    protected class DefaultWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 100 && pb != null) {
                // 网页加载完成
                LogUtils.e(">> progress: 100");
                pb.setVisibility(View.INVISIBLE);
//                if(!AndroidUtil.isNetworkAvailable(getActivity()) && !toastHasShow) {
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
    public void onDestroy() {
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
}

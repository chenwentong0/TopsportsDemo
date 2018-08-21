package com.example.common.net.error;

import android.support.annotation.NonNull;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;


/**
 * create by wentong.chen 2018.1.25
 * 网络请求重试retry条件
 */
public class RetryWhenNetworkException implements Function<Observable<? extends Throwable>, Observable<?>> {
    //    retry次数
    private int mCount = 3;
    //    延迟
    private long mDelay = 3000;
    //    叠加延迟
    private long mIncreaseDelay = 3000;

    public RetryWhenNetworkException() {

    }

    public RetryWhenNetworkException(int count, long delay) {
        this.mCount = count;
        this.mDelay = delay;
    }

    public RetryWhenNetworkException(int count, long delay, long increaseDelay) {
        this.mCount = count;
        this.mDelay = delay;
        this.mIncreaseDelay = increaseDelay;
    }


    @Override
    public Observable<?> apply(@NonNull Observable<? extends Throwable> observable) throws Exception {
        return observable
                .zipWith(Observable.range(1, mCount + 1), new BiFunction<Throwable, Integer, Wrapper>() {
                    @Override
                    public Wrapper apply(@NonNull Throwable throwable, @NonNull Integer integer) throws Exception {
                        return new Wrapper(throwable, integer);
                    }
                }).flatMap(new Function<Wrapper, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Wrapper wrapper) throws Exception {
                        if ((wrapper.throwable instanceof ConnectException
                                || wrapper.throwable instanceof SocketTimeoutException
                                || wrapper.throwable instanceof TimeoutException)
                                && wrapper.index < mCount + 1) { //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
                            return Observable.timer(mDelay + (wrapper.index - 1) * mIncreaseDelay, TimeUnit.MILLISECONDS);

                        }
                        return Observable.error(wrapper.throwable);
                    }
                });
    }

    private class Wrapper {
        private int index;
        private Throwable throwable;

        public Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }

}

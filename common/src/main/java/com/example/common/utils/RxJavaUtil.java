package com.example.common.utils;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Date 2018/8/24
 * Time 10:38
 *
 * @author wentong.chen
 * RxJava 操作工具类
 */
public class RxJavaUtil {

    /**
     * IO线程执行 main线程订阅
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> IO2Main() {
        FlowableTransformer flowableTransformer = new FlowableTransformer<T, T>() {
            @Override
            public Publisher apply(Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
        return flowableTransformer;
    }

    /**
     * IO线程执行 main线程订阅
     * @param flowable
     * @param <T>
     * @return
     */
    public static <T> Flowable<T>  Main2IO(Flowable<T> flowable) {
        return flowable.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io());
    }
}

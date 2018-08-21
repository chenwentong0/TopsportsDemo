package com.example.chenwentong.helloworld.net;

import com.example.common.net.subscriber.BaseModel;

import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * @author by chen.wentong on 2018/8/17.
 */

public interface IService {
    String baseUrl = "http://wanandroid.com";

    /**
     * 测试借口
     * @return
     */
    @GET("tools/mockapi/9303/msg")
    Flowable<BaseModel> getMsg();
}

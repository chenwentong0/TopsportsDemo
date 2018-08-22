package com.example.common.utils.imageutil.glide;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.MemoryCategory;
import com.example.common.utils.imageutil.config.SingleConfig;


/**
 * Created by wentong.chen on 18/7/11.
 * 功能：加载图片对外暴露接口定义
 */
public interface ILoader {
    /**
     * 初始化图片加载设置
     * @param context
     * @param cacheSizeInM 设置内存缓存大小
     * @param memoryCategory
     * @param isInternalCD 是否缓存在内置存储卡还是外置存储
     */
    void init(Context context, int cacheSizeInM, MemoryCategory memoryCategory, boolean isInternalCD);

    /**
     * 请求获取图片
     * @param singleConfig 请求加载图片的一些配置参数
     */
    void request(SingleConfig singleConfig);

    /**
     * 清理内存缓存
     */
    void clearMemoryCache(View view);

    /**
     * 清理硬盘缓存
     */
    void clearDiskCache();

    void clearMemory();

    void trimMemory(int level);

    void clearAllMemoryCache();

    /**
     * 是否使用缓存
     * @param url
     */
    boolean isCache(String url);

    /**
     * 下载图片到相册中
     * @param downLoadImageService 图片下载服务
     */
    void saveImageIntoGallery(DownLoadImageService downLoadImageService);

    void pause();

    void resume();
}

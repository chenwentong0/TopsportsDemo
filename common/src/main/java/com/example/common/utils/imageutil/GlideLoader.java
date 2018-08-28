package com.example.common.utils.imageutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.ViewAnimationFactory;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

/**
 * Created by wentong.chen on 18/7/11.
 * 功能：glide
 */
class GlideLoader implements ILoader {

    @Override
    public void init(Context context, int cacheSizeInM, MemoryCategory memoryCategory, boolean isInternalCD) {
        //如果在应用当中想要调整内存缓存的大小，开发者可以通过如下方式：
        Glide.get(context).setMemoryCategory(memoryCategory);
        GlideBuilder builder = new GlideBuilder();
        if (isInternalCD) {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSizeInM * 1024 * 1024));
        } else {
            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheSizeInM * 1024 * 1024));
        }
    }

    @Override
    public void request(final SingleConfig config) {
        RequestManager requestManager = Glide.with(config.getContext());
        RequestBuilder request = getDrawableTypeRequest(config, requestManager);
        RequestOptions requestOptions = new RequestOptions();
        //图片加载配置
        //通过设置图片监听对图片进行自定义或显示操作
        if (config.isAsBitmap()) {
            SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition transition) {
                    config.getBitmapListener().onSuccess(bitmap);
                }
            };

            setShapeModeAndBlur(config, requestOptions);
            if (config.getDiskCacheStrategy() != null) {
                requestOptions.diskCacheStrategy(config.getDiskCacheStrategy());
            }
            request.apply(requestOptions).into(target);

        } else {

            if (request == null) {
                return;
            }

            if (config.isGif()) {
                requestManager.asGif();
            }

            if (ImgLoaderUtil.shouldSetPlaceHolder(config)) {
                requestOptions.placeholder(config.getPlaceHolderResId());
            }

            int scaleMode = config.getScaleMode();

            switch (scaleMode) {
                case ScaleMode.CENTER_CROP:
                    requestOptions.centerCrop();
                    break;
                case ScaleMode.FIT_CENTER:
                    requestOptions.fitCenter();
                    break;
                default:
                    requestOptions.fitCenter();
                    break;
            }

            setShapeModeAndBlur(config, requestOptions);

            //设置缩略图
            if (config.getThumbnail() != 0) {

                request.thumbnail(config.getThumbnail());
            }

            //设置图片加载的分辨 sp
            if (config.getoWidth() != 0 && config.getoHeight() != 0) {
                requestOptions.override(config.getoWidth(), config.getoHeight());
            }

            //是否跳过磁盘存储
            if (config.getDiskCacheStrategy() != null) {
                requestOptions.diskCacheStrategy(config.getDiskCacheStrategy());
            }
            //设置图片加载动画
//            setAnimator(config, request);

            //设置图片加载优先级
            setPriority(config, requestOptions);

            if (config.getErrorResId() > 0) {
                requestOptions.error(config.getErrorResId());
            }


            if (config.getTarget() instanceof ImageView) {
                request.apply(requestOptions).into((ImageView) config.getTarget());
            }
        }
    }

    /**
     * 设置加载优先级
     *
     * @param config
     * @param request
     */
    private void setPriority(SingleConfig config, RequestOptions request) {
        switch (config.getPriority()) {
            case PriorityMode.PRIORITY_LOW:
                request.priority(Priority.LOW);
                break;
            case PriorityMode.PRIORITY_NORMAL:
                request.priority(Priority.NORMAL);
                break;
            case PriorityMode.PRIORITY_HIGH:
                request.priority(Priority.HIGH);
                break;
            case PriorityMode.PRIORITY_IMMEDIATE:
                request.priority(Priority.IMMEDIATE);
                break;
            default:
                request.priority(Priority.IMMEDIATE);
                break;
        }
    }

    @Nullable
    private RequestBuilder getDrawableTypeRequest(SingleConfig config, RequestManager requestManager) {
        RequestBuilder request = null;
        if (! TextUtils.isEmpty(config.getUrl())) {
            request = requestManager.load(ImgLoaderUtil.appendUrl(config.getUrl()));
            Log.e("TAG","getUrl : "+config.getUrl());
        } else if (!TextUtils.isEmpty(config.getFilePath())) {
            request = requestManager.load(ImgLoaderUtil.appendUrl(config.getFilePath()));
            Log.e("TAG","getFilePath : "+config.getFilePath());
//        } else if (!TextUtils.isEmpty(config.getContentProvider())) {
//            requestManager.load()
//            request = requestManager.loadFromMediaStore(Uri.parse(config.getContentProvider()));
//            Log.e("TAG","getContentProvider : "+config.getContentProvider());
        } else if (config.getResId() > 0) {
            request = requestManager.load(config.getResId());
            Log.e("TAG","getResId : "+config.getResId());
        } else if(config.getFile() != null){
            request = requestManager.load(config.getFile());
            Log.e("TAG","getFile : "+config.getFile());
        } else if(!TextUtils.isEmpty(config.getAssertspath())){
            request = requestManager.load(config.getAssertspath());
            Log.e("TAG","getAssertspath : "+config.getAssertspath());
        } else if(!TextUtils.isEmpty(config.getRawPath())){
            request = requestManager.load(config.getRawPath());
            Log.e("TAG","getRawPath : "+config.getRawPath());
        }
        return request;
    }

    /**
     * 设置图片滤镜和形状
     *
     * @param config
     */
    private void setShapeModeAndBlur(SingleConfig config, RequestOptions requestOptions) {

        int count = 0;

        Transformation[] transformation = new Transformation[statisticsCount(config)];

        if (config.isNeedBlur()) {
            transformation[count] = new BlurTransformation(config.getBlurRadius());
            count++;
        }

        if (config.isNeedBrightness()) {
            transformation[count] = new BrightnessFilterTransformation(config.getBrightnessLeve()); //亮度
            count++;
        }

        if (config.isNeedGrayscale()) {
            transformation[count] = new GrayscaleTransformation(); //黑白效果
            count++;
        }

        if (config.isNeedFilteColor()) {
            transformation[count] = new ColorFilterTransformation(config.getFilteColor());
            count++;
        }

        if (config.isNeedSwirl()) {
            transformation[count] = new SwirlFilterTransformation(0.5f, 1.0f, new PointF(0.5f, 0.5f)); //漩涡
            count++;
        }

        if (config.isNeedToon()) {
            transformation[count] = new ToonFilterTransformation(); //油画
            count++;
        }

        if (config.isNeedSepia()) {
            transformation[count] = new SepiaFilterTransformation(); //墨画
            count++;
        }

        if (config.isNeedContrast()) {
            transformation[count] = new ContrastFilterTransformation(config.getContrastLevel()); //锐化
            count++;
        }

        if (config.isNeedInvert()) {
            transformation[count] = new InvertFilterTransformation(); //胶片
            count++;
        }

        if (config.isNeedPixelation()) {
            transformation[count] =new PixelationFilterTransformation(config.getPixelationLevel()); //马赛克
            count++;
        }

        if (config.isNeedSketch()) {
            transformation[count] =new SketchFilterTransformation(); //素描
            count++;
        }

        if (config.isNeedVignette()) {
            transformation[count] =new VignetteFilterTransformation(new PointF(0.5f, 0.5f),
                    new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f);//晕映
            count++;
        }

        switch (config.getShapeMode()) {
            case ShapeMode.RECT:

                break;
            case ShapeMode.RECT_ROUND:
                transformation[count] = new RoundedCornersTransformation
                        (config.getRectRoundRadius(), 0, RoundedCornersTransformation.CornerType.ALL);
                count++;
                break;
            case ShapeMode.OVAL:
                transformation[count] = new CircleCrop();
                count++;
                break;

            case ShapeMode.SQUARE:
                transformation[count] = new CropSquareTransformation();
                count++;
                break;
        }
        if (transformation.length != 0) {
            requestOptions.transforms(transformation);
        }

    }

    /**
     * 设置加载进入动画
     *
     * @param config
     * @param request
     */
    private void setAnimator(SingleConfig config, RequestBuilder request) {
        if (config.getAnimationType() == AnimationMode.ANIMATION_ID) {
            request.transition(GenericTransitionOptions.with(config.getAnimationId()));
        } else if (config.getAnimationType() == AnimationMode.ANIMATOR) {
            request.transition(GenericTransitionOptions.with(config.getAnimator()));
        } else if (config.getAnimationType() == AnimationMode.ANIMATION) {
            request.transition(GenericTransitionOptions.with(new ViewAnimationFactory(config.getAnimation())));
        }
    }

    /**
     * 计算图片转换的个数
     * @param config
     * @return
     */
    private int statisticsCount(SingleConfig config) {
        int count = 0;

        if (config.getShapeMode() == ShapeMode.OVAL || config.getShapeMode() == ShapeMode.RECT_ROUND || config.getShapeMode() == ShapeMode.SQUARE) {
            count++;
        }

        if (config.isNeedBlur()) {
            count++;
        }

        if (config.isNeedFilteColor()) {
            count++;
        }

        if (config.isNeedBrightness()) {
            count++;
        }

        if (config.isNeedGrayscale()) {
            count++;
        }

        if (config.isNeedSwirl()) {
            count++;
        }

        if (config.isNeedToon()) {
            count++;
        }

        if (config.isNeedSepia()) {
            count++;
        }

        if (config.isNeedContrast()) {
            count++;
        }

        if (config.isNeedInvert()) {
            count++;
        }

        if (config.isNeedPixelation()) {
            count++;
        }

        if (config.isNeedSketch()) {
            count++;
        }

        if (config.isNeedVignette()) {
            count++;
        }

        return count;
    }

    @Override
    public void clearMemoryCache(View view) {
        Glide.with(GlobalConfig.context).clear(view);
    }

    @Override
    public void clearDiskCache() {
        Glide.get(GlobalConfig.context).clearDiskCache();
    }

    @Override
    public void clearMemory() {
        Glide.get(GlobalConfig.context).clearMemory();
    }

    @Override
    public void trimMemory(int level) {
        Glide.get(GlobalConfig.context).trimMemory(level);
    }

    @Override
    public void clearAllMemoryCache() {
        Glide.get(GlobalConfig.context).onLowMemory();
    }

    @Override
    public boolean isCache(String url) {
        return false;
    }

    @Override
    public void saveImageIntoGallery(DownLoadImageService downLoadImageService) {
        downLoadImageService.loadBitmap();
    }

    @Override
    public void pause() {
        Glide.with(GlobalConfig.context).pauseRequestsRecursive();
    }

    @Override
    public void resume() {
        Glide.with(GlobalConfig.context).resumeRequestsRecursive();
    }
}

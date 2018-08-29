package com.example.common.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chen.wentong
 * on 2018/8/24
 */
public abstract class CBaseFragment extends RxLifeFragment implements IBaseView {

    protected View mRootView;
    /**
     * 是否初始化完毕
     */
    private boolean isPrepared;
    /**
     * 是否可见
     */
    private boolean isFragmentVisible;

    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    /**
     * <pre>
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     * 一般用于PagerAdapter需要刷新各个子Fragment的场景
     * 不要new 新的 PagerAdapter 而采取reset数据的方式
     * 所以要求Fragment重新走initData方法
     * 故使用 来让Fragment下次执行initData
     * </pre>
     */
    private boolean forceLoad = false;

    private List<BasePresenter> mBasePresenters = new ArrayList<>();
    /**
     * 是否初始化过
     */
    private boolean isInitData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        initView();
        if (!isUseLazyLoad()) {
            isFirstLoad = false;
            isInitData = true;
            initData();
        } else {
            lazyLoad();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
            initVariables(bundle);
        }
    }

    /**
     * 是否使用懒加载模式（默认不使用）
     * @return
     */
    protected boolean isUseLazyLoad() {
        return false;
    }

    /**
     * 获取bundle传值，省去判断操作
     * @param bundle
     */
    protected void initVariables(Bundle bundle) {

    }

    protected abstract @LayoutRes int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 每次可见的时候初始化数据
     */
    protected abstract void initData();


    /**
     * fragment 可见
     */
    protected void onVisible() {
        isFragmentVisible = true;
        lazyLoad();
        if (hasInit()) {
            onVisibleRefresh();
        }
    }

    protected void onVisibleRefresh() {

    }

    /**
     * fragment不可见
     */
    protected void onInvisible() {
        isFragmentVisible = false;
    }

    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    protected boolean isPrepared() {
        return isPrepared;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 懒加载初始化数据
     */
    protected void lazyLoad() {
        if (isPrepared() && isFragmentVisible()) {
            if (forceLoad || isFirstLoad()) {
                forceLoad = false;
                isFirstLoad = false;
                isInitData = true;
                initData();
            }
        }
    }

    /**
     * 是否第一次初始化
     * @return
     */
    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onInvisible();
        } else {
            onVisible();
        }
    }

    protected boolean hasInit() {
        return isInitData;
    }

    @Override
    public void onDestroyView() {
        if (mBasePresenters != null) {
            for (BasePresenter basePresenter : mBasePresenters) {
                basePresenter.onDestroy();
            }
            mBasePresenters.clear();
        }
        super.onDestroyView();
        isPrepared = false;
        isFirstLoad = true;
        forceLoad = false;
        isInitData = false;
    }

    @Override
    public IBaseView addPresenter(BasePresenter<? extends IBaseView> basePresenter) {
        if (mBasePresenters != null && basePresenter != null && !mBasePresenters.contains(basePresenter)) {
            mBasePresenters.add(basePresenter);
        }
        return this;
    }

    /**
     * 设置是否强制刷新
     * @param forceLoad
     */
    public void setForceLoad(boolean forceLoad) {
        this.forceLoad = forceLoad;
    }
}

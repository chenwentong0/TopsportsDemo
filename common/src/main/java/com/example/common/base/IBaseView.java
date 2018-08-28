package com.example.common.base;

/**
 * Created by wentong.chen on 18/1/25.
 * 功能：
 */
public interface IBaseView {
    /**
     * 设置presenter
     * @param basePresenter
     * @return 对应的view接口
     */
    IBaseView addPresenter(BasePresenter<? extends IBaseView> basePresenter);

}

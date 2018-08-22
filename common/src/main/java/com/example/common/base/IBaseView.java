package com.example.common.base;

/**
 * Created by wentong.chen on 18/1/25.
 * 功能：
 */
public interface IBaseView {
    /**
     * 设置presenter
     * @param basePresenter
     */
    void setPresenter(BasePresenter<? extends IBaseView> basePresenter);

}

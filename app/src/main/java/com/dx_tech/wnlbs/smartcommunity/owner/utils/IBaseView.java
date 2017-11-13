package com.dx_tech.wnlbs.smartcommunity.owner.utils;

/**
 * Created by 戴圣伟 on 0016 2017/10/16.
 *  基类显示view 弹出框，显示隐藏载入框
 */

public interface IBaseView {
    void showLoadingDialog(String string);
    void showFailure(String reason);
}

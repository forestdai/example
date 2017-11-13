package com.dx_tech.wnlbs.smartcommunity.owner.contract;

import com.dx_tech.wnlbs.smartcommunity.owner.bean.ExampleBean;
import com.dx_tech.wnlbs.smartcommunity.owner.utils.IBaseView;

import retrofit2.Callback;

/**
 * Created by 戴圣伟 on 0016 2017/10/16.
 */

public class ExampleContract {

    /**
     * 逻辑处理层
     */
    public interface IExampleModel {
        void example(String str1, String str2, Callback<ExampleBean> callback);
    }

    /**
     * V视图层
     */
    public interface IExampleView extends IBaseView {
        //对获取到的数据进行操作
        void setExampleResult(ExampleBean loginResult);
    }

    /**
     * P视图与逻辑处理的连接层
     */
    public interface IExamplePresenter {
        void example(String str1, String str2);
    }
}

package com.dx_tech.wnlbs.smartcommunity.owner.model;

import com.dx_tech.wnlbs.smartcommunity.owner.api.ApiService;
import com.dx_tech.wnlbs.smartcommunity.owner.bean.ExampleBean;
import com.dx_tech.wnlbs.smartcommunity.owner.contract.ExampleContract;
import com.dx_tech.wnlbs.smartcommunity.owner.utils.MD5Encoder;
import com.dx_tech.wnlbs.smartcommunity.owner.utils.RetrofitUtils;

import retrofit2.Callback;

import static com.dx_tech.wnlbs.smartcommunity.owner.utils.FXConstant.APPID;
import static com.dx_tech.wnlbs.smartcommunity.owner.utils.FXConstant.assembleStrings;
import static com.dx_tech.wnlbs.smartcommunity.owner.utils.FXConstant.getSignString;

public class ExampleModel implements ExampleContract.IExampleModel {

    @Override
    public void example(String name, String password, Callback<ExampleBean> callback) {
        //登录的网络请求
        String a = assembleStrings("appId" ,APPID, "loginName", name, "password", MD5Encoder.encode(password));
        String sign = getSignString(a);
        RetrofitUtils.createService(ApiService.class)//实例化Retrofit对象
                .example(name, MD5Encoder.encode(password), APPID, sign)
                .enqueue(callback);
    }
}

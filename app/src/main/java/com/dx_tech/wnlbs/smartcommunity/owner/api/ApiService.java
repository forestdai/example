package com.dx_tech.wnlbs.smartcommunity.owner.api;

import com.dx_tech.wnlbs.smartcommunity.owner.bean.ExampleBean;
import com.dx_tech.wnlbs.smartcommunity.owner.constant.HttpConstant;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 戴圣伟 on 0016 2017/10/16.
 * 服务器接口总入口
 * 可将所有接口写在此处
 */

public interface ApiService {
    @FormUrlEncoded
    @POST(HttpConstant.WEB_Example)
    Call<ExampleBean> example(@Field("name") String name, @Field("password") String password,
                              @Field("appId") String appId,
                              @Field("sign") String sign);

}

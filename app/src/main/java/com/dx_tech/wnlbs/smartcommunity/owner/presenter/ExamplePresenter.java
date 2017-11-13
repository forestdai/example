package com.dx_tech.wnlbs.smartcommunity.owner.presenter;

import com.dx_tech.wnlbs.smartcommunity.owner.bean.ExampleBean;
import com.dx_tech.wnlbs.smartcommunity.owner.contract.ExampleContract;
import com.dx_tech.wnlbs.smartcommunity.owner.model.ExampleModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public  class ExamplePresenter implements ExampleContract.IExamplePresenter{
    private ExampleContract.IExampleModel mModel;
    private ExampleContract.IExampleView mView;

    public ExamplePresenter(ExampleContract.IExampleView view) {
        mModel = new ExampleModel();
        mView = view;
    }
    @Override
    public void example(String name,String password){
        mView.showLoadingDialog("登录中...");
        mModel.example(name, password, new Callback<ExampleBean>() {
            @Override
            public void onResponse(Call<ExampleBean> call, Response<ExampleBean> response) {
                mView.setExampleResult(response.body());
            }

            @Override
            public void onFailure(Call<ExampleBean> call, Throwable t) {
                mView.showFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }
}

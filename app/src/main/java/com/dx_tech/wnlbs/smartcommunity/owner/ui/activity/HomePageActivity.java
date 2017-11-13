package com.dx_tech.wnlbs.smartcommunity.owner.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.dx_tech.wnlbs.smartcommunity.owner.R;
import com.dx_tech.wnlbs.smartcommunity.owner.bean.ExampleBean;
import com.dx_tech.wnlbs.smartcommunity.owner.contract.ExampleContract;
import com.dx_tech.wnlbs.smartcommunity.owner.presenter.ExamplePresenter;
import com.dx_tech.wnlbs.smartcommunity.owner.utils.HomeFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 戴圣伟 on 0017 2017/10/17.
 * 主页四个fragment展示Activity
 */

public class HomePageActivity extends BaseActivity implements /**接口调用示例**/ExampleContract.IExampleView {
    private Context mContext;

    @BindView(R.id.main_ViewPager)
    ViewPager mViewPager;

    @BindView(R.id.main_TabLayout)
    TabLayout mTableLayout;

    private ExamplePresenter mExamplePresenter;
    private HomeFragmentAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);

        initView();
        /**
         * 接口调用
         **/
        mExamplePresenter = new ExamplePresenter(this);
        mExamplePresenter.example("name","password");
    }

    private void initView(){
        mPageAdapter = new HomeFragmentAdapter(this);
        mViewPager.setAdapter(mPageAdapter);
        mTableLayout.setupWithViewPager(mViewPager, true);
        mTableLayout.setTabsFromPagerAdapter(mPageAdapter);
    }

    @Override
    public void onBackPressed() {
        boolean nochidcall = mBaseFragment == null || !mBaseFragment.onBackPressed();
        if(nochidcall){
            super.onBackPressed();
        }
    }
/*********************************************************************************/
    /**
     * 接口正常响应数据回调
    **/
    @Override
    public void setExampleResult(ExampleBean loginResult) {

    }

    /**
     * 接口等待回调。可选
     **/
    @Override
    public void showLoadingDialog(String string) {

    }
    /**
     * 接口异常回调
     **/
    @Override
    public void showFailure(String reason) {

    }
/*********************************************************************************/
}

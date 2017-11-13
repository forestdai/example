package com.dx_tech.wnlbs.smartcommunity.owner.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.dx_tech.wnlbs.smartcommunity.owner.R;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.BaseFragment;
import com.dx_tech.wnlbs.smartcommunity.owner.utils.FragmentMapManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 戴圣伟 on 0017 2017/10/17.
 * 内容展示Activity
 * 除主页四个以外所有内容的fragment展示均以此Activity管理
 * 包含页面无底部导航，可使用顶部actionbar
 */

public class DetailsActivity extends BaseActivity {
    private FragmentManager mFragmentManager;
    private FragmentMapManager mFragmentMapManager;

    @BindView(R.id.frameLayout_container)
    FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setupFragment();
    }

    private void setupFragment(){
        Intent intent = getIntent();
        String fragmentname = intent.getStringExtra("fragmentname");
        boolean extra = intent.getBooleanExtra("extra", false);
        if(fragmentname == null || fragmentname.equals("")){
            throw new NullPointerException("TransactionFragment is null!");
        }
        mFragmentManager = getSupportFragmentManager();
        setTransactionFragment(fragmentname, extra ? intent : null, false);
    }

    public void setTransactionFragment(String fragmentname, Intent intent){
        setTransactionFragment(fragmentname, intent, true);
    }

    public void setTransactionFragment(String fragmentname, Intent intent, boolean anim){
        mFragmentMapManager = FragmentMapManager.getInstance();
        setTransactionFragment(mFragmentMapManager.getFragment(fragmentname), intent, anim);
    }

    /**
     * DetailsActivity加载Fragment的主体方法
     * @param fragment 需要加载的Fargment类对象
     * @param  intent 需要传递的Intent
     * @param anim 该设置仅针对DetailsActivity被创建后第一页Fragment的进入动画
     *             true 确保第一页Fragment与Activity同时展现而无入场动画
     * */
    public void setTransactionFragment(BaseFragment fragment, Intent intent, boolean anim){
        if(fragment == null){
            return;
        }
        if(intent != null){
            fragment.onIntent(intent);
        }
        if(mFragmentContainer != null && mFragmentManager != null && !isFinishing()){
            String tag = fragment.getClass().getName();
            FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
            if(includeBackStack(tag)){
                mFragmentManager.popBackStackImmediate(tag, 0);
            }else{
                mTransaction.setCustomAnimations(
                        anim ? R.anim.fragment_slide_right_enter : 0,
                        R.anim.fragment_slide_left_exit,
                        R.anim.fragment_slide_left_enter,
                        R.anim.fragment_slide_right_exit);
                mTransaction.replace(R.id.frameLayout_container, fragment, tag);
                mTransaction.addToBackStack(tag);
                mTransaction.commitAllowingStateLoss();
            }
        }
    }

    private boolean includeBackStack(String name){
        mFragmentMapManager = FragmentMapManager.getInstance();
        int size = mFragmentManager.getBackStackEntryCount();
        for(int i = 0 ; i < size ; i++){
            if(name.equals(mFragmentManager.getBackStackEntryAt(i).getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(mBaseFragment != null && mBaseFragment.isAnim){
            //截断Fragment切换动画进行中的返回响应
            return;
        }
        boolean nochidcall = mBaseFragment == null || !mBaseFragment.onBackPressed();
        if(nochidcall){
            if(mFragmentManager.getBackStackEntryCount() < 2){
                //只有一个fragment时直接关闭，避免出现空白的activity
                DetailsActivity.this.finish();
            }else{
                super.onBackPressed();
            }
        }
    }
}

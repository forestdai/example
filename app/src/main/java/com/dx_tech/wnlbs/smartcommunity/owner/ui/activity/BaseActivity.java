package com.dx_tech.wnlbs.smartcommunity.owner.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.BackHandledInterface;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.BaseFragment;
import com.dx_tech.wnlbs.smartcommunity.owner.utils.FragmentMapManager;

/**
 * Created by 戴圣伟 on 0016 2017/10/16.
 * Activity父类
 */

public class BaseActivity extends AppCompatActivity implements BackHandledInterface {
    protected BaseFragment mBaseFragment;


    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        mBaseFragment = selectedFragment;
    }

    /**
     *Activity中若涉及多个Fragment页面管理
     * 调用 mBaseFragment.onBackPressed()
     * 以观察者模式监听所有Fragment的Back键
     * .example {@link HomePageActivity }
     **/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 由Activity直接跳转Detail的方法
     * @param cls 需要首次加载的Fargment类
     * */
    protected void setDetailsFragment(Class<?> cls){
        setDetailsFragment(cls, null);
    }

    /**
     * 由Activity直接跳转Detail的方法
     * @param cls 需要首次加载的Fargment类
     * @param  intent 需要传递的Intent
     * */
    protected void setDetailsFragment(Class<?> cls, Intent intent){
        setDetailsFragment(cls.getName(), intent);
    }

    /**
     * 由Activity直接跳转Detail的方法
     * @param classname 需要首次加载的Fargment类名
     * */
    protected void setDetailsFragment(String classname){
        setDetailsFragment(classname, null);
    }

    /**
     * 由Activity直接跳转Detail的方法
     * @param classname 需要首次加载的Fargment类名
     * @param  intent 需要传递的Intent
     * */
    protected void setDetailsFragment(String classname, Intent intent){
        if(intent == null){
            intent = new Intent();
            intent.putExtra("extra",false);
        }else{
            intent.putExtra("extra",true);
        }
        intent.setClass(this, DetailsActivity.class);
        intent.putExtra("fragmentname",classname);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BaseFragment fragment = FragmentMapManager.getInstance().getFragmentForCode(requestCode);
        if(fragment != null){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

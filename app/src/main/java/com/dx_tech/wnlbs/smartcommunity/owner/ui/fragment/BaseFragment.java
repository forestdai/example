package com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dx_tech.wnlbs.smartcommunity.owner.ui.activity.DetailsActivity;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.activity.HomePageActivity;
import com.dx_tech.wnlbs.smartcommunity.owner.utils.FragmentMapManager;

/**
 * Created by 戴圣伟 on 0016 2017/10/16.
 * Fragment父类
 */

public abstract class BaseFragment extends Fragment {

    protected BackHandledInterface mBackHandledInterface;

    public boolean isAnim = false;

    /**
     * Fragment中对Back键的截断
     * return true 拦截Back
     **/
    public abstract boolean onBackPressed();

    public void onIntent(Intent intent){}
    @Override
    public void onStart() {
        super.onStart();
        mBackHandledInterface.setSelectedFragment(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getActivity() instanceof BackHandledInterface)) {
            this.mBackHandledInterface = (BackHandledInterface) getActivity();
        } else {
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim < 1) {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
        Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        anim.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
                isAnim = true;
            }

            public void onAnimationRepeat(Animation animation) {
                isAnim = true;
            }

            public void onAnimationEnd(Animation animation) {
                isAnim = false;
            }
        });
        return anim;
    }

    /**
     * Fragment内跳转Fargment方法
     * @param classname 需要跳转Fragment的类名
     * */
    protected void setTransactionFragment(String classname){
        setTransactionFragment(classname, null);
    }

    /**
     * Fragment内跳转Fargment方法
     * @param classname 需要跳转Fragment的类名
     * @param intent 需要传递的Intent对象
     * */
    protected void setTransactionFragment(String classname, Intent intent){
        Activity activity = getActivity();
        if(activity instanceof DetailsActivity){
            DetailsActivity da = (DetailsActivity)activity;
            da.setTransactionFragment(classname, intent);
        }else{
            if(intent == null){
                intent = new Intent();
                intent.putExtra("extra",false);
            }else{
                intent.putExtra("extra",true);
            }
            intent.setClass(activity, DetailsActivity.class);
            intent.putExtra("fragmentname",classname);
            activity.startActivity(intent);
        }
    }

    /**
     * Fragment内跳转Fargment方法
     * @param cls 需要跳转Fragment的类
     * */
    protected void setTransactionFragment(Class<?> cls){
        String name = cls.getName();
        setTransactionFragment(name, null);
    }

    /**
     * Fragment内跳转Fargment方法
     * @param cls 需要跳转Fragment的类
     * @param intent 需要传递的Intent对象
     * */
    protected void setTransactionFragment(Class<?> cls, Intent intent){
        String name = cls.getName();
        setTransactionFragment(name, intent);
    }

    protected void startActivityFromFargment(Intent intent){
        getActivity().startActivityForResult(intent, FragmentMapManager.getInstance().getFragmentCode(this.getClass()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //确保每个fragment在与其依赖Activity脱离，
        //或者寄宿的Activity被销毁时从全局管理列表中清除
        FragmentMapManager.getInstance().deleteFragment(this.getClass());
    }
}

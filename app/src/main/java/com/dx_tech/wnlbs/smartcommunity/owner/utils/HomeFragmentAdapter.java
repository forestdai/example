package com.dx_tech.wnlbs.smartcommunity.owner.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.dx_tech.wnlbs.smartcommunity.owner.ui.activity.BaseActivity;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by 戴圣伟 on 0017 2017/10/17.
 * 主页四个fragment容器
 * 因使用FragmentPagerAdapter，针对较多页面将占用过多内存
 * 除非主页底部增加功能页面，否则不可使用此适配器增加更多fargment
 */

public class HomeFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment> mFragmentLsit;

    private ArrayList<String> titleList = new ArrayList<String>() {{
        add("主页");
        add("资讯");
        add("友邻");
        add("我的");
    }};


    public HomeFragmentAdapter(BaseActivity context) {
        super(context.getSupportFragmentManager());
        FragmentMapManager fm = FragmentMapManager.getInstance();
        mFragmentLsit = new ArrayList<>();
        for(Class<?> clas : FragmentMapManager.staticFragmentList){
            mFragmentLsit.add((BaseFragment)fm.getFragment(clas));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentLsit.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentLsit.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}

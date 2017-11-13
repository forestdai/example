package com.dx_tech.wnlbs.smartcommunity.owner.utils;

import android.support.v4.app.Fragment;

import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.BaseFragment;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.home.HomeFragment;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.information.InformationFragment;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.mysettings.MysettingsFragment;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.neighbour.NeighbourFragment;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by 戴圣伟 on 0017 2017/10/17.
 * Fragment管理类
 * 保存所有fargment实例对象
 * 常驻保存四个主页对象，提高体验
 * 销毁时删除
 */

public class FragmentMapManager {
    private static Map<String, Fragment> mFragmentMap = null;
    private List<String> mFragmentList = null;
    public static Class<?>[] staticFragmentList = new Class<?>[]{
            HomeFragment.class,
            InformationFragment.class,
            NeighbourFragment.class,
            MysettingsFragment.class
    };
    
    private static class Hodler{
        private static final FragmentMapManager INSTANCE = new FragmentMapManager();
    }

    private FragmentMapManager(){
        if(mFragmentList == null){
            mFragmentList = new ArrayList<>();
        }
        if(mFragmentMap == null ){
            mFragmentMap = new HashMap<String , Fragment>();
            for(Class<?> cls : staticFragmentList){
                String name = cls.getName();
                mFragmentMap.put(name, (BaseFragment)instanceClassforName(name));
                mFragmentList.add(name);
            }
        }

    }
    
    public static final FragmentMapManager getInstance(){
        return Hodler.INSTANCE;
    }

    public void deleteFragment(Class<?> cls){
        if(includeFragment(cls.getName()) && !ArrayUtils.contains(staticFragmentList, cls)){
            mFragmentMap.remove(cls.getName());
        }
    }

    public boolean includeFragment(String name){
        return mFragmentMap.containsKey(name);
    }
    
    public BaseFragment getFragment(Class<?> cls){
        return getFragment(cls.getName());
    }

    public BaseFragment getFragment(String name){
//        for(Class<?> cls : staticFragmentList){
//            if(name.equals(cls.getName())){
//                return (BaseFragment)instanceClassforName(name);
//            }
//        }
        if(includeFragment(name)){
            return (BaseFragment)mFragmentMap.get(name);
        }else{
            BaseFragment fragment = (BaseFragment)instanceClassforName(name);
            mFragmentMap.put(name, fragment);
            if(mFragmentList.indexOf(name) == -1 ){
                mFragmentList.add(name);
            }
            return fragment;
        }
    }

    public BaseFragment getFragmentForCode(int code){
        String name = mFragmentList.get(code);
        if(includeFragment(name)){
            return getFragment(name);
        }else{
            return null;
        }
    }

    public int getFragmentCode(Class<?> cls){
        return getFragmentCode(cls.getName());
    }

    public int getFragmentCode(String classname){
        return mFragmentList.indexOf(classname);
    }

    private Object instanceClassforName(String className){
        try {
            Class<?> fragmentclass = Class.forName(className);
            return fragmentclass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

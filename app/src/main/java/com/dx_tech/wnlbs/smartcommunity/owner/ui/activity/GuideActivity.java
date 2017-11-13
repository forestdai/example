package com.dx_tech.wnlbs.smartcommunity.owner.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.dx_tech.wnlbs.smartcommunity.owner.R;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.Login.LoginFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 戴圣伟 on 0018 2017/10/18
 * 首次启动的引导页.
 */

public class GuideActivity extends BaseActivity {
    @BindView(R.id.guide_viewpager)
    ViewPager mViewPager;

    private ArrayList<View> mViewList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initViewPager();
    }

    private void initViewPager(){
        LayoutInflater lf = LayoutInflater.from(this);
        ImageView imageView01 = new ImageView(this);
        imageView01.setBackgroundResource(R.drawable.launch_1);
        ImageView imageView02 = new ImageView(this);
        imageView02.setBackgroundResource(R.drawable.launch_2);
//        ImageView imageView03 = new ImageView(this);
//        imageView03.setBackgroundResource(R.drawable.launch_3);
        View view3 = lf.inflate(R.layout.view_guide_item_3, null);


        mViewList = new ArrayList<>();
        mViewList.add(imageView01);
        mViewList.add(imageView02);
        mViewList.add(view3);

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return mViewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViewList.get(position));

            }

            @Override
            public int getItemPosition(Object object) {

                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViewList.get(position);
                container.addView(view);
                if(position == mViewList.size() - 1){
                    Button btn = ButterKnife.findById(view, R.id.enter_button);
                    btn.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setDetailsFragment(LoginFragment.class);
                            finish();
//                            startHomeActivity();
                        }
                    });
                }
                return view;
            }

        };

        mViewPager.setAdapter(pagerAdapter);
    }

    private void startHomeActivity(){
        Intent it = new Intent(GuideActivity.this, HomePageActivity.class);
        startActivity(it);
        finish();
    }


}

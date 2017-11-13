package com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dx_tech.wnlbs.smartcommunity.owner.R;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.AFragment;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 戴圣伟 on 0017 2017/10/17.
 * 主页面
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.test_jump)
    Button mButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_fragment_layout, container, false);
        ButterKnife.bind(this, view);

        mButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.putExtra("test","来自主页Activity跳转的数据");
                setTransactionFragment(AFragment.class, it);
            }
        });
        return view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

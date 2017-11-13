package com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dx_tech.wnlbs.smartcommunity.owner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测试页面B
 */

public class BFragment extends BaseFragment {
    @BindView(R.id.back_home)
    Button mBackHome;
    @BindView(R.id.test_bfragment_text)
    TextView mTextView;

    Intent mIntent;
    @Override
    public void onIntent(Intent intent) {
        mIntent = intent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.b_layout, container, false);
        ButterKnife.bind(this, view);
        mTextView.setText(mIntent.getStringExtra("test"));
        mBackHome.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public boolean onBackPressed() {
        Intent it = new Intent();
        it.putExtra("test","来自BFragment页面返回的数据");
        setTransactionFragment(AFragment.class, it);
        return true;
    }
}

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
import com.dx_tech.wnlbs.smartcommunity.owner.ui.activity.GuideActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测试页面A
 */

public class AFragment extends BaseFragment {
    @BindView(R.id.test_jump)
    Button mButton;

    @BindView(R.id.test_afragment_text)
    TextView mTextView;

    Intent mIntent;

    @Override
    public void onIntent(Intent intent) {
        mIntent = intent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_layout, container, false);
        ButterKnife.bind(this, view);

        mTextView.setText(mIntent.getStringExtra("test"));
        mButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.putExtra("test","来自AFragment的数据传递");
//                setTransactionFragment(BFragment.class, it);
                it.setClass(getActivity(), GuideActivity.class);
                startActivityFromFargment(it);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

package com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dx_tech.wnlbs.smartcommunity.owner.R;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.activity.HomePageActivity;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.activity.LaunchActivity;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 戴圣伟 on 0018 2017/10/18.
 * 登录界面
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.login_button)
    Button mLoginBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_layout, container, false);
        ButterKnife.bind(this, view);

        mLoginBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHomeActivity();
            }
        });
        return view;
    }

    private void startHomeActivity(){
        Intent it = new Intent(getActivity(), HomePageActivity.class);
        startActivity(it);
        getActivity().finish();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

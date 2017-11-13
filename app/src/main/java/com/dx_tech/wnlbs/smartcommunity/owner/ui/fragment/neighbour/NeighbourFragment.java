package com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.neighbour;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dx_tech.wnlbs.smartcommunity.owner.R;
import com.dx_tech.wnlbs.smartcommunity.owner.ui.fragment.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by 戴圣伟 on 0017 2017/10/17.
 * 友邻圈页面
 */

public class NeighbourFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.neighbour_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

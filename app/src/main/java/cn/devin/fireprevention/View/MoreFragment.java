package cn.devin.fireprevention.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.devin.fireprevention.R;

/**
 * Created by Devin on 2017/12/2.
 * “我的” 界面
 */

public class MoreFragment extends Fragment{
    private static final String ARG_POSITION = "position";

    public static MoreFragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(ARG_POSITION,position);
        MoreFragment fragment = new MoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more,container,false);

        return rootView;
    }
}

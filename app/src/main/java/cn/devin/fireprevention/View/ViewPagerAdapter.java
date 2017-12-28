package cn.devin.fireprevention.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Devin on 2017/12/2.
 * ViewPaper 适配器
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{
    private final int PAGE_COUNT = 3;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MapFragment.newInstance(position);
            case 1:
                return TaskFragment.newInstance(position);
            case 2:
                return MoreFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}

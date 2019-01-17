package Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @SensorAPIViewPageAdapter
 * @author Henrik Olofsson
 * Used to control the TabLayout inside Assignment2Fragment.
 */
public class SensorAPIViewPageAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 2;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SensorAPIViewPageAdapter(FragmentManager fm){
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int pos) {
        return mFragmentList.get(pos);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}

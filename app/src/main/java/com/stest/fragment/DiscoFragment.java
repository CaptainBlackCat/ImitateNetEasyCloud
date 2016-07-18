package com.stest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stest.InnerFragment.AnchorFragment;
import com.stest.InnerFragment.ListFragment;
import com.stest.InnerFragment.RankingFragment;
import com.stest.InnerFragment.RecommendFragment;
import com.stest.neteasycloud.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Limuyang on 2016/7/7.
 */
public class DiscoFragment extends Fragment {

    @ViewInject(R.id.disco_tab)
    private TabLayout main_tab;
    @ViewInject(R.id.disco_viewPager)
    private ViewPager main_viewpager;
    private List<String> mTitleList = Arrays.asList("个性推荐", "歌单", "主播电台", "排行榜");
    private List<Fragment> fragments = new ArrayList<>(4);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void addView() {
        fragments.add(new RecommendFragment());
        fragments.add(new ListFragment());
        fragments.add(new AnchorFragment());
        fragments.add(new RankingFragment());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.disco_fragment, container, false);
        ViewUtils.inject(this, v);
        addView();
        MyAdapter myAdapter = new MyAdapter(getFragmentManager());
        main_viewpager.setAdapter(myAdapter);
        main_tab.setTabMode(TabLayout.MODE_FIXED);
        main_tab.setTabsFromPagerAdapter(myAdapter);
        main_tab.setupWithViewPager(main_viewpager);
        main_viewpager.setCurrentItem(0);
        return v;
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}

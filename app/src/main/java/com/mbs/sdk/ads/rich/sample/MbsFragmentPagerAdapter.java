package com.mbs.sdk.ads.rich.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

public class MbsFragmentPagerAdapter extends FragmentPagerAdapter {
  private List<Fragment> fragmentList;

  public MbsFragmentPagerAdapter(FragmentManager fm) {
    super(fm);
    fragmentList = Arrays.asList(new BannerFragment(),
                                 new InterstitialFragment(),
                                 new HalfWindowInterstitialFragment(),
                                 new AppStartFragment(),
                                 new NativeFragment(),
                                 new NativeTemplateFragment());
  }

  @Override
  public Fragment getItem(int position) {
    Fragment f;
    f = fragmentList.get(position);
    if (f == null) {
      f = new Fragment();
    }
    return f;
  }

  @Override
  public int getCount() {
    return fragmentList.size();
  }
}

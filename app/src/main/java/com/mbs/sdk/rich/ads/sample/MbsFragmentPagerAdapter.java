package com.mbs.sdk.rich.ads.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MbsFragmentPagerAdapter extends FragmentPagerAdapter {
  public MbsFragmentPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    Fragment f = new Fragment();
    switch (position) {
      case 0:
        f = new BannerFragment();
        break;
      case 1:
        f = new InterstitialFragment();
        break;
      case 2:
        f = new HalfWindowInterstitialFragment();
        break;
      case 3:
        f = new AppStartFragment();
        break;
      case 4:
        f = new NativeFragment();
        break;
      case 5:
        f = new NativeTemplateFragment();
        break;
      default:
        break;
    }
    return f;
  }

  @Override
  public int getCount() {
    return 4;
  }
}

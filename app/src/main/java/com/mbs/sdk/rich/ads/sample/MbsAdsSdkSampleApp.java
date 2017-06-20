package com.mbs.sdk.rich.ads.sample;

import android.app.Application;

import com.mbs.sdk.rich.ads.MSAdsSdk;

public class MbsAdsSdkSampleApp extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    MSAdsSdk.start(this, "5cef4650-fd27-d158-30ee-cdd01721bf92");
  }
}

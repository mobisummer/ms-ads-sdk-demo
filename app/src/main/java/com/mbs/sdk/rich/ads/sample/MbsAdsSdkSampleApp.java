package com.mbs.sdk.rich.ads.sample;

import android.app.Application;

import com.mbs.sdk.rich.ads.MSAdsSdk;

public class MbsAdsSdkSampleApp extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    MSAdsSdk.start(this, "fd30cd7d-2f70-6abc-2ace-915cf72b8a30");
  }
}

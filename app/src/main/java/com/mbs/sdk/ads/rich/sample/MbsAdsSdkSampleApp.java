package com.mbs.sdk.ads.rich.sample;

import android.app.Application;

import com.mbs.sdk.ads.rich.MsRichSdk;

public class MbsAdsSdkSampleApp extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    MsRichSdk.start(this);
  }
}

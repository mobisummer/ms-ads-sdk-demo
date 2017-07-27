package com.mbs.sdk.ads.rich.sample;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mbs.sdk.ads.rich.Ad;
import com.mbs.sdk.ads.rich.AdError;
import com.mbs.sdk.ads.rich.AdListener;
import com.mbs.sdk.ads.rich.AdRequest;
import com.mbs.sdk.ads.rich.AdRequestOption;
import com.mbs.sdk.ads.rich.NativeTemplateAd;

public class NativeTemplateFragment extends Fragment {
  private NativeTemplateAd mNativeTemplateAd;
  private FrameLayout mContentContainer;
  private LinearLayout mTopContainer;
  private Button mBtnLoad;
  private TextView mTvStatus;

  /**
   * You should use your own **PLACEMENT_ID** in production
   */
  private static final String PLACEMENT_ID = "1662684189370000_1769833153869305";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mNativeTemplateAd = new NativeTemplateAd(getActivity());
    mNativeTemplateAd.setAdListener(mAdListener);

    initView();
    initAction();
  }

  private void initView() {
    mBtnLoad = new Button(getActivity());
    mBtnLoad.setText(getString(R.string.load));

    mTvStatus = new TextView(getActivity());
    mTvStatus.setGravity(Gravity.CENTER);

    mTopContainer = new LinearLayout(getActivity());
    {
      mTopContainer.setOrientation(LinearLayout.VERTICAL);
      mTopContainer.addView(mBtnLoad);
      mTopContainer.addView(mTvStatus);
    }
  }

  private void initAction() {
    mBtnLoad.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBtnLoad.setEnabled(false);
        mTvStatus.setText(getString(R.string.ad_start_loading));
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        AdRequestOption option = AdRequestOption.newNativeTemplateBuilder()
                                                .largeSize()
                                                .backgroundColor(Color.BLACK)
                                                .btnBackgroundColor(Color.BLUE)
                                                .btnTextColor(Color.RED)
                                                .descriptionTextColor(Color.GRAY)
                                                .titleTextColor(Color.RED)
                                                .typeFace(font)
                                                .build();
        AdRequest request = AdRequest.newBuilder()
                                     .placementId(PLACEMENT_ID)
                                     .withOption(option)
                                     .build();
        mNativeTemplateAd.loadAd(request);
      }
    });
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    mContentContainer = new FrameLayout(getActivity());
    mContentContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                 ViewGroup.LayoutParams.MATCH_PARENT));

    mContentContainer.addView(mTopContainer,
                              new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                                           FrameLayout.LayoutParams.WRAP_CONTENT,
                                                           Gravity.TOP));

    mContentContainer.addView(mNativeTemplateAd,
                              new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                                           FrameLayout.LayoutParams.WRAP_CONTENT,
                                                           Gravity.BOTTOM));

    return mContentContainer;
  }

  @Override
  public void onDestroyView() {
    mContentContainer.removeAllViews();
    mContentContainer = null;
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
    mTopContainer.removeAllViews();
    mTopContainer = null;
    mNativeTemplateAd = null;
    super.onDestroy();
  }

  private final AdListener mAdListener = new AdListener() {
    @Override
    public void onAdLoaded(Ad ad) {
      if (ad == mNativeTemplateAd) {
        mTvStatus.setText(getString(R.string.ad_load_success));
      }
    }

    @Override
    public void onAdClosed(Ad ad) {
      if (ad == mNativeTemplateAd) {
        mTvStatus.setText(getString(R.string.ad_closed));
      }
    }

    @Override
    public void onAdShowed(Ad ad) {
      if (ad == mNativeTemplateAd) {
        mBtnLoad.setEnabled(true);
        mTvStatus.setText(getString(R.string.ad_showed));
      }
    }

    @Override
    public void onAdClicked(Ad ad) {
      if (ad == mNativeTemplateAd) {
        Toast.makeText(getActivity(), getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onAdError(Ad ad, AdError adError) {
      if (ad == mNativeTemplateAd) {
        mBtnLoad.setEnabled(true);
        mTvStatus.setText(getString(R.string.ad_load_error, adError.getErrorMessage()));
      }
    }
  };
}

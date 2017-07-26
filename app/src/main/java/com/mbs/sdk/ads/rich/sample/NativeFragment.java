package com.mbs.sdk.ads.rich.sample;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mbs.sdk.ads.rich.Ad;
import com.mbs.sdk.ads.rich.AdError;
import com.mbs.sdk.ads.rich.AdListener;
import com.mbs.sdk.ads.rich.AdRequest;
import com.mbs.sdk.ads.rich.ImageDownloader;
import com.mbs.sdk.ads.rich.ImageFilter;
import com.mbs.sdk.ads.rich.NativeAd;
import com.mbs.sdk.ads.rich.NativeAdAssets;

public class NativeFragment extends Fragment {
  private static final String TAG = "NativeFragment";
  private NativeAd mNativeAd;
  private FrameLayout mContentContainer;
  private LinearLayout mTopContainer;
  private FrameLayout mAdMarkContainer;
  private Button mBtnLoad;
  private Button mBtnShow;
  private TextView mTvStatus;

  private LinearLayout mNativeAssetsContainer;
  private ImageView mIvIcon;
  private TextView mTvTitle;
  private TextView mTvRating;
  private ImageView mIvCover;
  private Button mBtnCTA;

  /**
   * You should use your own **PLACEMENT_ID** in production
   */
  private static final String PLACEMENT_ID = "1662684189370000_1769833153869304";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mNativeAd = new NativeAd(getActivity());
    mNativeAd.setAdListener(mAdListener);

    initView();
    initAction();
  }

  private void initView() {
    mBtnLoad = new Button(getActivity());
    mBtnLoad.setText(getString(R.string.load));

    mBtnShow = new Button(getActivity());
    mBtnShow.setText(getString(R.string.show));
    mBtnShow.setEnabled(false);

    mTvStatus = new TextView(getActivity());
    mTvStatus.setGravity(Gravity.CENTER);

    mTopContainer = new LinearLayout(getActivity());
    {
      mTopContainer.setOrientation(LinearLayout.VERTICAL);
      mTopContainer.addView(mBtnLoad);
      mTopContainer.addView(mBtnShow);
      mTopContainer.addView(mTvStatus);
    }

    mNativeAssetsContainer = new LinearLayout(getActivity());
    mNativeAssetsContainer.setOrientation(LinearLayout.VERTICAL);
    mNativeAssetsContainer.setVisibility(View.GONE);
    mNativeAssetsContainer.setBackgroundColor(getResources().getColor(R.color.bg_native_assets_container));
    {
      LinearLayout titleLine = new LinearLayout(getActivity());
      titleLine.setOrientation(LinearLayout.VERTICAL);
      mTvTitle = new TextView(getActivity());
      mTvTitle.setGravity(Gravity.CENTER);
      titleLine.addView(mTvTitle,
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                      LinearLayout.LayoutParams.WRAP_CONTENT));
      mTvRating = new TextView(getActivity());
      mTvRating.setGravity(Gravity.CENTER);
      titleLine.addView(mTvRating,
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                      LinearLayout.LayoutParams.WRAP_CONTENT));

      LinearLayout iconTitleLine = new LinearLayout(getActivity());
      iconTitleLine.setOrientation(LinearLayout.HORIZONTAL);
      iconTitleLine.setGravity(Gravity.CENTER);
      mIvIcon = new ImageView(getActivity());
      iconTitleLine.addView(mIvIcon,
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                          LinearLayout.LayoutParams.WRAP_CONTENT));
      iconTitleLine.addView(titleLine,
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                          LinearLayout.LayoutParams.WRAP_CONTENT,
                                                          1));
      mBtnCTA = new Button(getActivity());
      iconTitleLine.addView(mBtnCTA,
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                          LinearLayout.LayoutParams.WRAP_CONTENT));

      mNativeAssetsContainer.addView(iconTitleLine,
                                     new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                   LinearLayout.LayoutParams.WRAP_CONTENT));
      mIvCover = new ImageView(getActivity());
      mNativeAssetsContainer.addView(mIvCover,
                                     new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                   LinearLayout.LayoutParams.WRAP_CONTENT));
      mAdMarkContainer = new FrameLayout(getContext());
    }
  }

  private void initAction() {
    mBtnLoad.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBtnLoad.setEnabled(false);
        mBtnShow.setEnabled(false);
        mTvStatus.setText(getString(R.string.ad_start_loading));

        AdRequest request = AdRequest.newBuilder().placementId(PLACEMENT_ID).build();
        mNativeAd.loadAd(request);
      }
    });

    mBtnShow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBtnLoad.setEnabled(true);
        mBtnShow.setEnabled(false);

        NativeAdAssets assets = mNativeAd.getNativeAdAssets();
        if (assets == null) {
          // Usually this won't happen here.
          mTvStatus.setText(getString(R.string.ad_load_error, "Invalid native ad assets"));
          return;
        }
        /**
         * This is only an example for how to layout native ad assets.
         * Developer should follow their own design and handle all the unexpected situation,
         * such as no icon or cover.
         */
        // icon
        {
          NativeAdAssets.Image icon = assets.getIcon();
          BitmapDrawable fallback = new BitmapDrawable(getResources(),
                                                       BitmapFactory.decodeResource(getResources(),
                                                                                    R.drawable.icon_stub));
          if (icon != null && !TextUtils.isEmpty(icon.getUrl())) {
            ImageDownloader.downloadAndDisplayImage(icon, mIvIcon, fallback);
          } else {
            mIvIcon.setImageDrawable(fallback);
          }
        }
        // cover
        {
          NativeAdAssets.Image cover = ImageFilter.filter(assets.getCovers(), 600, 314);
          BitmapDrawable fallback = new BitmapDrawable(getResources(),
                                                       BitmapFactory.decodeResource(getResources(),
                                                                                    R.drawable.cover_stub));
          if(cover == null && assets.getCovers() != null && assets.getCovers().size() > 0){
            cover = assets.getCovers().get(0);
          }
          if (!TextUtils.isEmpty(cover.getUrl())) {
            ImageDownloader.downloadAndDisplayImage(cover, mIvCover, fallback);
          } else {
            mIvCover.setImageDrawable(fallback);
          }
        }
        mTvTitle.setText(assets.getTitle());
        mTvRating.setText(getString(R.string.app_star, assets.getRating()));
        mBtnCTA.setText(assets.getCallToAction());
        mNativeAd.registerViewForInteraction(mNativeAssetsContainer, mBtnCTA);

        mNativeAssetsContainer.setVisibility(View.VISIBLE);
        mAdMarkContainer.addView(mNativeAd.getNativeAdAssets().getAdMarkView());
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

    mContentContainer.addView(mNativeAssetsContainer,
                              new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                                           FrameLayout.LayoutParams.WRAP_CONTENT,
                                                           Gravity.BOTTOM));
    mContentContainer.addView(mAdMarkContainer,
                              new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                                           FrameLayout.LayoutParams.WRAP_CONTENT,
                                                           Gravity.BOTTOM | Gravity.RIGHT));

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
    mNativeAd = null;
    super.onDestroy();
  }

  private final AdListener mAdListener = new AdListener() {
    @Override
    public void onAdLoaded(Ad ad) {
      if (ad == mNativeAd) {
        mBtnShow.setEnabled(true);
        mTvStatus.setText(getString(R.string.ad_load_success));
      }
    }

    @Override
    public void onAdClosed(Ad ad) {
      mTvStatus.setText(getString(R.string.ad_closed));
    }

    @Override
    public void onAdShowed(Ad ad) {
      if (ad == mNativeAd) {
        mTvStatus.setText(getString(R.string.ad_showed));
      }
    }

    @Override
    public void onAdClicked(Ad ad) {
      if (ad == mNativeAd) {
        Toast.makeText(getActivity(), getString(R.string.ad_clicked), Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onAdError(Ad ad, AdError adError) {
      if (ad == mNativeAd) {
        mBtnLoad.setEnabled(true);
        mBtnShow.setEnabled(false);
        mTvStatus.setText(getString(R.string.ad_load_error, adError.getErrorMessage()));
      }
    }
  };
}
package com.taisenjay.pocketmoo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.taisenjay.pocketmoo.R;

import uk.co.senab.photoview.PhotoView;

public class BigImageActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_URL = "big_image";

    private PhotoView bigPhpto;

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);

        mUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        Log.e("url", mUrl);

        initView();
    }

    private void initView() {
        bigPhpto = (PhotoView) findViewById(R.id.big_phpto);

        Glide.with(this).load(mUrl)
                .placeholder(R.mipmap.img_default)
                .into(bigPhpto);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}

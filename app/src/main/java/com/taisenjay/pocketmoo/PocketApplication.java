package com.taisenjay.pocketmoo;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;

/**
 * Author : WangJian
 * Date   : 2017/7/2
 * Created by a handsome boy with love
 */

public class PocketApplication extends Application {

    String AD_MOB_ID = "ca-app-pub-8800040129375841~6828648415";

    public static Context globeContext;
    private static PocketApplication mInstance;

    public static PocketApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        globeContext = this.getApplicationContext();

        MobileAds.initialize(this, AD_MOB_ID);
    }

}

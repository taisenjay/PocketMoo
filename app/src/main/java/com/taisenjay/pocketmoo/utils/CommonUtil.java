package com.taisenjay.pocketmoo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.taisenjay.pocketmoo.PocketApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Author : WangJian
 * Date   : 2017/7/1
 * Created by a handsome boy with love
 */

public class CommonUtil {

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
//            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static String getSystemInfo(Context context){
        StringBuilder sb = new StringBuilder();
        sb.append("App ").append(getVersionName(context)).append(" ")
                .append("Model ").append(Build.MODEL).append(" ")
                .append("OS ").append(Build.VERSION.RELEASE);
        return sb.toString();
    }


    public static void setSP(String key,Object value){
        SPUtils.setSP(PocketApplication.getInstance(),key,value);
    }

    public static Object getSP(String key,Object defaultObj){
        return SPUtils.getSp(PocketApplication.getInstance(),key,defaultObj);
    }

    public static void webViewLoadData(WebView wv, String baseUrl, String data) {
        wv.loadDataWithBaseURL(baseUrl, data, "text/html", "utf-8", null);
    }

    //初始化webview
    public static void initWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放


/**
 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
 *//*
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);*/
    }

    public static String makeHtmlImgFitScreen(String htmltext) {

        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }

        Log.d("VACK", doc.toString());
        return doc.toString();
    }

}

package com.example.samplestickerapp.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

//adb shell setprop debug.firebase.analytics.app com.lmeter
public class Analytics {

    private static FirebaseAnalytics firebaseAnalytics;

    public static void init(Application app) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(app);
    }

    public static void setCurrentScreen(Activity activity, String screenName) {
        firebaseAnalytics.setCurrentScreen(activity, screenName, null);
    }

    public static void setCurrentScreen(Activity activity, String screenName,Class aClass) {
        firebaseAnalytics.setCurrentScreen(activity, screenName, aClass.getSimpleName());
    }

    public static void trackWithGroupId(String event, String postId) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.GROUP_ID, postId);
        track(event, bundle);
    }

    public static void track(String event) {
        track(event, null);
    }

    public static void track(String event, Bundle bundle) {
        firebaseAnalytics.logEvent(event, bundle);

    }

    public interface Param {
        String GROUP_ID = "group_id";

    }

    public interface AnalyticsEvents {

        String STICKER_ADDED = "sticker_added";
        String STICKER_FAV = "sticker_fav_added";
        String STICKER_UNFAV = "sticker_fav_removed";
        String APP_SHARE = "app_share";

    }


    public interface Screens {
        String HOME_SCREEN = "home";
        String TRENDING_SCREEN = "trending";
        String FAV_SCREEN = "favorite";
        String STICKER_DETAILS = "sticker";
        String STICKER_BANNER_VIEW = "sticker_banner";
    }


}
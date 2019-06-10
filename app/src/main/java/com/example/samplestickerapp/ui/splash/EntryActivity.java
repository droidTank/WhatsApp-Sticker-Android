/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.example.samplestickerapp.ui.splash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.samplestickerapp.BuildConfig;
import com.example.samplestickerapp.R;
import com.example.samplestickerapp.StickerApplication;
import com.example.samplestickerapp.data.local.AppDatabase;
import com.example.samplestickerapp.data.local.entities.StickerPack;
import com.example.samplestickerapp.data.remote.ApiService;
import com.example.samplestickerapp.fcm.MyFirebaseMessagingService;
import com.example.samplestickerapp.provider.ContentFileParser;
import com.example.samplestickerapp.ui.base.BaseActivity;
import com.example.samplestickerapp.ui.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryActivity extends BaseActivity {
    private static final String TAG = EntryActivity.class.getSimpleName();
    boolean isVersionFetched;
    boolean isDataFetched;
    private View progressBar;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        progressBar = findViewById(R.id.entry_activity_progress);
        overridePendingTransition(0, 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        setRemoteConfig();
        MyFirebaseMessagingService.checkAndSentFcmToken();
        fetchData();

    }

    private void initData() {
        if (isVersionFetched && isDataFetched) {
            progressBar.setVisibility(View.GONE);
            final Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    void setRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        long cacheExpiration = 12 * 60 * 60;// 12 hrs
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            //cacheExpiration = 100;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                        } else {

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }

    void fetchData() {
        ApiService.getApiClient().appVersion().enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    int version_code = response.body().getAsJsonObject().get("version_code").getAsInt();
                    if (BuildConfig.VERSION_CODE < version_code) {
                        forceUpdate();
                    } else {
                        isVersionFetched = true;
                        initData();
                    }


                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                isVersionFetched = true;
                initData();
            }
        });


        ApiService.getApiClient().stickers(String.valueOf(BuildConfig.STICKER_APP_ID)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    isDataFetched = true;
                    assert response.body() != null;
                    String jsonObject = response.body().string();
                    if (response.isSuccessful()) {
                        List<StickerPack> data = null;
                        data = ContentFileParser.parseStickerPacks(jsonObject);
                        Log.i(TAG, "onResponse: " + data.size());
                        AppDatabase.getInstance(StickerApplication.getAppContext()).subjectDao().insertStickerAndPack(data);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                initData();

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                isDataFetched = true;
                initData();

            }
        });


    }

    private void forceUpdate() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Update")
                .setMessage("Please update your app in Play Store")
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Intent view = new Intent();
                    view.setAction(Intent.ACTION_VIEW);
                    view.setData(Uri.parse("http://www.google.com"));
                    startActivity(view);
                });
        dialogBuilder.show();


    }
}

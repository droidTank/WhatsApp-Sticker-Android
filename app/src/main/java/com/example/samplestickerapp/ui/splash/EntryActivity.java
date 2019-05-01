/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.example.samplestickerapp.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samplestickerapp.BuildConfig;
import com.example.samplestickerapp.R;
import com.example.samplestickerapp.model.StickerPack;
import com.example.samplestickerapp.provider.StickerPackLoader;
import com.example.samplestickerapp.ui.base.BaseActivity;
import com.example.samplestickerapp.ui.detail.StickerPackDetailsActivity;
import com.example.samplestickerapp.ui.home.StickerPackListActivity;
import com.example.samplestickerapp.utils.AppPrefManager;
import com.example.samplestickerapp.utils.StickerPackValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class EntryActivity extends BaseActivity {
    private static final String TAG = EntryActivity.class.getSimpleName();
    private View progressBar;
    private LoadListAsyncTask loadListAsyncTask;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        overridePendingTransition(0, 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setRemoteConfig();

    }

    private void initData() {
        progressBar = findViewById(R.id.entry_activity_progress);
        loadListAsyncTask = new LoadListAsyncTask(this);
        loadListAsyncTask.execute();
    }

    private void showStickerPack(ArrayList<StickerPack> stickerPackList) {
        progressBar.setVisibility(View.GONE);
        if (stickerPackList.size() > 1 || true) {
            final Intent intent = new Intent(this, StickerPackListActivity.class);
            intent.putParcelableArrayListExtra(StickerPackListActivity.EXTRA_STICKER_PACK_LIST_DATA, stickerPackList);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        } else {
            final Intent intent = new Intent(this, StickerPackDetailsActivity.class);
            intent.putExtra(StickerPackDetailsActivity.EXTRA_SHOW_UP_BUTTON, false);
            intent.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_DATA, stickerPackList.get(0));
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    private void showErrorMessage(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        Log.e("EntryActivity", "error fetching sticker packs, " + errorMessage);
        final TextView errorMessageTV = findViewById(R.id.error_message);
        errorMessageTV.setText(getString(R.string.error_message, errorMessage));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadListAsyncTask != null && !loadListAsyncTask.isCancelled()) {
            loadListAsyncTask.cancel(true);
        }
    }

    void downloadMainJsonFile() {
        String stickerFileName = mFirebaseRemoteConfig.getString("sticker_file_name");
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        File stickersJsonFile = new File(getFilesDir(), stickerFileName);
        if (!stickersJsonFile.exists())
            mStorageRef.child("stickers").child("data").child(stickerFileName).getFile(stickersJsonFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    AppPrefManager.getInstance(getApplicationContext()).putString(AppPrefManager.JSON_FILE_PATH, stickersJsonFile.getAbsolutePath());
                    initData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EntryActivity.this, "error ", Toast.LENGTH_SHORT).show();
                    initData();
                }
            });
        else
            initData();
    }

    void setRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        HashMap<String, Object> map = new HashMap<>();
        map.put("sticker_file_name", "stickers-v1.json");
        mFirebaseRemoteConfig.setDefaults(map);
        long cacheExpiration = 3600;// 1 hrs
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 1000;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                            Log.i(TAG, "Fetch Succeeded: ");
                            Toast.makeText(EntryActivity.this, "Fetch Succeeded", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, "Fetch Failed: ");
                            Toast.makeText(EntryActivity.this, "Fetch Failed", Toast.LENGTH_SHORT).show();

                        }
                        downloadMainJsonFile();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(EntryActivity.this, "error occured", Toast.LENGTH_SHORT).show();
            }
        });


    }

    static class LoadListAsyncTask extends AsyncTask<Void, Void, Pair<String, ArrayList<StickerPack>>> {
        private final WeakReference<EntryActivity> contextWeakReference;

        LoadListAsyncTask(EntryActivity activity) {
            this.contextWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected Pair<String, ArrayList<StickerPack>> doInBackground(Void... voids) {
            ArrayList<StickerPack> stickerPackList;
            try {
                final Context context = contextWeakReference.get();
                if (context != null) {
                    stickerPackList = StickerPackLoader.fetchStickerPacks(context);
                    if (stickerPackList.size() == 0) {
                        return new Pair<>("could not find any packs", null);
                    }
                    for (StickerPack stickerPack : stickerPackList) {
                        StickerPackValidator.verifyStickerPackValidity(context, stickerPack);

                    }

                    return new Pair<>(null, stickerPackList);
                } else {
                    return new Pair<>("could not fetch sticker packs", null);
                }
            } catch (Exception e) {
                Log.e("EntryActivity", "error fetching sticker packs", e);
                return new Pair<>(e.getMessage(), null);
            }
        }

        @Override
        protected void onPostExecute(Pair<String, ArrayList<StickerPack>> stringListPair) {

            final EntryActivity entryActivity = contextWeakReference.get();
            if (entryActivity != null) {
                if (stringListPair.first != null) {
                    entryActivity.showErrorMessage(stringListPair.first);
                } else {
                    entryActivity.showStickerPack(stringListPair.second);
                }
            }
        }
    }
}

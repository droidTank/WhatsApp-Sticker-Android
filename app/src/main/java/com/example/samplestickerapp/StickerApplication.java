/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.example.samplestickerapp;

import android.app.Application;
import android.content.Context;

import com.example.samplestickerapp.data.local.entities.StickerPack;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;

public class StickerApplication extends Application {
   public static StickerApplication appContext;
  public ArrayList<StickerPack> stickerPacks;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        FirebaseApp.initializeApp(this);
        appContext=this;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static StickerApplication getInstance(){
        return appContext;
    }


}

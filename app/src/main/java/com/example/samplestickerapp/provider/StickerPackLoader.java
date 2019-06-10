/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.example.samplestickerapp.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.samplestickerapp.BuildConfig;
import com.example.samplestickerapp.data.local.AppDatabase;
import com.example.samplestickerapp.data.local.entities.Sticker;
import com.example.samplestickerapp.data.local.entities.StickerPack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.example.samplestickerapp.provider.StickerContentProvider.ANDROID_APP_DOWNLOAD_LINK_IN_QUERY;
import static com.example.samplestickerapp.provider.StickerContentProvider.IOS_APP_DOWNLOAD_LINK_IN_QUERY;
import static com.example.samplestickerapp.provider.StickerContentProvider.LICENSE_AGREENMENT_WEBSITE;
import static com.example.samplestickerapp.provider.StickerContentProvider.PRIVACY_POLICY_WEBSITE;
import static com.example.samplestickerapp.provider.StickerContentProvider.PUBLISHER_EMAIL;
import static com.example.samplestickerapp.provider.StickerContentProvider.PUBLISHER_WEBSITE;
import static com.example.samplestickerapp.provider.StickerContentProvider.STICKER_FILE_EMOJI_IN_QUERY;
import static com.example.samplestickerapp.provider.StickerContentProvider.STICKER_FILE_NAME_IN_QUERY;
import static com.example.samplestickerapp.provider.StickerContentProvider.STICKER_PACK_ICON_IN_QUERY;
import static com.example.samplestickerapp.provider.StickerContentProvider.STICKER_PACK_IDENTIFIER_IN_QUERY;
import static com.example.samplestickerapp.provider.StickerContentProvider.STICKER_PACK_NAME_IN_QUERY;
import static com.example.samplestickerapp.provider.StickerContentProvider.STICKER_PACK_PUBLISHER_IN_QUERY;
import static com.example.samplestickerapp.provider.StickerContentProvider.STICKER_PACK_SIZE;
import static com.example.samplestickerapp.provider.StickerContentProvider.STICKER_PACK_TRAY_ICON_URL;

public class StickerPackLoader {






    public static byte[] fetchStickerAsset(@NonNull final int identifier, @NonNull final String name, ContentResolver contentResolver) throws IOException {
        try (final InputStream inputStream = contentResolver.openInputStream(getStickerAssetUri(identifier, name));
             final ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            if (inputStream == null) {
                throw new IOException("cannot read sticker asset:" + identifier + "/" + name);
            }
            int read;
            byte[] data = new byte[16384];

            while ((read = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
            return buffer.toByteArray();
        }
    }

    private static Uri getStickerListUri(int id) {
        String identifier = String.valueOf(id);

        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(StickerContentProvider.STICKERS).appendPath(identifier).build();
    }

    public static Uri getStickerAssetUri(int identifier, String stickerName) {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(StickerContentProvider.STICKERS_ASSET).appendPath(String.valueOf(identifier)).appendPath(stickerName).build();
    }
}

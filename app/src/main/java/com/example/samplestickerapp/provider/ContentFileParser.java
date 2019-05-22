/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.example.samplestickerapp.provider;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.util.JsonReader;

import com.example.samplestickerapp.model.Sticker;
import com.example.samplestickerapp.model.StickerPack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentFileParser {


    @NonNull
    static List<StickerPack> parseStickerPacks(@NonNull InputStream contentsInputStream) throws IOException, IllegalStateException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(contentsInputStream))) {
            return readStickerPacks(reader);
        }
    }

    @NonNull
    private static List<StickerPack> readStickerPacks(@NonNull JsonReader reader) throws IOException, IllegalStateException {
        List<StickerPack> stickerPackList = new ArrayList<>();
        String androidPlayStoreLink = null;
        String iosAppStoreLink = null;

            reader.beginArray();
            while (reader.hasNext()) {
                StickerPack stickerPack = readStickerPack(reader);
                stickerPackList.add(stickerPack);
            }
            reader.endArray();
        if (stickerPackList.size() == 0) {
            throw new IllegalStateException("sticker pack list cannot be empty");
        }
        for (StickerPack stickerPack : stickerPackList) {
            stickerPack.setAndroidPlayStoreLink(androidPlayStoreLink);
            stickerPack.setIosAppStoreLink(iosAppStoreLink);
        }
        return stickerPackList;
    }

    @NonNull
    private static StickerPack readStickerPack(@NonNull JsonReader reader) throws IOException, IllegalStateException {
        reader.beginObject();
        String identifier = null;
        String name = null;
        String publisher = "sticker";
        String trayImageFile = null;
        String publisherEmail = null;
        String publisherWebsite = null;
        String privacyPolicyWebsite = null;
        String licenseAgreementWebsite = null;
        List<Sticker> stickerList = null;
        long totalSize = 0;
        String trayImageUrl = null;
        while (reader.hasNext()) {
            String key = reader.nextName();
            switch (key) {
                case "id":
                    identifier = reader.nextString();
                    break;
                case "name":
                    name = reader.nextString();
                    break;
                case "stickers":
                    stickerList = readStickers(reader);
                    break;
                case "size":
                    totalSize = reader.nextLong();
                    break;
                case "tray_image_url":
                    trayImageUrl = reader.nextString();
                    break;
                default:
                    reader.skipValue();
            }
        }
        trayImageFile = name + ".png";
        if (TextUtils.isEmpty(identifier)) {
            throw new IllegalStateException("identifier cannot be empty");
        }
        if (TextUtils.isEmpty(name)) {
            throw new IllegalStateException("name cannot be empty");
        }
        if (TextUtils.isEmpty(trayImageFile)) {
            throw new IllegalStateException("tray_image_file cannot be empty");
        }
        if (stickerList == null || stickerList.size() == 0) {
            throw new IllegalStateException("sticker list is empty");
        }
        if (identifier.contains("..") || identifier.contains("/")) {
            throw new IllegalStateException("identifier should not contain .. or / to prevent directory traversal");
        }
        reader.endObject();
        final StickerPack stickerPack = new StickerPack(identifier, name, publisher, trayImageFile, publisherEmail, publisherWebsite, privacyPolicyWebsite, licenseAgreementWebsite);
        stickerPack.setStickers(stickerList);
        stickerPack.setTotalSize(totalSize);
        stickerPack.setTrayImageUrl(trayImageUrl);
        return stickerPack;
    }

    @NonNull
    private static List<Sticker> readStickers(@NonNull JsonReader reader) throws IOException, IllegalStateException {
        reader.beginArray();
        List<Sticker> stickerList = new ArrayList<>();
        while (reader.hasNext()) {
            reader.beginObject();
            String imageFile = null;
            String imageUrl = null;
            while (reader.hasNext()) {
                final String key = reader.nextName();
                if ("file_name".equals(key)) {
                    imageFile = reader.nextString();
                } else if ("file_url".equals(key)) {
                    imageUrl = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            if (TextUtils.isEmpty(imageFile)) {
                throw new IllegalStateException("sticker image_file cannot be empty");
            }
            if (imageFile != null && !imageFile.endsWith(".webp")) {
                throw new IllegalStateException("image file for stickers should be webp files, image file is: " + imageFile);
            }
            if (imageFile != null && (imageFile.contains("..") || imageFile.contains("/"))) {
                throw new IllegalStateException("the file name should not contain .. or / to prevent directory traversal, image file is:" + imageFile);
            }
            stickerList.add(new Sticker(imageFile, getEmojis(stickerList.size()), imageUrl));
        }
        reader.endArray();
        return stickerList;
    }

    private static List<String> getEmojis(int pos) {
        String[] group1 = {
                "😀", "😁", "😂", "😃", "😄", "😅", "😆", "😉", "😊", "😋"};
        String[] group2 = {
                "😥", "😮", "😯", "😪", "😫", "😴", "😌", "😛", "😜", "😝"};
        int i = pos / 10;
        int j = pos % 10;
        return Arrays.asList(group1[i], group2[j]);
    }
}

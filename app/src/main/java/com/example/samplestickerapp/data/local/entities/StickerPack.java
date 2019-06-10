/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.example.samplestickerapp.data.local.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = StickerPack.TABLE_NAME)
public class StickerPack implements Parcelable {

    public static final String TABLE_NAME = "sticker_pack";
    public static final Creator<StickerPack> CREATOR = new Creator<StickerPack>() {
        @Override
        public StickerPack createFromParcel(Parcel in) {
            return new StickerPack(in);
        }

        @Override
        public StickerPack[] newArray(int size) {
            return new StickerPack[size];
        }
    };
    private final String publisherEmail;
    private final String publisherWebsite;
    private final String privacyPolicyWebsite;
    private final String licenseAgreementWebsite;

    @PrimaryKey()
    private int identifier;
    private String name;
    private String publisher;
    private String trayImageFile;
    private String trayImageUrl;
    private String iosAppStoreLink;
    private String androidPlayStoreLink;
    @Ignore
    private List<Sticker> stickers;
    private long totalSize;
    private long download;
    private boolean isWhitelisted;
    private Boolean isFav;


    public StickerPack(int identifier, String name, String publisher, String trayImageFile, String publisherEmail, String publisherWebsite, String privacyPolicyWebsite, String licenseAgreementWebsite) {
        this.identifier = identifier;
        this.name = name;
        this.publisher = publisher;
        this.trayImageFile = trayImageFile;
        this.publisherEmail = publisherEmail;
        this.publisherWebsite = publisherWebsite;
        this.privacyPolicyWebsite = privacyPolicyWebsite;
        this.licenseAgreementWebsite = licenseAgreementWebsite;
    }

    protected StickerPack(Parcel in) {
        publisherEmail = in.readString();
        publisherWebsite = in.readString();
        privacyPolicyWebsite = in.readString();
        licenseAgreementWebsite = in.readString();
        identifier = in.readInt();
        name = in.readString();
        publisher = in.readString();
        trayImageFile = in.readString();
        trayImageUrl = in.readString();
        iosAppStoreLink = in.readString();
        androidPlayStoreLink = in.readString();
        stickers = in.createTypedArrayList(Sticker.CREATOR);
        totalSize = in.readLong();
        download = in.readLong();
        isWhitelisted = in.readByte() != 0;
        isFav = in.readByte() != 0;
    }

    public void setIsWhitelisted(boolean isWhitelisted) {
        this.isWhitelisted = isWhitelisted;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    public void setStickers(List<Sticker> stickers) {
        this.stickers = stickers;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }


    public String getPublisherEmail() {
        return publisherEmail;
    }

    public String getPublisherWebsite() {
        return publisherWebsite;
    }

    public String getPrivacyPolicyWebsite() {
        return privacyPolicyWebsite;
    }

    public String getLicenseAgreementWebsite() {
        return licenseAgreementWebsite;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTrayImageFile() {
        return trayImageFile;
    }

    public void setTrayImageFile(String trayImageFile) {
        this.trayImageFile = trayImageFile;
    }

    public String getTrayImageUrl() {
        return trayImageUrl;
    }

    public void setTrayImageUrl(String trayImageUrl) {
        this.trayImageUrl = trayImageUrl;
    }

    public String getIosAppStoreLink() {
        return iosAppStoreLink;
    }

    public void setIosAppStoreLink(String iosAppStoreLink) {
        this.iosAppStoreLink = iosAppStoreLink;
    }

    public String getAndroidPlayStoreLink() {
        return androidPlayStoreLink;
    }

    public void setAndroidPlayStoreLink(String androidPlayStoreLink) {
        this.androidPlayStoreLink = androidPlayStoreLink;
    }

    public boolean isWhitelisted() {
        return isWhitelisted;
    }


    public long getDownload() {
        return download;
    }

    public void setDownload(long download) {
        this.download = download;
    }

    public Boolean isFav() {
        return isFav;
    }

    public void setFav(Boolean fav) {
        isFav = fav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(publisherEmail);
        dest.writeString(publisherWebsite);
        dest.writeString(privacyPolicyWebsite);
        dest.writeString(licenseAgreementWebsite);
        dest.writeInt(identifier);
        dest.writeString(name);
        dest.writeString(publisher);
        dest.writeString(trayImageFile);
        dest.writeString(trayImageUrl);
        dest.writeString(iosAppStoreLink);
        dest.writeString(androidPlayStoreLink);
        dest.writeTypedList(stickers);
        dest.writeLong(totalSize);
        dest.writeLong(download);
        dest.writeByte((byte) (isWhitelisted ? 1 : 0));
        dest.writeByte(isFav != null && isFav ? (byte) 1 : (byte) 0);
    }
}

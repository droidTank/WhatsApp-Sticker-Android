package com.example.samplestickerapp.data.local.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class StickerPackWithSticker {
    @Embedded
    private StickerPack stickerPack;

    @Relation(entity = Sticker.class,parentColumn = "identifier", entityColumn = "parentId")
    private List<Sticker> sticker;

    public StickerPack getStickerPack() {
        return stickerPack;
    }

    public void setStickerPack(StickerPack stickerPack) {
        this.stickerPack = stickerPack;
    }

    public List<Sticker> getSticker() {
        return sticker;
    }

    public void setSticker(List<Sticker> sticker) {
        this.sticker = sticker;
    }
}
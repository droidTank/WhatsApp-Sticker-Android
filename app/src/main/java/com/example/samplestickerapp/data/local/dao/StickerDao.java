package com.example.samplestickerapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.samplestickerapp.data.local.entities.Sticker;
import com.example.samplestickerapp.data.local.entities.StickerPack;
import com.example.samplestickerapp.data.local.entities.StickerPackWithSticker;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class StickerDao {

    @Query(value = "Select * FROM " + StickerPack.TABLE_NAME)
    protected abstract List<StickerPackWithSticker> geStickerPackWithStickers();

    @Insert
    public abstract void insertStickerPack(List<StickerPack> stickerPacks);

    @Insert
    public abstract void insertStickers(List<Sticker> stickerPacks);

    @Query("Delete From " + StickerPack.TABLE_NAME)
    public abstract void deleteAllStickerPacks();

    @Query("Delete From " + Sticker.TABLE_NAME)
    public abstract void deleteAllStickers();


    public List<StickerPack> getStickerPacks() {
        List<StickerPack> stickerPacks = new ArrayList<>();
        for (StickerPackWithSticker stickerPack : geStickerPackWithStickers()) {
            StickerPack packStickerPack = stickerPack.getStickerPack();
            packStickerPack.setStickers(stickerPack.getSticker());
            stickerPacks.add(packStickerPack);
        }
        return stickerPacks;

    }


}

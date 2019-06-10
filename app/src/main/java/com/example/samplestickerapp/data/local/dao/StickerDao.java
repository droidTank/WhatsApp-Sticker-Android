package com.example.samplestickerapp.data.local.dao;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.samplestickerapp.data.local.entities.Sticker;
import com.example.samplestickerapp.data.local.entities.StickerPack;
import com.example.samplestickerapp.data.local.entities.StickerPackWithSticker;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class StickerDao {

    @Transaction
    @Query(value = "Select * FROM " + StickerPack.TABLE_NAME)
    protected abstract List<StickerPackWithSticker> getAllStickerPackWithStickers();



    @Transaction
    @Query(value = "Select * FROM " + StickerPack.TABLE_NAME)
    protected abstract LiveData<List<StickerPackWithSticker>> getAllStickerPackWithStickersLive();


    @Transaction
    @Query(value = "Select * FROM " + StickerPack.TABLE_NAME + " WHERE isFav=1")
    protected abstract LiveData<List<StickerPackWithSticker>> getAllFavStickerPackWithStickers();


    @Query(value = "Select * FROM " + StickerPack.TABLE_NAME + " WHERE identifier=:id")
    @Transaction
    protected abstract StickerPackWithSticker geStickerPackWithSticker(String id);

    @Query(value = "Select identifier FROM " + StickerPack.TABLE_NAME + " where isFav=1")
    protected abstract List<Integer> getFavStickerId();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract List<Long> insertStickerPack(List<StickerPack> stickerPacks);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    protected abstract int updateStickerPack(List<StickerPack> stickerPacks);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract List<Long> insertStickers(List<Sticker> stickerPacks);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    protected abstract int updateStickers(List<Sticker> stickerPacks);


    @Query("Update  " + StickerPack.TABLE_NAME + " set isFav=:fav where identifier=:id")
    public abstract void setFavStickerPack(int id, boolean fav);


    @Query("Delete From " + StickerPack.TABLE_NAME)
    public abstract void deleteAllStickerPacks();

    @Query("Delete From " + Sticker.TABLE_NAME)
    public abstract void deleteAllStickers();

    @Query("Delete From " + Sticker.TABLE_NAME + " WHERE  parentId in ( :ids)")
    public abstract void deleteAllStickersByIds(String[] ids);


    @Transaction
    public void insertStickerAndPack(List<StickerPack> stickerPacks) {
        List<Integer> favIds = getFavStickerId();
        List<Sticker> stickerList = new ArrayList<>();
        for (StickerPack pack :
                stickerPacks) {
            if (favIds.contains(pack.getIdentifier())) {
                pack.setFav(true);
            } else
                pack.setFav(false);

            for (Sticker sticker : pack.getStickers()) {
                sticker.setParentId(pack.getIdentifier());
                stickerList.add(sticker);
            }

        }

        {
            List<Long> insertResult = insertStickerPack(stickerPacks);
            List<StickerPack> updateList = new ArrayList<>();
            for (int i = 0; i < insertResult.size(); i++) {
                if (insertResult.get(i) == -1) {
                    updateList.add(stickerPacks.get(i));
                }
            }
            if (!updateList.isEmpty()) {
                updateStickerPack(updateList);
            }
        }

        {
            List<Long> insertResult2 = insertStickers(stickerList);
            List<Sticker> stickers = new ArrayList<>();
            for (int i = 0; i < insertResult2.size(); i++) {
                if (insertResult2.get(i) == -1) {
                    stickers.add(stickerList.get(i));
                }
            }
            if (!stickers.isEmpty()) {
                updateStickers(stickers);
            }
        }


    }


    public List<StickerPack> getAllStickerPacks() {
        List<StickerPack> stickerPacks = new ArrayList<>();
        for (StickerPackWithSticker stickerPack : getAllStickerPackWithStickers()) {
            StickerPack packStickerPack = stickerPack.getStickerPack();
            packStickerPack.setStickers(stickerPack.getSticker());
            stickerPacks.add(packStickerPack);
        }
        return stickerPacks;

    }


    public LiveData<List<StickerPack>> getAllStickerPacksLiveData() {
        return Transformations.map(getAllStickerPackWithStickersLive(), (livedata) -> {
            List<StickerPack> stickerPacks = new ArrayList<>();
            for (StickerPackWithSticker stickerPack : livedata) {

                StickerPack packStickerPack = stickerPack.getStickerPack();
                packStickerPack.setStickers(stickerPack.getSticker());


                stickerPacks.add(packStickerPack);
            }
            return stickerPacks;
        });


    }




    public LiveData<List<StickerPack>> getAllFavStickerPacks() {
        return Transformations.map(getAllFavStickerPackWithStickers(), (livedata) -> {
            List<StickerPack> stickerPacks = new ArrayList<>();
            for (StickerPackWithSticker stickerPack : livedata) {

                StickerPack packStickerPack = stickerPack.getStickerPack();
                packStickerPack.setStickers(stickerPack.getSticker());


                stickerPacks.add(packStickerPack);
            }
            return stickerPacks;
        });


    }

    @Nullable
    public StickerPack getStickerPack(String id) {
        StickerPackWithSticker stickerPack = geStickerPackWithSticker(id);
        StickerPack packStickerPack = null;
        if (stickerPack != null) {
            packStickerPack = stickerPack.getStickerPack();
            packStickerPack.setStickers(stickerPack.getSticker());
        }

        return packStickerPack;

    }


}

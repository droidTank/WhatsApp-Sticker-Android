/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.example.samplestickerapp.ui.detail;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.samplestickerapp.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class StickerPreviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final StickerPreviewAdapter.StickerClickListener stickerClickListener;
    public SimpleDraweeView stickerPreviewView;

    StickerPreviewViewHolder(final View itemView, StickerPreviewAdapter.StickerClickListener stickerClickListener) {
        super(itemView);
        this.stickerClickListener = stickerClickListener;
        stickerPreviewView = itemView.findViewById(R.id.sticker_preview);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        stickerClickListener.onClicked(getLayoutPosition());

    }
}
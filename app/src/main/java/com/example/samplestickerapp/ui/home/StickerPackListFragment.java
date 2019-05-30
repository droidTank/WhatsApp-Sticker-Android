package com.example.samplestickerapp.ui.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.samplestickerapp.R;
import com.example.samplestickerapp.data.local.entities.StickerPack;

import java.util.ArrayList;
import java.util.List;

import static com.example.samplestickerapp.ui.home.StickerPackListActivity.EXTRA_STICKER_PACK_LIST_DATA;


public class StickerPackListFragment extends Fragment implements StickerPackListAdapter.OnAddButtonClickedListener {
    private static final int STICKER_PREVIEW_DISPLAY_LIMIT = 5;

    private static final String VIEW_STYLE = "param1";
    private String mParam1;
    private RecyclerView packRecyclerView;
    private StickerPackListAdapter allStickerPacksListAdapter;
    private LinearLayoutManager packLayoutManager;
    private ArrayList<StickerPack> stickerPackList= new ArrayList<>();

    public StickerPackListFragment() {
        // Required empty public constructor
    }

    public static StickerPackListFragment newInstance(ArrayList<StickerPack> stickerPacks,String type) {
        StickerPackListFragment fragment = new StickerPackListFragment();
        Bundle args = new Bundle();
        args.putString(VIEW_STYLE, type);
        args.putParcelableArrayList(EXTRA_STICKER_PACK_LIST_DATA,stickerPacks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(VIEW_STYLE);
            stickerPackList = getArguments().getParcelableArrayList(EXTRA_STICKER_PACK_LIST_DATA);
        }
        if (stickerPackList==null)
            stickerPackList=new ArrayList<>();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sticker_pack_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        packRecyclerView = view.findViewById(R.id.sticker_pack_list);
        showStickerPackList(stickerPackList);
    }

    private void showStickerPackList(List<StickerPack> stickerPackList) {
        allStickerPacksListAdapter = new StickerPackListAdapter(stickerPackList, this);
        packRecyclerView.setAdapter(allStickerPacksListAdapter);
        packLayoutManager = new LinearLayoutManager(requireContext());
        packLayoutManager.setOrientation(RecyclerView.VERTICAL);
        packRecyclerView.setLayoutManager(packLayoutManager);
        packRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                StickerPackListFragment.this.recalculateColumnCount();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    packRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                else {
                    packRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                }
            }
        });
    }

    private void recalculateColumnCount() {
        final int previewSize = getResources().getDimensionPixelSize(R.dimen.sticker_pack_list_item_preview_image_size);
        int firstVisibleItemPosition = packLayoutManager.findFirstVisibleItemPosition();
        StickerPackListItemViewHolder viewHolder = (StickerPackListItemViewHolder) packRecyclerView.findViewHolderForAdapterPosition(firstVisibleItemPosition);
        if (viewHolder != null) {
            final int max = Math.max(viewHolder.imageRowView.getMeasuredWidth() / previewSize, 1);
            int numColumns = Math.min(STICKER_PREVIEW_DISPLAY_LIMIT, max);
            allStickerPacksListAdapter.setMaxNumberOfStickersInARow(numColumns);
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAddButtonClicked(StickerPack stickerPack) {
        // TODO: 24/5/19
        Toast.makeText(requireContext(), "todo", Toast.LENGTH_SHORT).show();

    }
}

package com.example.samplestickerapp.ui.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplestickerapp.R;
import com.example.samplestickerapp.data.local.AppDatabase;
import com.example.samplestickerapp.data.local.dao.StickerDao;
import com.example.samplestickerapp.data.local.entities.StickerPack;
import com.example.samplestickerapp.ui.custom_progress_ly.CustomDataLayout;
import com.example.samplestickerapp.utils.Analytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class StickerPackListFragment extends Fragment implements StickerPackListAdapter.OnAddButtonClickedListener {
    public static final int HOME_TYPE = 1;
    public static final int TRENDING_TYPE = 2;
    public static final int FAV_TYPE = 3;

    private static final int STICKER_PREVIEW_DISPLAY_LIMIT = 5;
    private static final String VIEW_STYLE = "param1";
    CustomDataLayout customDataLayout;
    private int type;
    private RecyclerView packRecyclerView;
    private StickerPackListAdapter allStickerPacksListAdapter;
    private LinearLayoutManager packLayoutManager;
    private List<StickerPack> stickerPackList = new ArrayList<>();

    public StickerPackListFragment() {
        // Required empty public constructor
    }

    public static StickerPackListFragment newInstance(int type) {
        StickerPackListFragment fragment = new StickerPackListFragment();
        Bundle args = new Bundle();
        args.putInt(VIEW_STYLE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(VIEW_STYLE, HOME_TYPE);
        }


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
        customDataLayout = view.findViewById(R.id.custom_prog_ly);
        StickerDao stickerDao = AppDatabase.getInstance(requireContext()).subjectDao();
        if (type == HOME_TYPE) {

            stickerDao.getAllStickerPacksLiveData().observe(this, (d) -> {
                stickerPackList = d;
                showStickerPackList(stickerPackList);
            });
            Analytics.setCurrentScreen(requireActivity(), Analytics.Screens.HOME_SCREEN);


        } else if (type == TRENDING_TYPE) {
            Analytics.setCurrentScreen(requireActivity(), Analytics.Screens.TRENDING_SCREEN);

            stickerDao.getAllStickerPacksLiveData().observe(this, (d) -> {
                stickerPackList = d;

                Collections.sort(stickerPackList, new Comparator<StickerPack>() {
                    @Override
                    public int compare(StickerPack o1, StickerPack o2) {
                        return Long.compare(o2.getDownload(), o1.getDownload());
                    }
                });
                int size = 10;
                if (stickerPackList.size() > size) {
                    List<StickerPack> stickerPacks = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        stickerPacks.add(stickerPackList.get(i));
                    }

                    stickerPackList = stickerPacks;
                }

                showStickerPackList(stickerPackList);
            });

        } else if (type == FAV_TYPE) {
            Analytics.setCurrentScreen(requireActivity(), Analytics.Screens.FAV_SCREEN);
            stickerPackList = new ArrayList<>();
            stickerDao.getAllFavStickerPacks().observe(this, (data) -> {
                stickerPackList = data;
                showStickerPackList(data);
            });

        }
        initAdapter();


    }


    private void initAdapter() {
        allStickerPacksListAdapter = new StickerPackListAdapter(stickerPackList, this);
        packRecyclerView.setAdapter(allStickerPacksListAdapter);
        packLayoutManager = new LinearLayoutManager(requireContext());
        packLayoutManager.setOrientation(RecyclerView.VERTICAL);
        packRecyclerView.setLayoutManager(packLayoutManager);
        packRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getContext() == null)
                    return;
                boolean isSuccess = StickerPackListFragment.this.recalculateColumnCount();
                if (isSuccess)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        packRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        packRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    }
            }
        });
    }


    private void showStickerPackList(List<StickerPack> stickerPackList) {
        if (stickerPackList.isEmpty())
            customDataLayout.setViewStatus(CustomDataLayout.Status.NO_RESULT, "no data");
        allStickerPacksListAdapter.setStickerPackList(stickerPackList);
        allStickerPacksListAdapter.notifyDataSetChanged();

    }

    private boolean recalculateColumnCount() {
        final int previewSize = getResources().getDimensionPixelSize(R.dimen.sticker_pack_list_item_preview_image_size);
        int firstVisibleItemPosition = packLayoutManager.findFirstVisibleItemPosition();
        StickerPackListItemViewHolder viewHolder = (StickerPackListItemViewHolder) packRecyclerView.findViewHolderForAdapterPosition(firstVisibleItemPosition);
        if (viewHolder != null) {
            final int max = Math.max(viewHolder.imageRowView.getMeasuredWidth() / previewSize, 1);
            int numColumns = Math.min(STICKER_PREVIEW_DISPLAY_LIMIT, max);
            allStickerPacksListAdapter.setMaxNumberOfStickersInARow(numColumns);
            return true;
        }
        return false;
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
        boolean fav = stickerPack.isFav() != null && stickerPack.isFav();

        int identifier = stickerPack.getIdentifier();
        Analytics.trackWithGroupId(fav ? Analytics.AnalyticsEvents.STICKER_UNFAV : Analytics.AnalyticsEvents.STICKER_FAV, String.valueOf(identifier));
        AppDatabase.getInstance(requireContext()).subjectDao().setFavStickerPack(identifier, !fav);
        stickerPack.setFav(!fav);
        allStickerPacksListAdapter.notifyDataSetChanged();

    }
}

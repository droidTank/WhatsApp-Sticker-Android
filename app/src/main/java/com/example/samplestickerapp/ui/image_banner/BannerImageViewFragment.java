package com.example.samplestickerapp.ui.image_banner;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplestickerapp.R;
import com.example.samplestickerapp.utils.Analytics;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BannerImageViewFragment extends Fragment {

    private static final String ARG_ITEM_COUNT = "item_count";
    List<String> image = Collections.emptyList();
    private RecyclerView bigImRv;
    private TextView title;
    private int pos;

    public static BannerImageViewFragment newInstance(List<String> image,int pos) {
        final BannerImageViewFragment fragment = new BannerImageViewFragment();
        final Bundle args = new Bundle();
        args.putInt("pos",pos);
        args.putStringArrayList(ARG_ITEM_COUNT, new ArrayList<String>(image));
        fragment.setArguments(args);
        return fragment;
    }

    public static void launch(FragmentManager supportFragmentManager, List<String> image,int pos) {
        supportFragmentManager.beginTransaction().add(android.R.id.content, BannerImageViewFragment.newInstance(image,pos),
                "imagess").addToBackStack("txxag").commit();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getStringArrayList(ARG_ITEM_COUNT);
            pos = getArguments().getInt("pos");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_banner_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Analytics.setCurrentScreen(requireActivity(), Analytics.Screens.STICKER_BANNER_VIEW,BannerImageViewFragment.class);
        bigImRv = view.findViewById(R.id.list);
        title = view.findViewById(R.id.title);
        View toolbar_view = view.findViewById(R.id.close_iv);
        toolbar_view.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        bigImRv.setLayoutManager(layoutManager);
        bigImRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pos;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (pos != firstVisibleItemPosition) {
                }
                pos = firstVisibleItemPosition;
            }
        });
        bigImRv.setAdapter(new ImageItemAdapter());
        layoutManager.scrollToPosition(pos);
        new PagerSnapHelper().attachToRecyclerView(bigImRv);

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    private class ImageItemAdapter extends RecyclerView.Adapter<ImageItemAdapter.ViewHolder> {
        private ImageItemAdapter() {
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Uri parse = Uri.parse(image.get(position));
            holder.bigImageView.setImageURI(parse);

        }

        @Override
        public int getItemCount() {
            return image.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final SimpleDraweeView bigImageView;

            ViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.fragment_image_item, parent, false));
                bigImageView = itemView.findViewById(R.id.full_image);


            }
        }

    }

}

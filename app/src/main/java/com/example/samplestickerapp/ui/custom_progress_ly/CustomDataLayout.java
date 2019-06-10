package com.example.samplestickerapp.ui.custom_progress_ly;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.samplestickerapp.R;
import com.facebook.shimmer.ShimmerFrameLayout;


public class CustomDataLayout extends FrameLayout {


    TextView noDataText;
    ImageView no_data_iv;
    Button retry_btn;
    private ViewGroup progress_layout;
    private View customProgrss;

    public CustomDataLayout(@NonNull Context context) {
        this(context, null);
    }

    public CustomDataLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CustomDataLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomDataLayout, 0, 0);
        int progressbarPosition = attributes.getInt(R.styleable.CustomDataLayout_progressbar_position, 1);
        int layoutId = attributes.getResourceId(R.styleable.CustomDataLayout_progress_layout, -1);
        Log.i("ddd", "CustomDataLayout: " + layoutId);
        attributes.recycle();
        init(progressbarPosition, layoutId);
    }

    void init(int progressbar_position, int layoutId) {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.no_data_latout, this, true);
        progress_layout = findViewById(R.id.progress_layout);
        noDataText = findViewById(R.id.no_data_tv);
        no_data_iv = findViewById(R.id.no_data_iv);
        retry_btn = findViewById(R.id.retry_btn);
        View progress_bar = findViewById(R.id.progress_bar);

        if (layoutId != -1) {
            progress_bar.setVisibility(GONE);
            customProgrss = LayoutInflater.from(getContext()).inflate(layoutId, progress_layout, false);
            progress_layout.addView(customProgrss);

        } else if (progressbar_position == 0) {
            LayoutParams lp = (LayoutParams) progress_bar.getLayoutParams();
            lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            float dip = 50f;
            Resources r = getResources();
            float px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.getDisplayMetrics()
            );
            lp.topMargin = (int) px;


        }
        setVisibility(GONE);
    }

    private void showProgress() {
        progress_layout.setVisibility(VISIBLE);
        if (customProgrss instanceof ShimmerFrameLayout) {
            ((ShimmerFrameLayout) customProgrss).startShimmer();
        }
    }

    private void hideProgress() {
        if (customProgrss instanceof ShimmerFrameLayout) {
            ((ShimmerFrameLayout) customProgrss).stopShimmer();
        }
        progress_layout.setVisibility(GONE);
    }

    public void setViewStatus(Status status) {
        retry_btn.setVisibility(GONE);
        no_data_iv.setVisibility(VISIBLE);

        if (status == Status.NO_NETWORK) {
            noDataText.setText(R.string.connectivity_error);
            no_data_iv.setImageResource(R.drawable.ic_no_internet);
            hideProgress();
            setVisibility(VISIBLE);
            retry_btn.setVisibility(VISIBLE);
        } else if (status == Status.NO_RESULT) {
            noDataText.setText("No Listing Found");
            // TODO: 5/12/18  change icon
            // no_data_iv.setImageResource(R.drawable.ic_no_result);
            no_data_iv.setImageDrawable(null);
            no_data_iv.setVisibility(GONE);
            hideProgress();
            setVisibility(VISIBLE);
        } else if (status == Status.SOME_WRONG) {
            noDataText.setText(R.string.error_occurred);
            no_data_iv.setImageResource(R.drawable.ic_error_occured);
            hideProgress();
            setVisibility(VISIBLE);
        } else if (status == Status.SHOW_PROGRESS) {
            setVisibility(VISIBLE);
            showProgress();
        } else if (status == Status.HIDE_PROGRESS) {
            hideProgress();
            setVisibility(GONE);


        }

    }

    public void setRetryClickListener(OnClickListener listener) {
        retry_btn.setOnClickListener(listener);
    }

    public void setViewStatus(Status errorStatus, String noResults) {
        setViewStatus(errorStatus);
        if (errorStatus == Status.NO_RESULT)
            noDataText.setText(noResults);
    }


    public enum Status {
        NO_NETWORK, NO_RESULT, SOME_WRONG, SHOW_PROGRESS, HIDE_PROGRESS
    }


}

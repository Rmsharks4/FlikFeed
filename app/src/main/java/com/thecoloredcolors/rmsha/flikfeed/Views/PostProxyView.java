package com.thecoloredcolors.rmsha.flikfeed.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.PostFragment;
import com.thecoloredcolors.rmsha.flikfeed.GlideApp;
import com.thecoloredcolors.rmsha.flikfeed.R;

/**
 * Created by rmsha on 11/27/2017.
 */

public class PostProxyView extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public PostProxyView(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.post_proxy_view);
    }

    public ImageView getImageView() {
        return imageView;
    }
}

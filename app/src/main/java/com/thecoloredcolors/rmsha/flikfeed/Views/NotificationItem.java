package com.thecoloredcolors.rmsha.flikfeed.Views;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.thecoloredcolors.rmsha.flikfeed.Fragments.PostFragment;
import com.thecoloredcolors.rmsha.flikfeed.R;

import static android.view.View.GONE;

/**
 * Created by rmsha on 12/3/2017.
 */

public class NotificationItem extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private ImageView imageButton;
    private TextView textView;
    private View itemView;

    public NotificationItem(View itemView) {
        super(itemView);
        this.itemView = itemView;
        imageView = (ImageView) itemView.findViewById(R.id.ivUserAvatar);
        imageButton = (ImageView) itemView.findViewById(R.id.ivFollowAvatar);
        imageButton.setVisibility(GONE);
        textView = (TextView) itemView.findViewById(R.id.tvContent);
    }

    public View getItemView() {
        return itemView;
    }

    public void setItemView(View itemView) {
        this.itemView = itemView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public ImageView getImageButton() {
        return imageButton;
    }

    public void setImageButton(ImageView imageButton) {
        this.imageButton = imageButton;
    }
}

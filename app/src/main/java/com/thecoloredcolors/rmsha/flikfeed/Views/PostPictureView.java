package com.thecoloredcolors.rmsha.flikfeed.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.thecoloredcolors.rmsha.flikfeed.Classes.TagClass;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.List;

/**
 * Created by rmsha on 11/23/2017.
 */

public class PostPictureView extends SubsamplingScaleImageView {

    private GestureDetector gestureDetector;
    private PostView postView;

    public PostPictureView(Context context) {
        super(context);
    }

    public PostPictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void SetArguments(PostView postView) {
        this.postView = postView;
        init(getContext());
    }

    private void init(final Context context) {

        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                postView.DoubleTapFunction(getRootView().getRootView());
                return true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        if(postView!=null) {
            postView.SetVisibleTags(getRootView(), getRootView().getRootView().findViewById(R.id.tag), false);
            gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

}
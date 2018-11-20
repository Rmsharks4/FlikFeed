package com.thecoloredcolors.rmsha.flikfeed.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.thecoloredcolors.rmsha.flikfeed.R;

/**
 * Created by rmsha on 11/24/2017.
 */

public class PostVideoView extends VideoView {

    private VidListener v = new VidListener(getContext());
    private PostView postView;

    public PostVideoView(Context context) {
        super(context);
    }

    public PostVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void SetArguments(PostView postView) {
        this.postView = postView;
        init(getContext());
    }

    public PostVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PostVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {
        setOnTouchListener(v);
        setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                start();
            }
        });
        ViewGroup m = (ViewGroup) findViewById(R.id.exomedia_controls_interactive_container);
        RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) m.getLayoutParams();
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            l.setMargins(l.leftMargin, 100, l.rightMargin, l.bottomMargin);
        }
        m.setLayoutParams(l);
        m.removeViewAt(1);
    }

    protected class VidListener extends TouchListener {

        VidListener(Context context) {
            super(context);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            return super.onSingleTapConfirmed(event);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(postView!=null) {
                postView.DoubleTapFunction(getRootView().getRootView());
            }
            return super.onDoubleTap(e);
        }
    }
}
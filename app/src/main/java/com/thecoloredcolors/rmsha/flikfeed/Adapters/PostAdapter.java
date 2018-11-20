package com.thecoloredcolors.rmsha.flikfeed.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.thecoloredcolors.rmsha.flikfeed.Activities.MainActivity;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostClass;
import com.thecoloredcolors.rmsha.flikfeed.Views.PostView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmsha on 11/12/2017.
 */

public class PostAdapter extends PagerAdapter {

    private List<PostClass> posts = new ArrayList<>();
    private Context context;
    private FragmentManager fragmentManager;

    public PostAdapter(List<PostClass> posts, Context context, FragmentManager fragmentManager) {
        this.posts = posts;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((PostView)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(PostView)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PostView postView = new PostView(context);
        postView.SetArguments(posts.get(position), MainActivity.currentUser.UserProxy(),fragmentManager);
        container.addView(postView,0);
        return postView;
    }

}

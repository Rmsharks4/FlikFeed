package com.thecoloredcolors.rmsha.flikfeed.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.PostFragment;
import com.thecoloredcolors.rmsha.flikfeed.GlideApp;
import com.thecoloredcolors.rmsha.flikfeed.R;
import com.thecoloredcolors.rmsha.flikfeed.Views.PostProxyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmsha on 11/27/2017.
 */

public class DiscoverAdapter extends RecyclerView.Adapter<PostProxyView> {

    private Context context;
    private List<PostProxyClass> proxyClassList = new ArrayList<>();
    private FragmentManager fragmentManager;

    public DiscoverAdapter(Context context, List<PostProxyClass> proxyClassList, FragmentManager fragmentManager) {
        this.context = context;
        this.proxyClassList = proxyClassList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public PostProxyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_proxy_layout, parent, false);
        return new PostProxyView(v);
    }

    @Override
    public void onBindViewHolder(final PostProxyView holder, @SuppressLint("RecyclerView") final int position) {
        GlideApp
                .with(context)
                .asBitmap()
                .load(proxyClassList.get(position).getPostResource().getResource())
                .priority(Priority.HIGH)
                .placeholder(R.drawable.proxy_holder)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        holder.getImageView().setImageBitmap(resource);
                    }
                });
        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("postid",proxyClassList.get(position).getPostid());
                final PostFragment pageFragment = new PostFragment();
                pageFragment.setArguments(b);
                fragmentManager.beginTransaction().replace(android.R.id.content,pageFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return proxyClassList.size();
    }
}

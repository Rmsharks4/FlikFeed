package com.thecoloredcolors.rmsha.flikfeed.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thecoloredcolors.rmsha.flikfeed.Classes.PostLikeClass;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.UserProfileFragment;
import com.thecoloredcolors.rmsha.flikfeed.GlideApp;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.List;

/**
 * Created by rmsha on 11/13/2017.
 */

public class LikesAdapter extends ArrayAdapter<PostLikeClass> {

    private UserProxyClass currentuser;
    private FragmentManager fragmentManager;

    private List<Boolean> followstats;

    public LikesAdapter(@NonNull Context context, @NonNull List<PostLikeClass> objects, List<Boolean> followstats, UserProxyClass currentuser, FragmentManager fragmentManager) {
        super(context, R.layout.like_item_for_post, objects);
        this.followstats = followstats;
        this.currentuser = currentuser;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater theinflater = LayoutInflater.from(getContext());

        @SuppressLint("ViewHolder")
        final View view = theinflater.inflate(R.layout.like_item_for_post, parent, false);

        final UserProxyClass current = getItem(position).UserProxy();

        ImageView profilePic = (ImageView) view.findViewById(R.id.profilepic);

        profilePic.setImageResource(R.drawable.user_pink);
        if(current.getProfilepic()!=null) {
            GlideApp
                    .with(getContext())
                    .asBitmap()
                    .load(current.getProfilepic())
                    .circleCrop()
                    .into(profilePic);
        }

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileClick(current.getUserid(),followstats.get(position));
            }
        });

        TextView username = (TextView) view.findViewById(R.id.likeusername);

        username.setText(current.getUsername());

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileClick(current.getUserid(),followstats.get(position));
            }
        });

        final TextView followbutton = (TextView) view.findViewById(R.id.followlikebutton);

        final FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();

        if(currentuser.getUserid().equals(current.getUserid())) {
            followbutton.setVisibility(View.GONE);
        } else if(followstats.get(position))
            followbutton.setText("UNFOLLOW");
        else
            followbutton.setText("FOLLOW");

        followbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(followstats.get(position))
                {
                    fireBaseHelper.RegisterUnfollow(currentuser.getUserid(),current.getUserid());
                    followbutton.setText("FOLLOW");
                }
                else
                {
                    fireBaseHelper.RegisterFollow(currentuser.getUserid(),current.getUserid());
                    followbutton.setText("UNFOLLOW");
                }
                followstats.set(position,!followstats.get(position));
            }
        });

        return view;
    }

    private void UserProfileClick(String userid,boolean Followstat)
    {
        Bundle b = new Bundle();
        b.putString("userid",userid);
        b.putBoolean("followstat",Followstat);
        final UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.setArguments(b);
        fragmentManager.beginTransaction().replace(android.R.id.content, userProfileFragment).addToBackStack(null).commit();
    }

}
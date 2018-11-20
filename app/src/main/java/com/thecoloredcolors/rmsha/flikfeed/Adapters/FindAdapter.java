package com.thecoloredcolors.rmsha.flikfeed.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.thecoloredcolors.rmsha.flikfeed.Activities.MainActivity;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.SearchResult;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.PostFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.UserProfileFragment;
import com.thecoloredcolors.rmsha.flikfeed.GlideApp;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.List;

/**
 * Created by rmsha on 11/28/2017.
 */

//SEARCH-RESULT-ID = username

public class FindAdapter extends ArrayAdapter<SearchResult> {

    private FragmentManager fragmentManager;

    public FindAdapter(@NonNull Context context, @NonNull List<SearchResult> objects, FragmentManager fragmentManager) {
        super(context, R.layout.search_item, objects);
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater theinflater = LayoutInflater.from(getContext());

        @SuppressLint("ViewHolder")
        final View view = theinflater.inflate(R.layout.search_item, parent, false);
        final ImageView profile = (ImageView) view.findViewById(R.id.profilepic);
        TextView username = (TextView) view.findViewById(R.id.username);
        TextView name = (TextView) view.findViewById(R.id.name);

        final SearchResult s = getItem(position);

            username.setText(s.getMatch());
            name.setText(s.getSideline());
            if(s.getPicture()!=null) {
                GlideApp
                        .with(getContext())
                        .asBitmap()
                        .load(s.getPicture())
                        .centerInside()
                        .circleCrop()
                        .into(profile);
            } else profile.setImageResource(R.drawable.user_pink);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(s.getType()==SearchResult.USER)
                        UserProfileClick(s.getSearchid());
                    else
                        PostClick(s.getSearchid());
                }
            });
        return view;
    }

    private void UserProfileClick(String userid)
    {
        final Bundle b = new Bundle();
        b.putString("userid",userid);
        FireBaseHelper.GetInstance().getDataBase().collection("following")
                .document(MainActivity.currentUser.getUserid()+userid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        b.putBoolean("followstat",documentSnapshot.exists());
                        final UserProfileFragment userProfileFragment = new UserProfileFragment();
                        userProfileFragment.setArguments(b);
                        fragmentManager.beginTransaction().replace(android.R.id.content, userProfileFragment).addToBackStack(null).commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"User-profile load failed!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PostClick(String postid) {
        Bundle b = new Bundle();
        b.putString("postid",postid);
        final PostFragment pageFragment = new PostFragment();
        pageFragment.setArguments(b);
        fragmentManager.beginTransaction().replace(android.R.id.content,pageFragment).addToBackStack(null).commit();
    }

}

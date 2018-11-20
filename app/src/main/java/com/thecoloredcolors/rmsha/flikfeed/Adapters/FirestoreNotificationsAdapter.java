package com.thecoloredcolors.rmsha.flikfeed.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.thecoloredcolors.rmsha.flikfeed.Activities.MainActivity;
import com.thecoloredcolors.rmsha.flikfeed.Classes.NotificationClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.Views.NotificationItem;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.PostFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.UserProfileFragment;
import com.thecoloredcolors.rmsha.flikfeed.GlideApp;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmsha on 12/3/2017.
 */

public class FirestoreNotificationsAdapter extends FirestoreRecyclerAdapter<NotificationClass,NotificationItem> {

    private Context context;
    private FragmentManager fragmentManager;

    public FirestoreNotificationsAdapter(FirestoreRecyclerOptions<NotificationClass> options, Context context, FragmentManager fragmentManager) {
        super(options);
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(final NotificationItem holder, int position, NotificationClass model) {
        final NotificationClass n = getItem(position);

        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("postid", n.getPostid());
                final PostFragment postFragment = new PostFragment();
                postFragment.setArguments(b);
                fragmentManager.beginTransaction().replace(android.R.id.content, postFragment).addToBackStack(null).commit();
            }
        });

        if(n.NotifUserProxy()==null) {
            FireBaseHelper.GetInstance().getDataBase().collection("users")
                    .document(n.getUserid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            n.loadNotifUserProxy(documentSnapshot.toObject(UserClass.class).UserProxy());
                            TextView tv = holder.getTextView().findViewById(R.id.tvContent);

                            if (n.getType() == NotificationClass.POST_LIKE) {
                                tv.setText(n.NotifUserProxy().getUsername() + " likes your post!");
                            } else if (n.getType() == NotificationClass.COMMENT) {
                                tv.setText(n.NotifUserProxy().getUsername() + " commented on your post!");
                            } else if (n.getType() == NotificationClass.COMMENT_LIKE) {
                                tv.setText(n.NotifUserProxy().getUsername() + " likes your comment!");
                            }

                            if(n.NotifUserProxy().getProfilepic()!=null) {
                                GlideApp.with(context)
                                        .load(n.NotifUserProxy().getProfilepic())
                                        .centerInside()
                                        .circleCrop()
                                        .into(holder.getImageView());
                            }
                        }
                    });
        } else {
            TextView tv = holder.getTextView().findViewById(R.id.tvContent);

            if (n.getType() == NotificationClass.POST_LIKE) {
                tv.setText(n.NotifUserProxy().getUsername() + " likes your post!");
            } else if (n.getType() == NotificationClass.COMMENT) {
                tv.setText(n.NotifUserProxy().getUsername() + " commented on your post!");
            } else if (n.getType() == NotificationClass.COMMENT_LIKE) {
                tv.setText(n.NotifUserProxy().getUsername() + " likes your comment!");
            }

            if(n.NotifUserProxy().getProfilepic()!=null) {
                GlideApp.with(context)
                        .load(n.NotifUserProxy().getProfilepic())
                        .centerInside()
                        .circleCrop()
                        .into(holder.getImageView());
            }
        }

        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bundle b = new Bundle();
                b.putString("userid",n.getUserid());
                FireBaseHelper.GetInstance().getDataBase().collection("following")
                        .document(MainActivity.currentUser.getUserid()+n.getUserid())
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
                        Toast.makeText(context,"User-profile load failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        super.onError(e);
    }

    @Override
    public NotificationItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item, parent, false);
        return new NotificationItem(v);
    }

}

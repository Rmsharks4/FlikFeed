package com.thecoloredcolors.rmsha.flikfeed.Fragments;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;

import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.thecoloredcolors.rmsha.flikfeed.Activities.MainActivity;
import com.thecoloredcolors.rmsha.flikfeed.Adapters.FirestoreNotificationsAdapter;
import com.thecoloredcolors.rmsha.flikfeed.Classes.NotificationClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FirestoreNotificationsAdapter notificationsAdapter;
    private List<NotificationClass> notifications = new ArrayList<>();

    FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();

    private OnFragmentInteractionListener mListener;

    public NotificationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        ButterKnife.bind(this, rootView);
        final RecyclerView rvNotifications = rootView.findViewById(R.id.mRecyclerVIew);
        final ProgressBar progressBar = rootView.findViewById(R.id.progressnotifs);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvNotifications.setHasFixedSize(true);
        rvNotifications.setLayoutManager(linearLayoutManager);

        Query query = fireBaseHelper.getDataBase()
                .collection("notifications")
                .whereEqualTo("notifuserid",MainActivity.currentUser.getUserid())
                .orderBy("time", Query.Direction.DESCENDING);

        final FirestoreRecyclerOptions<NotificationClass> options = new FirestoreRecyclerOptions.Builder<NotificationClass>()
                .setQuery(query, NotificationClass.class)
                .build();

        notificationsAdapter = new FirestoreNotificationsAdapter(options,getContext(),getActivity().getSupportFragmentManager());
        rvNotifications.setAdapter(notificationsAdapter);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getContext(),"An error occured - please try again later!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(snapshot!=null) {
                    if (notifications.isEmpty()) {
                        notifications.addAll(snapshot.toObjects(NotificationClass.class));
                    }

                    if (notifications.size() < snapshot.size()) {
                        if (isAppOnForeground(getContext())) {
                            MainActivity.GenerateNotifIcon();
                        } else {
                            for (int i = notifications.size(); i < snapshot.size(); i++) {
                                final NotificationClass n = snapshot.getDocuments().get(i).toObject(NotificationClass.class);
                                FireBaseHelper.GetInstance().getDataBase().collection("users")
                                        .document(n.getUserid())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                n.loadNotifUserProxy(documentSnapshot.toObject(UserClass.class).UserProxy());
                                                GenerateNotification(n);
                                            }
                                        });
                            }
                        }
                        notifications.clear();
                        notifications.addAll(snapshot.toObjects(NotificationClass.class));
                    }
                    progressBar.setVisibility(View.GONE);
                }
                if(notifications.isEmpty()) {
                    Toast.makeText(getContext(),"No new notifications to show!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(notificationsAdapter!=null)
            notificationsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(notificationsAdapter!=null)
            notificationsAdapter.stopListening();
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = null;
        if (activityManager != null) {
            appProcesses = activityManager.getRunningAppProcesses();
        }
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals("com.thecoloredcolors.rmsha.flikfeed")) {
                return true;
            }
        }
        return false;
    }

    private void GenerateNotification(NotificationClass n) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("postid",n.getPostid());
        String text = "";
        if (n.getType() == NotificationClass.POST_LIKE) {
            text = n.NotifUserProxy().getUsername() + " likes your post!";
        } else if (n.getType() == NotificationClass.COMMENT) {
            text = n.NotifUserProxy().getUsername() + " commented on your post!";
        } else if (n.getType() == NotificationClass.COMMENT_LIKE) {
            text = n.NotifUserProxy().getUsername() + " likes your comment!";
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getContext(), channelId)
                        .setSmallIcon(R.drawable.flickfeed)
                        .setContentTitle("FlikFeed")
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0 , notificationBuilder.build());
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}

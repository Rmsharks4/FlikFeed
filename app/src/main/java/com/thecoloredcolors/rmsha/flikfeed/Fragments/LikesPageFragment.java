package com.thecoloredcolors.rmsha.flikfeed.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.thecoloredcolors.rmsha.flikfeed.Activities.MainActivity;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostLikeClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.Adapters.LikesAdapter;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LikesPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LikesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikesPageFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public LikesPageFragment() {
        // Required empty public constructor
    }

    public static LikesPageFragment newInstance(String param1, String param2) {
        LikesPageFragment fragment = new LikesPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_likes_page, container, false);
        UpdateView(view);
        return view;
    }

    public void UpdateView(final View view) {

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.loadlikes);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            final String postid = bundle.getString("postid");
            if (bundle.size() > 1) {
                final FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();
                String commentid = bundle.getString("commentid");

                fireBaseHelper.getDataBase().collection("comment-likes")
                        .whereEqualTo("commentid",commentid)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                final List<PostLikeClass> usernames = documentSnapshots.toObjects(PostLikeClass.class);
                                final List<Boolean> followstats = new ArrayList<>();
                                if (usernames.size() > 0) {
                                    fireBaseHelper.getDataBase().runTransaction(new Transaction.Function<Boolean>() {
                                        @Nullable
                                        @Override
                                        public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                            followstats.clear();
                                            for (int i = 0; i < usernames.size(); i++) {
                                                DocumentReference u = fireBaseHelper
                                                        .getDataBase()
                                                        .collection("users")
                                                        .document(usernames.get(i).getUserid());
                                                usernames.get(i).loadUserProxy(transaction.get(u).toObject(UserClass.class).UserProxy());
                                                DocumentReference d = fireBaseHelper
                                                        .getDataBase()
                                                        .collection("following")
                                                        .document(MainActivity.currentUser.getUserid()+usernames.get(i).getUserid());
                                                followstats.add(transaction.get(d).exists());
                                            }
                                            return true;
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Boolean> task) {
                                            progressBar.setVisibility(View.GONE);
                                            LikesAdapter likesAdapter = new LikesAdapter(getContext(), usernames, followstats, MainActivity.currentUser.UserProxy(), getActivity().getSupportFragmentManager());
                                            ListView listView = (ListView) view.findViewById(R.id.likesonpostlistview);
                                            listView.setAdapter(likesAdapter);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("why","nolike");
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "No Likes to show!...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {

                final FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();

                fireBaseHelper.getDataBase().collection("post-likes")
                        .whereEqualTo("postid",postid)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                final List<PostLikeClass> usernames = documentSnapshots.toObjects(PostLikeClass.class);
                                final List<Boolean> followstats = new ArrayList<>();
                                if (usernames.size() > 0) {
                                    fireBaseHelper.getDataBase().runTransaction(new Transaction.Function<Boolean>() {
                                        @Nullable
                                        @Override
                                        public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                            followstats.clear();
                                            for (int i = 0; i < usernames.size(); i++) {
                                                DocumentReference u = fireBaseHelper
                                                        .getDataBase()
                                                        .collection("users")
                                                        .document(usernames.get(i).getUserid());
                                                usernames.get(i).loadUserProxy(transaction.get(u).toObject(UserClass.class).UserProxy());
                                                DocumentReference d = fireBaseHelper
                                                        .getDataBase()
                                                        .collection("following")
                                                        .document(MainActivity.currentUser.getUserid()+usernames.get(i).getUserid());
                                                followstats.add(transaction.get(d).exists());
                                            }
                                            return true;
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Boolean> task) {
                                            progressBar.setVisibility(View.GONE);
                                            LikesAdapter likesAdapter = new LikesAdapter(getContext(), usernames, followstats, MainActivity.currentUser.UserProxy(), getActivity().getSupportFragmentManager());
                                            ListView listView = (ListView) view.findViewById(R.id.likesonpostlistview);
                                            listView.setAdapter(likesAdapter);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("why","nolike");
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "No Likes to show!...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

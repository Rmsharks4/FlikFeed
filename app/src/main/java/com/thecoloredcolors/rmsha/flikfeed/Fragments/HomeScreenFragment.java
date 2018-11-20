package com.thecoloredcolors.rmsha.flikfeed.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.thecoloredcolors.rmsha.flikfeed.Activities.MainActivity;
import com.thecoloredcolors.rmsha.flikfeed.Adapters.PostAdapter;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostClass;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;


public class HomeScreenFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public HomeScreenFragment() {
    }

    public static HomeScreenFragment newInstance(String param1, String param2) {
        return new HomeScreenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        Toolbar toolbar = view.findViewById(R.id.mytoolbar);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        final VerticalViewPager viewPager = view.findViewById(R.id.verticalviewpager);
        final ProgressBar progressBar = view.findViewById(R.id.progresshome);

        final FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();

        fireBaseHelper
                .getDataBase()
                .collection("posts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        List<PostClass> l = new ArrayList<>();
                        l.addAll(documentSnapshots.toObjects(PostClass.class));
                        PostAdapter postAdapter = new PostAdapter(l,getContext(),getActivity().getSupportFragmentManager());
                        viewPager.setAdapter(postAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"No internet connection!",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

package com.thecoloredcolors.rmsha.flikfeed.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.thecoloredcolors.rmsha.flikfeed.Activities.SearchActivity;
import com.thecoloredcolors.rmsha.flikfeed.Adapters.FindAdapter;
import com.thecoloredcolors.rmsha.flikfeed.Classes.SearchResult;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.aflak.libraries.SearchResultFilter;
import me.aflak.utils.Condition;

public class TopFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private ProgressBar progressBar;
    private boolean LOADING = false;

    public TopFragment() {
    }

    public static TopFragment newInstance(String param1, String param2) {
        return new TopFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        listView = view.findViewById(R.id.list_view);
        progressBar = view.findViewById(R.id.progresstop);
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

    public void LoadTopResults(final String search) {
        progressBar.setVisibility(View.VISIBLE);
        if(!LOADING) {
            LOADING = true;
            SearchActivity activity = (SearchActivity) getActivity();
            List<SearchResult> result = new ArrayList<>();
            result.addAll(SearchResultFilter.builder()
                    .extraCondition(new Condition<SearchResult>() {
                        @Override
                        public boolean verify(SearchResult object) {
                            return (object.getMatch().toLowerCase().contains(search) || object.getSideline().toLowerCase().contains(search));
                        }
                    })
                    .on(activity.getSearchusers()));
            result.addAll(SearchResultFilter.builder()
                    .extraCondition(new Condition<SearchResult>() {
                        @Override
                        public boolean verify(SearchResult object) {
                            return object.getMatch().toLowerCase().contains(search);
                        }
                    })
                    .on(activity.getSearchposts()));
            Collections.shuffle(result);
            FindAdapter adapter = new FindAdapter(getContext(), result, activity.getSupportFragmentManager());
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            LOADING = false;
        }
    }

}

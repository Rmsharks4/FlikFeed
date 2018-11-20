package com.thecoloredcolors.rmsha.flikfeed.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.thecoloredcolors.rmsha.flikfeed.Activities.LoginActivity;
import com.thecoloredcolors.rmsha.flikfeed.Activities.MainActivity;
import com.thecoloredcolors.rmsha.flikfeed.Activities.SearchActivity;
import com.thecoloredcolors.rmsha.flikfeed.Activities.WelcomeActivity;
import com.thecoloredcolors.rmsha.flikfeed.Adapters.DiscoverAdapter;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.GlideApp;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;
import static com.thecoloredcolors.rmsha.flikfeed.Activities.SplashActivity.LOGGED_IN;
import static com.thecoloredcolors.rmsha.flikfeed.Activities.SplashActivity.LOGIN;


public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription, mLogout;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private Context mContext;
    private ProgressBar progressBar;

    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;

    private RecyclerView recyclerView;
    private DiscoverAdapter adapter;
    private FireBaseHelper fireBaseHelper;
    private List<PostClass> posts;
    private List<PostProxyClass> discoverposts;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private boolean LOAD_MORE = true;

    private int current_num;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mDisplayName = (TextView) view.findViewById(R.id.display_name);
        mUsername = (TextView) view.findViewById(R.id.username);
        mWebsite = (TextView) view.findViewById(R.id.website);
        mDescription = (TextView) view.findViewById(R.id.description);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        mContext = getActivity();
        setupToolbar();
        setProfileWidgets(MainActivity.currentUser);
        getFollowersCount();
        getFollowingCount();
        getPostsCount();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.addOnScrollListener(scrollListener);

        progressBar = (ProgressBar) view.findViewById(R.id.discoverprogress);

        fireBaseHelper = FireBaseHelper.GetInstance();

        discoverposts = new ArrayList<>();
        posts = new ArrayList<>();
        current_num = 0;

        fireBaseHelper
                .getDataBase()
                .collection("posts")
                .whereEqualTo("userid", MainActivity.currentUser.getUserid())
                .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        posts.addAll(documentSnapshots.toObjects(PostClass.class));
                        int end = current_num+9;
                        if(current_num<posts.size() && current_num<end) {
                            for (; current_num < end && current_num < posts.size(); current_num++) {
                                discoverposts.add(posts.get(current_num).PostProxy());
                            }
                        }
                        if(current_num>=posts.size()-1)
                        {
                            progressBar.setVisibility(GONE);
                            LOAD_MORE = false;
                        }
                        adapter = new DiscoverAdapter(getContext(), discoverposts, getActivity().getSupportFragmentManager());
                        recyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"No internet connection!",Toast.LENGTH_SHORT).show();
            }
        });

        TextView editProfile = (TextView) view.findViewById(R.id.textEditProfile);
        mLogout = (TextView) view.findViewById(R.id.logout);
        editProfile.setText("Edit Your Profile");
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment profileFragment = new EditProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content,profileFragment).addToBackStack(null).commit();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.currentUser.delete();
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(LOGIN, Context.MODE_PRIVATE).edit();
                editor.putLong(LOGGED_IN,0);
                editor.apply();
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!recyclerView.canScrollVertically(1)) {
                LoadMore();
            }
        }
    };

    private void LoadMore() {
        if(LOAD_MORE) {
            int end = current_num + 9;
            if(current_num<posts.size() && current_num<end) {
                for (; current_num < end && current_num < posts.size(); current_num++) {
                    discoverposts.add(posts.get(current_num).PostProxy());
                }
                adapter.notifyDataSetChanged();
            }
            if(current_num>=posts.size()-1) {
                LOAD_MORE = false;
                progressBar.setVisibility(GONE);
            }
        }
    }

    private void getFollowersCount(){
        mFollowersCount = 0;

        FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();
        fireBaseHelper.getDataBase().collection("following")
                .whereEqualTo("user2id",MainActivity.currentUser.getUserid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        mFollowersCount = documentSnapshots.size();
                        mFollowers.setText(mFollowersCount+"");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mFollowers.setText(mFollowersCount+"");
            }
        });
    }

    private void getFollowingCount(){
        mFollowingCount = 0;

        FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();
        fireBaseHelper.getDataBase().collection("following")
                .whereEqualTo("user1id",MainActivity.currentUser.getUserid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        mFollowingCount = documentSnapshots.size();
                        mFollowing.setText(mFollowingCount+"");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mFollowing.setText(mFollowingCount+"");
            }
        });

    }

    private void getPostsCount(){
        mPostsCount = 0;

        FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();
        fireBaseHelper.getDataBase().collection("posts")
                .whereEqualTo("userid",MainActivity.currentUser.getUserid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        mPostsCount = documentSnapshots.size();
                        mPosts.setText(mPostsCount+"");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mPosts.setText(mPostsCount+"");
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setProfileWidgets(UserClass user) {
        mDisplayName.setText(user.getName());
        mUsername.setText(user.getUsername());
        if(user.getGender()==0)
            mWebsite.setText("Male !");
        else
            mWebsite.setText("Female !");
        mDescription.setText(user.getEmail());
        if(user.getProfilepic()!=null) {
            GlideApp
                    .with(getContext())
                    .asBitmap()
                    .load(user.getProfilepic())
                    .circleCrop()
                    .into(mProfilePhoto);
        } else mProfilePhoto.setImageResource(R.drawable.user_pink);
    }

    private void setupToolbar(){
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment profileFragment = new EditProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content,profileFragment).addToBackStack(null).commit();
            }
        });
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

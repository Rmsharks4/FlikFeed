package com.thecoloredcolors.rmsha.flikfeed.Activities;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.thecoloredcolors.rmsha.flikfeed.Adapters.DiscoverAdapter;
import com.thecoloredcolors.rmsha.flikfeed.Adapters.SearchActivityPagerAdapter;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.SearchResult;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.PeopleFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.PostFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.ProfileFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.TagsFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.TopFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.UserProfileFragment;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class SearchActivity extends AppCompatActivity implements TopFragment.OnFragmentInteractionListener, PeopleFragment.OnFragmentInteractionListener, TagsFragment.OnFragmentInteractionListener, PostFragment.OnFragmentInteractionListener, UserProfileFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    private RecyclerView recyclerView;
    private DiscoverAdapter adapter;
    private FireBaseHelper fireBaseHelper;
    private List<PostClass> posts;
    private List<PostProxyClass> discoverposts;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private SearchView searchView;
    private LinearLayout searchResults;
    private LinearLayout discoverResults;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabLayout.Tab toptab;
    private TabLayout.Tab peopletab;
    private TabLayout.Tab tagstab;
    private TopFragment top;
    private PeopleFragment people;
    private TagsFragment tags;
    private ProgressBar progressBar;

    private List<SearchResult> searchposts = new ArrayList<>();
    private List<SearchResult> searchusers = new ArrayList<>();

    private boolean LOAD_USERS = false;
    private boolean LOAD_POSTS = false;

    private boolean LOAD_MORE = true;

    private int current_num;

    private String searchstr;

    private boolean SHOW_RESULTS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.recyclerView);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.addOnScrollListener(scrollListener);

        progressBar = findViewById(R.id.discoverprogress);

        fireBaseHelper = FireBaseHelper.GetInstance();

        discoverposts = new ArrayList<>();
        posts = new ArrayList<>();
        current_num = 0;

        fireBaseHelper
                .getDataBase()
                .collection("posts")
                .orderBy("time", Query.Direction.DESCENDING)
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
                            LOAD_MORE = false;
                            progressBar.setVisibility(GONE);
                        }
                        adapter = new DiscoverAdapter(SearchActivity.this, discoverposts, getSupportFragmentManager());
                        recyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SearchActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.searchtabLayout);
        toptab = tabLayout.newTab().setText("TOP");
        peopletab = tabLayout.newTab().setText("PEOPLE");
        tagstab = tabLayout.newTab().setText("TAGS");

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tab_selector));

        tabLayout.addTab(toptab);
        tabLayout.addTab(peopletab);
        tabLayout.addTab(tagstab);

        viewPager = (ViewPager) findViewById(R.id.searchviewPager);

        SearchActivityPagerAdapter searchadapter = new SearchActivityPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(searchadapter);

        top = (TopFragment) searchadapter.getTop();
        people = (PeopleFragment) searchadapter.getPeople();
        tags = (TagsFragment) searchadapter.getTags();

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(SHOW_RESULTS) {
                    StartSearch(searchstr);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        searchView = (SearchView) findViewById(R.id.searchview);
        searchResults = (LinearLayout) findViewById(R.id.searchresults);
        discoverResults = (LinearLayout) findViewById(R.id.discoverresults);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String cs) {
                if (TextUtils.isEmpty(cs)){
                    discoverResults.setVisibility(View.VISIBLE);
                    searchResults.setVisibility(GONE);
                    SHOW_RESULTS = false;
                } else {
                    searchstr = cs;
                    SHOW_RESULTS = true;
                    discoverResults.setVisibility(GONE);
                    searchResults.setVisibility(View.VISIBLE);

                    if(searchusers.isEmpty() && !LOAD_USERS)
                        LoadAllUsers(cs);
                    if(searchposts.isEmpty() && !LOAD_POSTS)
                        LoadAllPosts(cs);
                    if(!LOAD_USERS & !LOAD_POSTS) {
                        StartSearch(cs);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);

    }

    private void LoadAllPosts(final String cs) {
        LOAD_POSTS = true;
        fireBaseHelper.getDataBase().collection("posts")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        searchposts.clear();
                        for(PostClass p : documentSnapshots.toObjects(PostClass.class)) {
                            searchposts.add(new SearchResult(p.getPostid(),p.getCaption(),p.getTime(),p.getResources().get(0).getResource(),SearchResult.POST));
                        }
                        LOAD_POSTS = false;
                        if(!LOAD_USERS) StartSearch(cs);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                LOAD_POSTS = false;
            }
        });
    }

    private void LoadAllUsers(final String cs) {
        LOAD_USERS = true;
        fireBaseHelper.getDataBase().collection("users")
                .orderBy("name")
                .orderBy("username")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        searchusers.clear();
                        for(UserClass u : documentSnapshots.toObjects(UserClass.class)) {
                            searchusers.add(new SearchResult(u.getUserid(),u.getUsername(),u.getName(),u.getProfilepic(),SearchResult.USER));
                        }
                        LOAD_USERS = false;
                        if(!LOAD_POSTS) StartSearch(cs);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                LOAD_USERS = false;
            }
        });
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
            if(current_num>=posts.size()-1)
            {
                LOAD_MORE = false;
                progressBar.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void StartSearch(String cs) {
            if(viewPager.getCurrentItem()==0)
                top.LoadTopResults(cs.toLowerCase());
            else if(viewPager.getCurrentItem()==1)
                people.LoadPeopleResults(cs.toLowerCase());
            else
                tags.LoadTagsResults(cs.toLowerCase());
    }

    public List<SearchResult> getSearchposts() {
        return searchposts;
    }

    public List<SearchResult> getSearchusers() {
        return searchusers;
    }

}

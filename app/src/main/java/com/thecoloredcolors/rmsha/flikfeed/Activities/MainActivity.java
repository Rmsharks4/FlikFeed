package com.thecoloredcolors.rmsha.flikfeed.Activities;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.orm.SugarContext;
import com.thecoloredcolors.rmsha.flikfeed.Adapters.MainActivityPagerAdapter;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.DiscoverFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.EditProfileFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.HomeScreenFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.LikesPageFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.NotificationsFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.PostFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.ProfileFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.UploadFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.UserProfileFragment;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;
import com.thecoloredcolors.rmsha.flikfeed.ui.activities.UserListingActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static com.thecoloredcolors.rmsha.flikfeed.Activities.SplashActivity.LOGGED_IN;
import static com.thecoloredcolors.rmsha.flikfeed.Activities.SplashActivity.LOGIN;

public class MainActivity extends AppCompatActivity implements EditProfileFragment.OnFragmentInteractionListener, HomeScreenFragment.OnFragmentInteractionListener, DiscoverFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, PostFragment.OnFragmentInteractionListener, UserProfileFragment.OnFragmentInteractionListener, LikesPageFragment.OnFragmentInteractionListener, UploadFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<TabLayout.Tab> tabIdHistory = new ArrayList<TabLayout.Tab>();
    private TabLayout.Tab home;
    private TabLayout.Tab discover;
    private TabLayout.Tab upload;
    private TabLayout.Tab heart;
    private TabLayout.Tab profile;
    public static UserClass currentUser;
    private static ImageView shownotif;
    private final int DISCOVER_CODE = 132;
    private final int UPLOAD_CODE = 465;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SugarContext.init(this);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        long userid = getSharedPreferences(LOGIN, Context.MODE_PRIVATE).getLong(LOGGED_IN,0);
        if(userid!=0) {
            currentUser = UserClass.findById(UserClass.class,userid);
            if(currentUser!=null && currentUser.getProfilepic()==null) {
                currentUser.setProfilepic("");
                Log.e("user", currentUser.getUserid());
            }
        }

        shownotif = findViewById(R.id.shownotif);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        home = tabLayout.newTab().setIcon(R.drawable.home_pink);
        heart = tabLayout.newTab().setIcon(R.drawable.heart);
        upload = tabLayout.newTab().setIcon(R.drawable.camera);
        discover = tabLayout.newTab().setIcon(R.drawable.search);
        profile = tabLayout.newTab().setIcon(R.drawable.user);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tab_selector));

        tabLayout.addTab(home);
        tabLayout.addTab(heart);
        tabLayout.addTab(upload);
        tabLayout.addTab(discover);
        tabLayout.addTab(profile);

        tabIdHistory.add(home);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        MainActivityPagerAdapter mainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(mainActivityPagerAdapter);

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch(tab.getPosition())
                {
                    case 0:
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        home.setIcon(R.drawable.home_pink);
                        discover.setIcon(R.drawable.search);
                        upload.setIcon(R.drawable.camera);
                        heart.setIcon(R.drawable.heart);
                        profile.setIcon(R.drawable.user);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        break;
                    case 1:
                        shownotif.setVisibility(GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        home.setIcon(R.drawable.home);
                        discover.setIcon(R.drawable.search);
                        upload.setIcon(R.drawable.camera);
                        heart.setIcon(R.drawable.heart_pink);
                        profile.setIcon(R.drawable.user);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        break;
                    case 2:
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        home.setIcon(R.drawable.home);
                        discover.setIcon(R.drawable.search);
                        upload.setIcon(R.drawable.camera_pink);
                        heart.setIcon(R.drawable.heart);
                        profile.setIcon(R.drawable.user);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        PermissionListener permissionlistener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                Intent intent = new Intent(MainActivity.this, ImagePickerDemo.class);
                                startActivityForResult(intent,UPLOAD_CODE);
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                            }

                        };

                        new TedPermission(MainActivity.this)
                                .setPermissionListener(permissionlistener)
                                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .check();
                        break;
                    case 3:
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        home.setIcon(R.drawable.home);
                        discover.setIcon(R.drawable.search_pink);
                        upload.setIcon(R.drawable.camera);
                        heart.setIcon(R.drawable.heart);
                        profile.setIcon(R.drawable.user);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                        startActivityForResult(intent,DISCOVER_CODE);
                        break;
                    case 4:
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        home.setIcon(R.drawable.home);
                        discover.setIcon(R.drawable.search);
                        upload.setIcon(R.drawable.camera);
                        heart.setIcon(R.drawable.heart);
                        profile.setIcon(R.drawable.user_pink);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        if(getIntent().getExtras()!=null && getIntent().getExtras().size()>0) {
            String postid = getIntent().getStringExtra("postid");
            Bundle b = new Bundle();
            b.putString("postid", postid);
            final PostFragment postFragment = new PostFragment();
            postFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, postFragment).addToBackStack(null).commit();
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed()
    {
        if (tabIdHistory.size() > 0)
        {
            tabIdHistory.remove(tabIdHistory.size() - 1);
            if(tabIdHistory.size() > 0)
            {
                tabIdHistory.get(tabIdHistory.size()-1).select();

            }
            else
            {
                FragmentManager mgr = getSupportFragmentManager();
                if (mgr.getBackStackEntryCount() == 0) {
                    super.onBackPressed();
                } else {
                    mgr.popBackStack();
                }
            }
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==DISCOVER_CODE || requestCode==UPLOAD_CODE) {
                onBackPressed();
        }
    }

    public void MessagePlz(View view) {
        Intent intent = new Intent(MainActivity.this,UserListingActivity.class);
        startActivity(intent);
    }

    public static void GenerateNotifIcon() {
        shownotif.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.Flash)
                .repeat(YoYo.INFINITE)
                .playOn(shownotif);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

}

package com.thecoloredcolors.rmsha.flikfeed.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostResourceClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.Utils;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class PublishActivity extends AppCompatActivity {

    public static final String ARG_TAKEN_PHOTO_URI = "arg_taken_photo_uri";

    private static boolean VIDEO = false;
    private static boolean IMAGE = false;
    private int index = 0;

    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private EditText e;

    private ArrayList<Uri> photoUri = new ArrayList<>();
    private List<PostResourceClass> posts = new ArrayList<>();
    private int photoSize;

    public static void openWithVideoUri(Activity openingActivity, ArrayList<Uri> videoUri) {
        Intent intent = new Intent(openingActivity, PublishActivity.class);
        intent.putExtra(ARG_TAKEN_PHOTO_URI, videoUri);
        VIDEO = true;
        openingActivity.startActivity(intent);
    }

    public static void openWithAlbumUri(Activity openingActivity, ArrayList<Uri> uriList) {
        Intent intent = new Intent(openingActivity, PublishActivity.class);
        intent.putExtra(ARG_TAKEN_PHOTO_URI, uriList);
        IMAGE = true;
        openingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        ButterKnife.bind(this);

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_grey_24dp);
        } else {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        e = findViewById(R.id.etDescription);
        photoSize = getResources().getDimensionPixelSize(R.dimen.publish_photo_thumbnail_size);

        if (savedInstanceState == null) {
            photoUri = getIntent().getParcelableArrayListExtra(ARG_TAKEN_PHOTO_URI);
        } else {
            photoUri = savedInstanceState.getParcelableArrayList(ARG_TAKEN_PHOTO_URI);
        }
        updateStatusBarColor();

        ivPhoto.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ivPhoto.getViewTreeObserver().removeOnPreDrawListener(this);
                loadThumbnailPhoto();
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateStatusBarColor() {
        if (Utils.isAndroid5()) {
            getWindow().setStatusBarColor(0xff888888);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void loadThumbnailPhoto() {
        if(!photoUri.isEmpty()) {
            Glide.with(this).load(photoUri.get(0)).into(ivPhoto);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            bringMainActivityToTop();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void bringMainActivityToTop() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARG_TAKEN_PHOTO_URI, photoUri);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(PublishActivity.this, ImagePickerDemo.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void ShareButton(View view) {
        index = 0;
        UploadURIs();
        Toast.makeText(PublishActivity.this,"Uploading...",Toast.LENGTH_LONG).show();
    }

    private void UploadURIs() {
        if(index<photoUri.size()) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            String src = "";
            if (VIDEO)
                src = "videos/";
            else if (IMAGE)
                src = "images/";
            StorageReference riversRef = storage.getReference(src + photoUri.get(index).getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(photoUri.get(index));
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(PublishActivity.this,"Upload failed!",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PublishActivity.this,"Uploading...",Toast.LENGTH_LONG).show();
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    if(IMAGE)
                        posts.add(new PostResourceClass(downloadUrl.toString(), 0));
                    else if(VIDEO)
                        posts.add(new PostResourceClass(downloadUrl.toString(),1));
                    Log.e("next",index+"");
                    index++;
                    UploadURIs();
                }
            });
        } else {
            Toast.makeText(PublishActivity.this,"Uploading...",Toast.LENGTH_LONG).show();
            DocumentReference d = FireBaseHelper.GetInstance().getDataBase().collection("posts")
                            .document();
                    PostClass mypost = new PostClass(d.getId(),e.getText().toString(), new Timestamp(System.currentTimeMillis() / 1000).toString(), MainActivity.currentUser.getUserid(), posts, 0, 0);
                    d.set(mypost).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            bringMainActivityToTop();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PublishActivity.this,"Upload failed!",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}

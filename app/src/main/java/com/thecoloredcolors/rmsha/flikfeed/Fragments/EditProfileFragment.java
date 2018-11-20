package com.thecoloredcolors.rmsha.flikfeed.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Flash;
import com.thecoloredcolors.rmsha.flikfeed.Activities.ImagePickerDemo;
import com.thecoloredcolors.rmsha.flikfeed.Activities.MainActivity;
import com.thecoloredcolors.rmsha.flikfeed.Activities.PublishActivity;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.GlideApp;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;

public class EditProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public EditProfileFragment() {
    }

    public static EditProfileFragment newInstance(String param1, String param2) {
        return new EditProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static final String TAG = "EditProfileFragment";

    private EditText mDisplayName, mUsername, mWebsite, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;
    private CircleImageView mProfilePhoto;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    TedBottomPicker bottomSheetDialogFragment;

    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout bottomSheetView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mDisplayName = (EditText) view.findViewById(R.id.display_name);
        mUsername = (EditText) view.findViewById(R.id.username);
        mWebsite = (EditText) view.findViewById(R.id.website);
        mEmail = (EditText) view.findViewById(R.id.email);
        mPhoneNumber = (EditText) view.findViewById(R.id.phoneNumber);
        mChangeProfilePhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);

        setProfileWidgets(MainActivity.currentUser);

        ImageView checkmark = (ImageView) view.findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfUsernameExists(mUsername.getText().toString());
            }
        });

        return view;
    }

    private void checkIfUsernameExists(final String username) {
        FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();
        fireBaseHelper.getDataBase().collection("users")
                .whereEqualTo("username",username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if(documentSnapshots.size()>0) {
                            if(!documentSnapshots.getDocuments().get(0).getString("userid").equals(MainActivity.currentUser.getUserid()))
                                Toast.makeText(getContext(),"Username already exists!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),"Saving Changes!...",Toast.LENGTH_SHORT).show();
                            final String name = mDisplayName.getText().toString();
                            final String username = mUsername.getText().toString();
                            final String address = mWebsite.getText().toString();
                            final String email = mEmail.getText().toString();
                            final String phoneNumber = mPhoneNumber.getText().toString();

                            MainActivity.currentUser.setName(name);
                            MainActivity.currentUser.setUsername(username);
                            MainActivity.currentUser.setAddress(address);
                            MainActivity.currentUser.setEmail(email);
                            MainActivity.currentUser.setPhonenumber(phoneNumber);

                            FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();
                            fireBaseHelper.getDataBase().collection("users")
                                    .document(MainActivity.currentUser.getUserid())
                                    .set(MainActivity.currentUser, SetOptions.merge());
                            MainActivity.currentUser.update();
                            Toast.makeText(getContext(),"Changes Saved!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"An error occured! Try again later!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProfileWidgets(final UserClass user){
        mDisplayName.setText(user.getName());
        mUsername.setText(user.getUsername());
        if(user.getAddress()!=null)
            mWebsite.setText(user.getAddress());
        mEmail.setText(user.getEmail());
        if(user.getPhonenumber()!=null)
            mPhoneNumber.setText(user.getPhonenumber());
        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogFragment = new TedBottomPicker.Builder(getActivity())
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();
                                fireBaseHelper.getDataBase().collection("users")
                                        .document(user.getUserid())
                                        .update("profilepic",uri.toString());
                                user.setProfilepic(uri.toString());
                                user.update();
                                if(user.getProfilepic()!=null) {
                                    GlideApp
                                            .with(getContext())
                                            .asBitmap()
                                            .load(user.getProfilepic())
                                            .circleCrop()
                                            .into(mProfilePhoto);
                                } else mProfilePhoto.setImageResource(R.drawable.user_pink);
                            }
                        })
                        .setPeekHeight(getResources().getDisplayMetrics().heightPixels)
                        .showCameraTile(false)
                        .showGalleryTile(true)
                        .setCompleteButtonText(getString(R.string.done))
                        .setEmptySelectionText(getString(R.string.no_select))
                        .create();

                bottomSheetDialogFragment.show(getChildFragmentManager());
            }
        });
        if(user.getProfilepic()!=null) {
            GlideApp
                    .with(getContext())
                    .asBitmap()
                    .load(user.getProfilepic())
                    .circleCrop()
                    .into(mProfilePhoto);
        } else mProfilePhoto.setImageResource(R.drawable.user_pink);
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

    @Override
    public void onResume() {
        super.onResume();
    }
}

package com.thecoloredcolors.rmsha.flikfeed.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;
import com.thecoloredcolors.rmsha.flikfeed.core.registration.RegisterContract;
import com.thecoloredcolors.rmsha.flikfeed.core.registration.RegisterPresenter;
import com.thecoloredcolors.rmsha.flikfeed.core.users.add.AddUserContract;
import com.thecoloredcolors.rmsha.flikfeed.core.users.add.AddUserPresenter;
import com.thecoloredcolors.rmsha.flikfeed.models.User;
import com.thecoloredcolors.rmsha.flikfeed.utils.Constants;
import com.thecoloredcolors.rmsha.flikfeed.utils.SharedPrefUtil;

import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.thecoloredcolors.rmsha.flikfeed.Activities.SplashActivity.LOGGED_IN;

public class SignupActivity extends AppCompatActivity implements RegisterContract.View, AddUserContract.View {

    private EditText edtname, edtemail, edtuser, edtpass, confirmpass;
    private RadioGroup gender;
    private int genderid;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";

    private RegisterPresenter mRegisterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);

        edtname = (EditText) findViewById(R.id.name);
        edtemail = (EditText) findViewById(R.id.email);
        edtuser = (EditText) findViewById(R.id.username);
        edtpass = (EditText) findViewById(R.id.password);
        confirmpass = (EditText) findViewById(R.id.confirm_password);
        gender = (RadioGroup) findViewById(R.id.radioSex);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void OpenLoginView(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createAccount(View view) {
        String email=edtemail.getText().toString();
        String password=edtpass.getText().toString();
        Log.d(TAG, "createAccount:" + email);
        if (validateForm()) {
            return;
        }

        mRegisterPresenter = new RegisterPresenter(this);
        mRegisterPresenter.register(this, email, password);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean validateForm() {
     boolean error = false;
        genderid = 0;
        if(gender.getCheckedRadioButtonId()==R.id.radioFemale)
            genderid = 1;
        if (edtname.getText().toString().trim().length() == 0) {
            edtname.setError("Full Name is required");
            edtname.requestFocus();
            error = true;
        } else if (edtname.getText().toString().length() > 40) {
            edtname.setError("Full Name should not be more than 40 chars");
            edtname.requestFocus();
            error = true;
        } else if (edtemail.getText().toString().trim().length() == 0) {
            edtemail.setError("This field is required");
            edtemail.requestFocus();
            error = true;
        } else {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (!edtemail.getText().toString().matches(emailPattern)) {
                edtemail.setError("Email is not vaild");
                edtemail.requestFocus();
                error = true;
            }
        }
        if (edtuser.getText().toString().trim().length() == 0) {
            edtuser.setError("Username required");
            edtuser.requestFocus();
            error = true;
        } else if (edtuser.getText().toString().length() > 8) {
            edtuser.setError("Username can not be longer than 8 chars");
            edtuser.requestFocus();
            error = true;
        }
        if (edtpass.getText().toString().trim().length() == 0) {
            edtpass.setError("Password is required");
            edtpass.requestFocus();
            error = true;
        } else if (edtpass.getText().toString().length() < 4) {
            edtpass.setError("Password is too short");
            edtpass.requestFocus();
            error = true;
        }
        if (confirmpass.getText().toString().trim().length() == 0) {
            confirmpass.setError("This field is required");
            confirmpass.requestFocus();
            error = true;
        } else if (!Objects.equals(confirmpass.getText().toString(), edtpass.getText().toString())) {
            confirmpass.setError("Passwords dont match");
            confirmpass.requestFocus();
            error = true;
        }
        return error;
    }

    @Override
    public void onRegistrationSuccess(final FirebaseUser firebaseUser) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final User user = new User(firebaseUser.getUid(),
                firebaseUser.getEmail(),
                new SharedPrefUtil(this).getString(Constants.ARG_FIREBASE_TOKEN));
        database.child(Constants.ARG_USERS)
                .child(firebaseUser.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            UserClass userClass = new UserClass(firebaseUser.getUid(),edtuser.getText().toString(),edtpass.getText().toString(),edtname.getText().toString(),edtemail.getText().toString(),genderid);
                            FireBaseHelper.GetInstance().AddNewUser(userClass);
                            Toast.makeText(SignupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onRegistrationFailure(String message) {
        Toast.makeText(SignupActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddUserSuccess(String message) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAddUserFailure(String message) {
        Toast.makeText(SignupActivity.this, "kharab ho gaya",
                Toast.LENGTH_SHORT).show();
    }
}

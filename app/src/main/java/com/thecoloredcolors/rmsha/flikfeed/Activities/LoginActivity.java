package com.thecoloredcolors.rmsha.flikfeed.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.orm.SugarContext;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;
import com.thecoloredcolors.rmsha.flikfeed.core.login.LoginContract;
import com.thecoloredcolors.rmsha.flikfeed.core.login.LoginPresenter;
import com.thecoloredcolors.rmsha.flikfeed.utils.Constants;
import com.thecoloredcolors.rmsha.flikfeed.utils.SharedPrefUtil;

import static android.content.ContentValues.TAG;
import static com.thecoloredcolors.rmsha.flikfeed.Activities.SplashActivity.LOGGED_IN;
import static com.thecoloredcolors.rmsha.flikfeed.Activities.SplashActivity.LOGIN;

public class LoginActivity extends AppCompatActivity implements LoginContract.View{

    private AutoCompleteTextView edtemail;
    private EditText edtpass;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        SugarContext.init(this);
        setContentView(R.layout.activity_login);

        edtemail = (AutoCompleteTextView) findViewById(R.id.email);
        edtpass = (EditText) findViewById(R.id.password);

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
    public void OpenSignupView(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
    public void OpenWelcomeView(View view) {
        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public void signIn(View view) {
        String email=edtemail.getText().toString();
        String password=edtpass.getText().toString();
        Log.d(TAG, "signIn:" + email);
        if (validateForm()) {
            return;
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userid = "";
                    if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FireBaseHelper.GetInstance().getDataBase().collection("users")
                            .document(userid)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()) {
                                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                                        UserClass u = documentSnapshot.toObject(UserClass.class);
                                        u.save();
                                        SharedPreferences.Editor editor = getSharedPreferences(LOGIN, Context.MODE_PRIVATE).edit();
                                        editor.putLong(LOGGED_IN,u.getId());
                                        editor.apply();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Login Unsuccessful!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean error = false;

        if (edtemail.getText().toString().trim().length() == 0) {
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
        if (edtpass.getText().toString().trim().length() == 0) {
            edtpass.setError("Password is required");
            edtpass.requestFocus();
            error = true;
        } else if (edtpass.getText().toString().length() < 4) {
            edtpass.setError("Password is too short");
            edtpass.requestFocus();
            error = true;
        }

        return error;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    @Override
    public void onLoginSuccess(String message) {
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences.Editor editor = getSharedPreferences(LOGIN, Context.MODE_PRIVATE).edit();
        editor.putBoolean(LOGGED_IN,true);
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(String message) {
        Toast.makeText(LoginActivity.this, "LOGIN FAILED",
                Toast.LENGTH_SHORT).show();
    }
}


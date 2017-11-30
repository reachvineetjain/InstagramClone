package com.nehvin.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    EditText username;
    EditText password;
    Button signup;
    TextView changeSignUpText;
    LinearLayout bckLinearLayout;
    ImageView logoImageView;
    Intent listUser;
    Boolean signUpModeActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listUser = new Intent(this, UserListActivity.class);
        if(ParseUser.getCurrentUser() != null)
        {
            Log.i("Current user logged in ", ParseUser.getCurrentUser().getUsername());
            Toast.makeText(this, "Current User"+ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
            showUserList();
        }
        else
        {
            setContentView(R.layout.activity_main);
            Log.i("No user logged in", "please login or sign up");
            Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
            initialize();
        }

    }

    private void initialize() {
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        password.setOnKeyListener(this);
        signup = findViewById(R.id.signupButton);
        changeSignUpText = findViewById(R.id.changeSignUpText);
        changeSignUpText.setOnClickListener(this);
        bckLinearLayout = findViewById(R.id.bckLinearLayout);
        bckLinearLayout.setOnClickListener(this);
        logoImageView = findViewById(R.id.logoImageView);
        logoImageView.setOnClickListener(this);

    }

    public void signUp(View view) {

        if(username.getText().toString().matches("") || password.getText().toString().matches(""))
        {
            Toast.makeText(this, "A username and password is required", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (signUpModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)
                        {
                            Log.i("Sign up", "successful");
                            showUserList();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(),
                        new LogInCallback()
                        {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(user != null)
                                {
                                    Log.i("Login Successful ", user.getUsername());
                                    showUserList();
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,
                                            "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.changeSignUpText)
        {
            Log.i("App info", "change to login mode");
            if (signUpModeActive)
            {
                signUpModeActive = false;
                signup.setText(R.string.loginText);
                changeSignUpText.setText(R.string.orSignUpText);
            }
            else
            {
                signUpModeActive = true;
                signup.setText(R.string.signupButton);
                changeSignUpText.setText(R.string.orLogin);
            }
        }
        else{
            if(v.getId() == R.id.bckLinearLayout || v.getId() == R.id.logoImageView)
            {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null && getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            signUp(v);
        }

        return false;
    }

    public void showUserList()
    {

        startActivity(listUser);
    }
}
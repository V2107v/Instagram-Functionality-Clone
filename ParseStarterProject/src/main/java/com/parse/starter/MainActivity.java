/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener, View.OnKeyListener {

  TextView loginTextView;
  boolean signUpActive = true;
  EditText userName, password;
  RelativeLayout backgroundLayout;

  public void showUserList () {
    Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
    startActivity(intent);
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
      signUp(view);
    }

    return false;
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.login) {
      Button signUp = findViewById(R.id.signup);

      if(signUpActive) {
         signUpActive = false;
         signUp.setText("Login");
         loginTextView.setText("or, SignUp");
      } else{
         signUpActive = true;
         signUp.setText("SignUp");
         loginTextView.setText("or, Login");
      }
    } else if (view.getId() == R.id.backgroundLayout){
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
  }

  public void signUp (View view) {
    if(userName.getText().toString().matches("") || password.getText().toString().matches("")) {
      Toast.makeText(this, "A Username and a password are Required.", Toast.LENGTH_SHORT).show();
    } else{
      if(signUpActive) {
        ParseUser user = new ParseUser();
        user.setUsername(userName.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("SignUp", "Success");
              Toast.makeText(MainActivity.this, "SignUp Success", Toast.LENGTH_SHORT).show();
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      } else {
        ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if( user != null) {
              Log.i("LogIn","Success");
              Toast.makeText(MainActivity.this, "LogIn Success", Toast.LENGTH_SHORT).show();
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Login/SignUp");

    loginTextView =  findViewById(R.id.login);
    loginTextView.setOnClickListener(this);

    userName = findViewById(R.id.userName);
    password = findViewById(R.id.password);
    backgroundLayout = findViewById(R.id.backgroundLayout);
    backgroundLayout.setOnClickListener(this);

    password.setOnKeyListener(this);

    if(ParseUser.getCurrentUser() != null) {
      showUserList();
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }



}
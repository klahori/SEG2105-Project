package com.example.user.loginsignup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //defining views
    private Button buttonSignIn,btnSignup;
    private EditText editTextEmail;
    private EditText editTextPassword;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();



        //initializing views
        editTextEmail = (EditText) findViewById(R.id.text_Email);
        editTextPassword = (EditText) findViewById(R.id.text_Password);
        buttonSignIn = (Button) findViewById(R.id.signinBtn);
        btnSignup  = (Button) findViewById(R.id.signupBtn);


        //attaching click listener
        buttonSignIn.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    //method for user login
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }




        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if the task is successfull
                        if (task.isSuccessful()) {
                            //start the welcome activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));

                            Toast.makeText(MainActivity.this, getString(R.string.signin_success), Toast.LENGTH_LONG).show();

                        } else {
                            //sign in failed stay in log in page(main activity) and tell user that sign in failed
                            Toast.makeText(MainActivity.this, getString(R.string.sigin_fail), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
        public void signup(){
        // goes to signup activity
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }


    @Override
    public void onClick(View view) {
        //if button pressed
        if(view == buttonSignIn){
            //does log in function
            userLogin();
        }
            // if button pressed
        if(view == btnSignup){
            //do the signup function
            signup();
            finish();
        }
    }
}
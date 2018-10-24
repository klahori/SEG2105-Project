package com.example.user.loginsignup;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.getBoolean;

public class SignupActivity extends AppCompatActivity  implements View.OnClickListener {

        private EditText editTextName, editTextEmail, editTextPassword, editTextPhone,editTextAddress,editTextLastName,editTextRole,editTextUsername;
        private ProgressBar progressBar;
        private FirebaseAuth mAuth;
        private  String Admin;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);
            editTextUsername=findViewById(R.id.editText_username);
            editTextRole=findViewById(R.id.role_type);
            editTextName = findViewById(R.id.edit_text_name);
            editTextLastName = findViewById(R.id.lastName);
            editTextAddress = findViewById(R.id.address);

            editTextEmail = findViewById(R.id.edit_text_email);
            editTextPassword = findViewById(R.id.edit_text_password);
            editTextPhone = findViewById(R.id.edit_text_phone);
            progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.GONE);

            mAuth = FirebaseAuth.getInstance();

            findViewById(R.id.button_register).setOnClickListener(this);
        }



        @Override
        protected void onStart() {
            super.onStart();

            if (mAuth.getCurrentUser() != null) {
            }
        }














        private void goBack(){
            Intent intent = new Intent(SignupActivity.this,MainActivity.class);
            startActivity(intent);
        }
        private void registerUser() {


            final String name = editTextName.getText().toString().trim();
            final String username = editTextUsername.getText().toString().trim();

            final String email = editTextEmail.getText().toString().trim();
            final String last = editTextLastName.getText().toString().trim();
            final String address = editTextAddress.getText().toString().trim();
            final String password = editTextPassword.getText().toString().trim();
            final String phone = editTextPhone.getText().toString().trim();
            final String role = editTextRole.getText().toString().trim();

            if (name.isEmpty()) {
                editTextName.setError(getString(R.string.input_error_first_name));
                editTextName.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                editTextEmail.setError(getString(R.string.input_error_email));
                editTextEmail.requestFocus();
                return;
            }

            if (last.isEmpty()) {
                editTextLastName.setError(getString(R.string.input_error_last_name));
                editTextLastName.requestFocus();
                return;
            }

            if (address.isEmpty()) {
                editTextAddress.setError(getString(R.string.input_error_address));
                editTextAddress.requestFocus();
                return;
            }


            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError(getString(R.string.input_error_email_invalid));
                editTextEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                editTextPassword.setError(getString(R.string.input_error_password));
                editTextPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                editTextPassword.setError(getString(R.string.input_error_password_length));
                editTextPassword.requestFocus();
                return;
            }

            if (phone.isEmpty()) {
                editTextPhone.setError(getString(R.string.input_error_phone));
                editTextPhone.requestFocus();
                return;
            }

            if (phone.length() != 10) {
                editTextPhone.setError(getString(R.string.input_error_phone_invalid));
                editTextPhone.requestFocus();
                return;
            }
            if (role.isEmpty()) {
                editTextRole.setError(getString(R.string.input_error_role));
                editTextRole.requestFocus();
                return;
            }


            if (!(role.equals("Admin") || role.equals("Service Provider") || role.equals("Home Owner"))) {
                editTextRole.setError(getString(R.string.input_error_role_invalid));
                editTextRole.requestFocus();
                return;
            }


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("Users");

// Attach a listener to read the daVta at our posts reference
            ref.orderByChild("username")
                    .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        editTextUsername.setError(getString(R.string.input_error_username));
                        editTextUsername.requestFocus();

                    } else

                    {

                        if (!(role.equals("Admin"))){
                            Admin="role";
                        }else{
                            Admin="Admin";
                        }
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference().child("Users");
// Attach a listener to read the daVta at our posts reference
                        ref.orderByChild("role").equalTo(Admin).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    editTextRole.setError(getString(R.string.input_error_admin));
                                    editTextRole.requestFocus();

                                } else

                                {


                                    progressBar.setVisibility(View.VISIBLE);
                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {

                                                    if (task.isSuccessful()) {

                                                        User user = new User(
                                                                username,
                                                                name,
                                                                last,
                                                                email,
                                                                phone,
                                                                address,
                                                                role

                                                        );

                                                        FirebaseDatabase.getInstance().getReference("Users")
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                progressBar.setVisibility(View.GONE);
                                                                if (task.isSuccessful()) {
                                                                    goBack();
                                                                    Toast.makeText(SignupActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    //display a failure message
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }




    @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_register:
                    registerUser();

                    break;
            }
        }
    }

package com.example.user.loginsignup;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class SignupActivity extends AppCompatActivity  implements View.OnClickListener {
    //defining views
        private EditText editTextName, editTextEmail, editTextPassword, editTextPhone,editTextAddress,editTextLastName,editTextRole,editTextUsername,editTextDay,editTextMonth,editTextYear;
        private ProgressBar progressBar;
        private FirebaseAuth mAuth;
        private  String Admin;
        private String roleT;
        private RadioGroup roleType;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);
            //initializing views
            editTextMonth =findViewById(R.id.editText_Month);
            editTextDay = findViewById(R.id.editText_day);
            editTextYear =findViewById(R.id.editText_Year);
            editTextUsername=findViewById(R.id.editText_username);
                //editTextRole=findViewById(R.id.role_type);
            editTextName = findViewById(R.id.edit_text_name);
            editTextLastName = findViewById(R.id.lastName);
            editTextAddress = findViewById(R.id.address);
            editTextEmail = findViewById(R.id.edit_text_email);
            editTextPassword = findViewById(R.id.edit_text_password);
            editTextPhone = findViewById(R.id.edit_text_phone);
            progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.GONE);
            //getting firebase auth object
            mAuth = FirebaseAuth.getInstance();
            //Radio Buttons
            roleType = findViewById(R.id.role_type);
            findViewById(R.id.button_register).setOnClickListener(this);
        }


        @Override
        protected void onStart() {
            super.onStart();

            if (mAuth.getCurrentUser() != null) {
            }
        }

            //function that goes back to login in page (MainActivity)
        private void goBack(){
            Intent intent = new Intent(SignupActivity.this,MainActivity.class);
            startActivity(intent);
        }
        //function that registers users using the info given in the text boxes
        private void registerUser() {

            // defining views
            final String name = editTextName.getText().toString().trim();
            final String username = editTextUsername.getText().toString().trim();
            final String day=editTextDay.getText().toString().trim();
            final String month=editTextMonth.getText().toString().trim();
            final String year=editTextYear.getText().toString().trim();
            final String email = editTextEmail.getText().toString().trim();
            final String last = editTextLastName.getText().toString().trim();
            final String address = editTextAddress.getText().toString().trim();
            final String password = editTextPassword.getText().toString().trim();
            final String phone = editTextPhone.getText().toString().trim();
            //final String role = editTextRole.getText().toString().trim();
            final String role = roleT;
// validation texts cannot be left empty
            if (name.isEmpty()) {
                editTextName.setError(getString(R.string.input_error_first_name));
                editTextName.requestFocus();
                return;
            }

            if (day.isEmpty()) {
                editTextDay.setError(getString(R.string.input_error_day));
                editTextDay.requestFocus();
                return;
            }
            if (month.isEmpty()) {
                editTextMonth.setError(getString(R.string.input_error_month));
                editTextMonth.requestFocus();
                return;
            }
            if (year.isEmpty()) {
                editTextYear.setError(getString(R.string.input_error_year));
                editTextYear.requestFocus();
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

// email text must have a valid email
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
// password must be greator that 6 characters(to be a good password)
            if (password.length() < 6) {
                editTextPassword.setError(getString(R.string.input_error_password_length));
                editTextPassword.requestFocus();
                return;
            }
            // day month can only have 2 numbers
            if (day.matches("[0-9]+") && day.length() != 2) {
                editTextDay.setError(getString(R.string.input_error_day_length));
                editTextDay.requestFocus();
                return;
            }


            if (month.matches("[0-9]+") && month.length() != 2) {
                editTextMonth.setError(getString(R.string.input_error_month_length));
                editTextMonth.requestFocus();
                return;
            }
            // year must only have 4 numbers
            if (year.matches("[0-9]+") && year.length() != 4) {
                editTextYear.setError(getString(R.string.input_error_year_length));
                editTextYear.requestFocus();
                return;
            }
            if (phone.isEmpty()) {
                editTextPhone.setError(getString(R.string.input_error_phone));
                editTextPhone.requestFocus();
                return;
            }
                // phone number length can only be 10
            if (phone.length() != 10) {
                editTextPhone.setError(getString(R.string.input_error_phone_invalid));
                editTextPhone.requestFocus();
                return;
            }
            if(roleType.getCheckedRadioButtonId() == -1) {
                Context context = getApplicationContext();
                //CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, getString(R.string.input_error_role), duration);
                toast.show();

                //Toast.makeText(SignupActivity.this, getString(R.string.input_error_role), Toast.LENGTH_LONG).show();
                //editTextRole.setError(getString(R.string.input_error_role));
                //editTextRole.requestFocus();
                return;
            }

            // in the data base on user
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("Users");

// Attach a listener to read the daVta at our posts reference
            ref.orderByChild("username")// go through all the usernames
                    .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {// if the one entered matches a username that already exist give an error saying username already exists
                        editTextUsername.setError(getString(R.string.input_error_username));
                        editTextUsername.requestFocus();

                    } else

                    {
                            // else if the role was Admin
                        if (!(role.equals("Admin"))){
                            Admin="role";
                        }else{
                            Admin="Admin";
                        }
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference().child("Users");
// Attach a listener to read the daVta at our posts reference
                        ref.orderByChild("role").equalTo(Admin).addListenerForSingleValueEvent(new ValueEventListener() {// go through to check if there already is an admin account
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //editTextRole.setError(getString(R.string.input_error_admin));//if there already is an admin account give an error since we can only have one admin account
                                    //editTextRole.requestFocus();
                                    Context context = getApplicationContext();
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, getString(R.string.input_error_admin), duration);
                                    toast.show();
                                } else

                                {


                                    progressBar.setVisibility(View.VISIBLE);
                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {

                                                    if (task.isSuccessful()) {// else the user has passed all the validation and has a unique account therefore make the account

                                                        User user = new User(
                                                                username,
                                                                name,
                                                                last,
                                                                email,
                                                                phone,
                                                                address,
                                                                role,
                                                                day,
                                                                month,
                                                                year

                                                        );




                                                        FirebaseDatabase.getInstance().getReference("Users")
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                progressBar.setVisibility(View.GONE);
                                                                if (task.isSuccessful()) {
                                                                    goBack();//if user account was made follow go back function
                                                                    Toast.makeText(SignupActivity.this, getString(R.string.registration_Success), Toast.LENGTH_LONG).show();// tell the user account was made
                                                                } else {
                                                                    //display a failure message
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();// if error tell user account was not made and the get the reason why
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

        //Radio Buttons
        public void onRadioButtonClicked(View view) {

            // Is the button now checked?
            boolean checked = ((RadioButton) view).isChecked();

            // Check which radio button was clicked
            switch(view.getId()) {
                case R.id.radio_admin:
                    if (checked)
                        // Admin
                        roleT = "Admin";
                    break;
                case R.id.radio_sp:
                    if (checked)
                        // Service Provider
                        roleT = "Home Owner";
                    break;
                case R.id.radio_hm:
                    if (checked)
                        // Home Owner
                        roleT = "Service Provider";
                    break;
            }
        }



    @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_register:// if register button pressed
                    registerUser();//follow registerUser function
                    break;
            }
        }
    }

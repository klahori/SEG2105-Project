package com.example.user.loginsignup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference databaseUser;
    private EditText phoneNumEdit,addressEdit,companyEdit,licenceEdit;
    private MultiAutoCompleteTextView descriptionEdit;

    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        phoneNumEdit = (EditText)findViewById(R.id.phoneNum);
        addressEdit = (EditText)findViewById(R.id.address);
        companyEdit = (EditText)findViewById(R.id.companyName);
        licenceEdit= findViewById(R.id.licensed);
        descriptionEdit= findViewById(R.id.description);
        findViewById(R.id.submitBtn).setOnClickListener(this);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //getting the user special id from logged in user
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in
        DatabaseReference uidRef = rootRef.child("Users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting the name and role of logged in user
                String phoneNumber = dataSnapshot.child("phone").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String companyName = dataSnapshot.child("companyName").getValue(String.class);
                String licence=dataSnapshot.child("licence").getValue(String.class);
                String description =dataSnapshot.child("description").getValue(String.class);
                // setting the text so it welcome the user by first name and tells them they are logged in as the role they  have
                phoneNumEdit.setText(phoneNumber);
                addressEdit.setText(address);
                if (companyName!=null) {
                    companyEdit.setText(companyName);
                }
                else{
                    companyEdit.setHint("Company Name");
                }
                if (licence!=null) {
                    licenceEdit.setText(licence);
                }
                else{
                    licenceEdit.setHint("Are you Licenced please type Yes or No");
                }
                if (description!=null) {
                    descriptionEdit.setText(description);
                }
                else{
                    descriptionEdit.setHint("Please enter a description about yourself");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);

    }

    void completeProfile(){
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        //getting the user special id from logged in user
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in
        DatabaseReference uidRef = rootRef1.child("Users").child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //getting the name and role of logged in user
                String phoneNumber = dataSnapshot.child("phone").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                String day = dataSnapshot.child("day").getValue(String.class);
                String month  = dataSnapshot.child("month").getValue(String.class);
                String year = dataSnapshot.child("year").getValue(String.class);
                String role = dataSnapshot.child("role").getValue(String.class);
                String username = dataSnapshot.child("username").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);




                // setting the text so it welcome the user by first name and tells them they are logged in as the role they  have
                final String companyNamef = companyEdit.getText().toString().trim();
                final String descriptionf= descriptionEdit.getText().toString().trim();
                final String licence= licenceEdit.getText().toString().trim();
                if (companyNamef.isEmpty()) {
                    companyEdit.setError(getString(R.string.companyError));
                    companyEdit.requestFocus();
                    return;
                }

                User user = new User();
                user.setCompanyName(companyNamef);
                user.setFirstName(firstName);
                user.setAddress(address);
                user.setDay(day);
                user.setEmail(email);
                user.setLastName(lastName);
                user.setMonth(month);
                user.setRole(role);
                user.setPhone(phoneNumber);
                user.setYear(year);
                user.setUsername(username);
                user.setDescription(descriptionf);
                user.setLicence(licence);




        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //getting the user special id from logged in userFirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //going through database user and special id  to get to the specific user logged in

        rootRef.child("Users").child(firebaseUser.getUid()).setValue(user).
                addOnCompleteListener(ProfileActivity.this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this, getString(R.string.updatedInfo), Toast.LENGTH_LONG).show();// tell the user account was made

                                } else {
                                    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();// if error tell user account was not made and the get the reason why
                                }
                            }
                        });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
            }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:// if register button pressed
                completeProfile();//follow registerUser function
                break;
        }
    }



}

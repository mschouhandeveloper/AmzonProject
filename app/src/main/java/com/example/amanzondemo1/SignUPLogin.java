package com.example.amanzondemo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUPLogin extends AppCompatActivity {

    //TextInputEditText name, mob_mail, password;
    TextInputLayout t_name,t_mob_mail,t_password,t_login_mailmob,t_login_password;

    RelativeLayout loginlay,signuplay;
    RadioGroup radioGroup;


    // for User Registeratino Validation
    String email_input;
    String passwordInput;
    String name;

    // For user Login Vlai
    String login_email,login_password;

    /// Firebase
    private FirebaseAuth mAuth;


    // User Profile data
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    int ival=0;

    // Progress bar
    ProgressBar loginprog,signupprog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p_login);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        if(user!=null){
            userID = user.getUid();

        }else{
            ival=1;

        }
        FirebaseApp.initializeApp(SignUPLogin.this);

        RadioButtonView();
        Signup();
        Login();



    }

private void RadioButtonView() {



    loginprog = findViewById(R.id.loginprog);
    signupprog = findViewById(R.id.signupprog);

    loginlay = findViewById(R.id.loginlay);
    signuplay = findViewById(R.id.signuplay);
    radioGroup = findViewById(R.id.groupradio);

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

//             if(ival==1){
//                 loginlay.setVisibility(View.GONE);
//                 signuplay.setVisibility(View.VISIBLE);
//                 radioGroup.check(R.id.signup);
//                 Log.i("ivalue in else","In I -: "+ ival);
//             }

            // Check which radio button was clicked
          //  Log.i("CheckedId",Integer.toString(checkedId));

            if(R.id.signup==checkedId){
                Log.i("CheckedId",Integer.toString(checkedId)+"  Signup");
                loginlay.setVisibility(View.GONE);
                signuplay.setVisibility(View.VISIBLE);
                ival=0;
            } else if(R.id.login == checkedId){
                Log.i("CheckedId",Integer.toString(checkedId)+"  Login");
                signuplay.setVisibility(View.GONE);
                loginlay.setVisibility(View.VISIBLE);
                ival=0;
            }
        }
    });
}

    private void Signup() {
        t_name = findViewById(R.id.t_name);
        t_mob_mail = findViewById(R.id.t_mailmob);
        t_password = findViewById(R.id.t_password);

    }

    private void Login() {
        t_login_mailmob = findViewById(R.id.t_login_mailmob);
        t_login_password = findViewById(R.id.t_login_password);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
        //    startActivity(new Intent(MainActivity.this, UserActivity.class));
            finish();
        }
    }


    public  void submitsignup(View view){

        signupprog.setVisibility(View.VISIBLE);

        if (!validateEmail() | !validateUsername() | !validatePassword()) {
            return;
        }
        String input = "Email: " + t_mob_mail.getEditText().getText().toString();
        input += "\n";
        input += "Username: " + t_name.getEditText().getText().toString();
        input += "\n";
        input += "Password: " + t_password.getEditText().getText().toString();
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();

        mAuth.createUserWithEmailAndPassword(email_input,passwordInput)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                           User user = new User(email_input,name,passwordInput);
                           FirebaseDatabase.getInstance().getReference("Users").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),
                                                "user Registered",Toast.LENGTH_LONG).show();
                                        Log.i("register","user register");
                                        signupprog.setVisibility(View.GONE);
                                    }else {
                                        Toast.makeText(getApplicationContext(),
                                                "denied",Toast.LENGTH_LONG).show();
                                        Log.i("register","user not register");
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),"Task Uncessgul",Toast.LENGTH_LONG).show();
                            Log.i("register","unsuccessufl task");
                        }
                    }
                });

    }

    public boolean validateEmail(){

        email_input = t_mob_mail.getEditText().getText().toString();

        if(email_input.isEmpty()){
            t_mob_mail.setError("Field can't be empty");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email_input).matches()) {
            t_mob_mail.setError("Enter Valid mail");
            return false;
        }else{
            t_mob_mail.setError(null);
            return true;
        }

    }
    private boolean validateUsername(){

        name = t_name.getEditText().getText().toString();

        if (name.isEmpty()) {
            t_name.setError("Field can't be empty");
            return false;
        } else if (name.length() > 15) {
            t_name.setError("Username too long");
            return false;
        } else {
            t_name.setError(null);
            return true;
        }
    }


    private boolean validatePassword() {
        passwordInput = t_password.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            t_password.setError("Field can't be empty");
            return false;
        } else {
            t_password.setError(null);
            return true;
        }
    }

    public void submitlogin(View view){

        Log.i("loginSubmit","Loginsubmitted");
        loginprog.setVisibility(View.VISIBLE);
        if (!valid_password_login() | !valid_mail_login() ) {
            return;
        }

        mAuth.signInWithEmailAndPassword(login_email,login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){

                        loginprog.setVisibility(View.GONE);
                        Intent intent = new Intent(SignUPLogin.this, MainActivity.class);
                        startActivity(intent);
                     //   userProfiledata();

                    } else{
                        user.sendEmailVerification();

                        Log.i("loginSubmit","send Varification link");
                    }

                }else{
                    Log.i("loginSubmit","not Successful");
                }
            }
        });
    }

//    private void userProfiledata() {
//
////        User userProfile = snapshot.getValue(User.class);
//
//        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                Log.i("firebase","userinfo");
//
//                User userProfile = snapshot.getValue(User.class);  // .getValue(String.class);
//                //  User userProfile = snapshot.child("Users").getValue(User.class);
//              // Log.i("userprofile",userProfile.toString());
//            // Log.i("userprofile",userProfile.email);
//
//                if(userProfile==null)
//                    Log.i("userprofile","Profile of user is null");
//
//                if(userProfile != null){
//
//                    Toast.makeText(getApplicationContext(),userProfile.name+userProfile.email,Toast.LENGTH_LONG).show();
//
//                    Log.i("userprofile","Profile of user is not null");
//                    Intent intent = new Intent(SignUPLogin.this, MainActivity.class);
//                    startActivity(intent);
//                    Log.i("loginSubmit",userProfile.name+userProfile.email);
//
//
//                }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.i("firebase","firebase error");
//            }
//        });
//
//    }

    public boolean valid_password_login(){

        login_password = t_login_password.getEditText().getText().toString().trim();
        if (login_password.isEmpty()) {
            t_login_password.setError("Field can't be empty");
            return false;
        } else {
            t_login_password.setError(null);
            return true;
        }

    }

    public boolean valid_mail_login(){

        login_email = t_login_mailmob.getEditText().getText().toString();

        if(login_email.isEmpty()){
            t_login_mailmob.setError("Field can't be empty");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(login_email).matches()) {
            t_login_mailmob.setError("Enter Valid mail");
            return false;
        }else{
            t_login_mailmob.setError(null);
            return true;
        }
    }
}
package com.example.amanzondemo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class ProductClass extends AppCompatActivity implements PaymentResultListener {

    TextView productname,productprice,conttext;
    ImageView productimg;
    int coutproduct=1;
    String price;
    Button minus;

    // User Profile data
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private FirebaseAuth mAuth;

    // Payment Mode
    String name;
    String pricepay;
    String email;
    String phoneNumber;


    public void productby(View view){


        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        //Log.i("Valueofuser",user.toString());

        if(user!=null){
            userID = user.getUid();
//            logout.setText("LogOut");
//            logout.setVisibility(View.VISIBLE);
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


            Log.i("firebase", "userinfo");

                    User userProfile = snapshot.getValue(User.class);  // .getValue(String.class);

                    if (userProfile == null)
                        Log.i("userprofile", "Profile of user is null");

                    if (userProfile != null) {

                      email = userProfile.email;

                        VarifyPhoneNumber();
                       // Toast.makeText(getApplicationContext(), userProfile.name + userProfile.email, Toast.LENGTH_LONG).show();

                       // Intent intent = new Intent(ProductClass.this,CheckoutPayment.class);
                      // payment();

                        Log.i("userprofile", userProfile.name);
                        Log.i("userprofile", userProfile.email);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("firebase","firebase error");
                }
            });
        }else{
            Log.i("logusernull","User data is null for now");

            Toast.makeText(getApplicationContext(),"You have to first login for purchase ", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),SignUPLogin.class);

            startActivity(intent);

        }

    }

    private void VarifyPhoneNumber() {

        Dialog dialog = new Dialog(ProductClass.this);
        dialog.setContentView(R.layout.mobile_popup);

        final EditText etPhoneNumber = dialog.findViewById(R.id.etPhoneNumber);
        Button sndotp = dialog.findViewById(R.id.sndotp);
        sndotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 phoneNumber = etPhoneNumber.getText().toString();
                if (phoneNumber.isEmpty())
                    Toast.makeText(ProductClass.this, "Enter your phone number", Toast.LENGTH_SHORT).show();
                else if(phoneNumber.length()>11||phoneNumber.length()<10){
                    Toast.makeText(ProductClass.this, "Your have to type 10 Digit number", Toast.LENGTH_SHORT).show();
                }else {
                    //verify phone number
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91"+phoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            ProductClass.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                    signInUser(phoneAuthCredential);
                                    Toast.makeText(getApplicationContext(),"Sign in User",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onVerificationFailed(FirebaseException e) {

                                   // Log.d(TAG, "onVerificationFailed:"+e.getLocalizedMessage());
                                    Toast.makeText(getApplicationContext(),"Varification Failed",Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCodeSent(final String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(verificationId, forceResendingToken);
                                    //
                                    Dialog dialog = new Dialog(ProductClass.this);
                                    dialog.setContentView(R.layout.verify_popup);

                                    final EditText etVerifyCode = dialog.findViewById(R.id.etVerifyCode);
                                    Button btnVerifyCode = dialog.findViewById(R.id.btnVerifyOTP);
                                    btnVerifyCode.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String verificationCode = etVerifyCode.getText().toString();
                                            if(verificationId.isEmpty()) return;
                                            //create a credential
                                            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,verificationCode);
                                            signInUser(credential);
                                        }
                                    });

                                    dialog.show();
                                }
                            });
                }

            }
        });
        dialog.show();
    }
public void signInUser(PhoneAuthCredential credential) {
    FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                       // startActivity(new Intent(MainActivity.this,HomePage.class));
                        payment();
                        Toast.makeText(getApplicationContext(),"Is Successfull",Toast.LENGTH_LONG).show();
                        finish();
                    }else {

                        //Log.d(TAG, "onComplete:"+task.getException().getLocalizedMessage());

                        Toast.makeText(getApplicationContext(),"Not Successfull",Toast.LENGTH_LONG).show();

                    }
                }
            });
}
    public void plus(View view){

        coutproduct++;
        conttext.setText(Integer.toString(coutproduct));
        minus.setEnabled(true);
    }

    public void minus(View view) {
        if (coutproduct <= 1|| coutproduct == 1) {
            minus.setEnabled(false);
      Toast.makeText(getApplicationContext(),"You can only choose in Positive numbers",Toast.LENGTH_LONG).show();
        } else {
            minus.setEnabled(true);
            coutproduct--;
            conttext.setText(Integer.toString(coutproduct));
            if(coutproduct == 1)
                minus.setEnabled(false);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_class);

        Bundle extras = getIntent().getExtras();
         price =  extras.getString("price");
        String name =  extras.getString("name");
        String image =  extras.getString("image");

        productname  = findViewById(R.id.productname);    // productprice
        productprice  = findViewById(R.id.productprice);
        productimg = findViewById(R.id.productimg);
        conttext = findViewById(R.id.conttext);
        minus = findViewById(R.id.minus);


        productname.setText("Product "+name);
        productprice.setText("Price "+price);

        Picasso.get().load(image).into(productimg);

        if(coutproduct == 1)
            minus.setEnabled(false);
    }

    public  void payment(){

        if(name==null)
            name = "Manish";

        if(price==null)
          price = "100";

        if(email==null)
           email = "dummymail@gmail.com";

        if(phoneNumber==null)
            phoneNumber = "9878979898";

        int amount = Math.round(Float.parseFloat(price)*100);;
        // Initialeze razorpay checkout
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_V0OgCcq2zBoxTc");
        checkout.setImage(R.drawable.addtocard);

        JSONObject object = new JSONObject();

        try {
            // PUt name
            object.put(name,"Android Coding");

            object.put("description", "Test Payment");

            object.put("theme.color","#0093d0");

            object.put("currency","INR");

            object.put("amout",amount);

            object.put("prefill.contact",phoneNumber);

            object.put("prefill.email",email);

            checkout.open(ProductClass.this, object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        //Ininialize Alert boz
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment ID");

        builder.setMessage(s);
        builder.show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
}
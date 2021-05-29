package com.example.amanzondemo1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutPayment extends AppCompatActivity implements PaymentResultListener {

    String name;
    String price;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_payment);

        Bundle extras = getIntent().getExtras();
         price =  extras.getString("price");
         name =  extras.getString("name");
         email =  extras.getString("email");

//        float amoutnt =  Integer.parseInt(price);
        payment();

    }

    public  void payment(){

        String sAmout = "100";

        int amount = Math.round(Float.parseFloat(price)*100);;//Math.round(Float.parseFloat(sAmout)*100);
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

            object.put("prefill.contact","6266643412");

            object.put("prefill.email",email);

            checkout.open(CheckoutPayment.this, object);

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
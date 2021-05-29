package com.example.amanzondemo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.amanzondemo1.Adapter.DealsAdapter;
import com.example.amanzondemo1.Adapter.HoAdaper;
import com.example.amanzondemo1.Adapter.SliderAdapter;
import com.example.amanzondemo1.Model.DealsModel;
import com.example.amanzondemo1.Model.HoModel;
import com.example.amanzondemo1.Model.SliderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView drawer;
    Button singinbutton;

    // List View Horizontal
    RecyclerView horizontalview;
    RecyclerView.LayoutManager layoutManager;
    HoAdaper hoAdaper;
    ArrayList<HoModel> holist;
    HoModel hoModel;

    // Deals
     RecyclerView dealsview;
     RecyclerView.LayoutManager layoutManagerdeals;
     DealsAdapter dealsAdapter;
     ArrayList<DealsModel> dealslist;
     DealsModel dealsModel;

    //Slider
    List<SliderModel> slidelist = new ArrayList<>();
//    private List<SliderModel> list_dataList;
RequestQueue rq;
    List<SliderModel> sliderImg;
    SliderAdapter sliderAdapter;

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    // List Deals
    ImageView img1,img2,img3,img4;
    ProgressBar progressBarho,progressslider;

    // Signinlogin
    RelativeLayout signinlay;
    TextView logout;
    // User Profile data
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private FirebaseAuth mAuth;
   // TextView textvi;
   TextView textvi;
   public void home(View view){
      //  Log.i("navigation","Close Drawer");
        drawerLayout.closeDrawer(Gravity.LEFT);
       // Toast.makeText(getApplicationContext(),"Home hello",Toast.LENGTH_LONG).show();
    }

    public void shop(View view){
        drawerLayout.closeDrawer(Gravity.LEFT);
      //  Toast.makeText(getApplicationContext(),"Home Shop",Toast.LENGTH_LONG).show();
    }


    public void signin(View view){

      // Log.i("signin","signupbuttonpressed");

       Intent intent = new Intent(MainActivity.this, SignUPLogin.class);
       startActivity(intent);

   }
   // RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.navbarlay);  //findViewByid(R.id.navbarlay);

    public void amzonpay(View view){

       Intent intent = new Intent(getApplicationContext(),CheckoutPayment.class);
       startActivity(intent);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        RelativeLayout placeHolder = (RelativeLayout) findViewById(R.id.navbarlay);
        getLayoutInflater().inflate(R.layout.nav_header, placeHolder);
//        textvi = (TextView) placeHolder.findViewById(R.id.textvi);

       //  findViewById(R.id.textvi);



        progressBarho = findViewById(R.id.progressbar);
        progressslider = findViewById(R.id.progressslider);

        progressslider.setVisibility(View.VISIBLE);
        progressBarho.setVisibility(View.VISIBLE);


        getLoginData();
        setUpToolbar();
        horizonallist();
        ImageSliderView();
        dealsoftheday();



    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent)
//    {
//        // initialize, inflate convertView from your layout if needed;
//      //  tvDisplayDate = (EditText)convertView.findViewById(R.id.editd);
//        // populate convertView with the item's details
//    }

    private void getLoginData() {
          signinlay = (RelativeLayout) findViewById(R.id.singin);
        textvi = (TextView)findViewById(R.id.textvi);
        logout = findViewById(R.id.logout);



        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
       //Log.i("Valueofuser",user.toString());

        if(user!=null){
            userID = user.getUid();
            logout.setText("LogOut");
            logout.setVisibility(View.VISIBLE);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


             // if(signinlay!=null)
                signinlay.setVisibility(View.INVISIBLE);

               // Log.i("firebase", "userinfo");

                User userProfile = snapshot.getValue(User.class);  // .getValue(String.class);
                //  User userProfile = snapshot.child("Users").getValue(User.class);
                // Log.i("userprofile",userProfile.toString());
                // Log.i("userprofile",userProfile.email);

                if (userProfile == null)
                    Log.i("userprofile", "Profile of user is null");

                if (userProfile != null) {

                    Toast.makeText(getApplicationContext(), userProfile.name + userProfile.email, Toast.LENGTH_LONG).show();

                    textvi.setText(userProfile.name);

                  //  Log.i("userprofile", userProfile.name);
                 //   Log.i("userprofile", userProfile.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Log.i("firebase","firebase error");
            }
        });
        }else{
//            Log.i("logusernull","User data is null for now");
           // textvi.setText("Hello SignIn,");
           if(signinlay!=null)
           signinlay.setVisibility(View.VISIBLE);
            logout.setVisibility(View.INVISIBLE);
            logout.setText("Logged In");
            textvi.setText("Hello, SignIn");
        }

    }


    private void dealsoftheday() {

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);

        dealsview = findViewById(R.id.dealsview);
        dealsview.hasFixedSize();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        dealsview.setLayoutManager(layoutManager);

        dealsAdapter = new DealsAdapter(MainActivity.this);
        dealsview.setAdapter(dealsAdapter);

        dealslist = new ArrayList<DealsModel>();


        loadDeals();

        //DealsAdapter dealsAdapter1 = new DealsAdapter(getApplicationContext());

    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(),"You are logged out",Toast.LENGTH_LONG).show();
        drawerLayout.closeDrawer(Gravity.LEFT);
        getLoginData();
    }

    private void loadDeals() {

        Toast.makeText(getApplicationContext(),"Toast generated",Toast.LENGTH_SHORT).show();
        //progressbar.setVisibility(View.GONE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://api.jsonbin.io/b/6048668300e5956cd8895d34", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i <response.length() ; i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        dealsModel = new DealsModel();

                        String product = jsonObject.getString("product");
                        String image = jsonObject.getString("image");
                        String priceh = jsonObject.getString("priceh");
                        String pricel = jsonObject.getString("pricel");
                        String ends = jsonObject.getString("ends");

                        dealsModel.setProduct(product);
                        dealsModel.setImage(image);
                        dealsModel.setEnds(ends);
                        dealsModel.setPriceh(priceh);
                        dealsModel.setPricel(pricel);

                        dealslist.add(dealsModel);

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    dealsAdapter.setData(dealslist);
                    dealsAdapter.notifyDataSetChanged();

                }
                Picasso.get().load(dealslist.get(0).getImage()).into(img1);
                Picasso.get().load(dealslist.get(1).getImage()).into(img2);
                Picasso.get().load(dealslist.get(2).getImage()).into(img3);
                Picasso.get().load(dealslist.get(3).getImage()).into(img4);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.i("erroroccured",error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void loadJsonhorizontal() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://api.jsonbin.io/b/604858d3683e7e079c4ab3c9", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i <response.length() ; i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);


//                        Log.i("jsonobjecthorizontal", jsonObject.getString("name"));
//                        Log.i("jsonobject",jsonObject.getString("image"));

                        String name = jsonObject.getString("name");
                        String image = jsonObject.getString("image");

                        hoModel = new HoModel();
                        hoModel.setName(name);
                        hoModel.setImage(image);
                        holist.add(hoModel);

//                       Log.i("holistdata",hoModel.getName());

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    hoAdaper.setData(holist);
                    hoAdaper.notifyDataSetChanged();
                    progressBarho.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.i("erroroccured",error.toString());
//                progressbar.setVisibility(View.VISIBLE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void ImageSliderView() {


        rq = CustomVolleyRequest.getInstance(this).getRequestQueue();

        sliderImg = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        sendRequest();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){

                    dots[i].setImageResource(R.drawable.inactive);

                }

                dots[position].setImageResource(R.drawable.active);

            }

            @Override
            public void onPageScrollStateChanged(int state) { }

        });

        progressslider.setVisibility(View.GONE);
    }

    private void sendRequest() {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://api.jsonbin.io/b/604832c800e5956cd88932b5/2", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++){

                    SliderModel sliderModel = new SliderModel();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        sliderModel.setImage(jsonObject.getString("image"));
                        sliderModel.setName(jsonObject.getString("name"));

                        Log.i("imageurl",jsonObject.getString("image"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sliderImg.add(sliderModel);
                }

                sliderAdapter = new SliderAdapter(sliderImg, MainActivity.this);
                viewPager.setAdapter(sliderAdapter);

                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new SliderTimer(), 2000, 3000);

                dotscount = sliderAdapter.getCount();
                dots = new ImageView[dotscount];

                for(int i = 0; i < dotscount; i++){

                    dots[i] = new ImageView(MainActivity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.inactive));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanel.addView(dots[i], params);

                }
                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        CustomVolleyRequest.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    private void horizonallist() {

        horizontalview = findViewById(R.id.horizontalview);
        horizontalview.hasFixedSize();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        horizontalview.setLayoutManager(layoutManager);


        hoAdaper = new HoAdaper();
        horizontalview.setAdapter(hoAdaper);


        holist = new ArrayList<HoModel>();


        loadJsonhorizontal();
        
    }



    private void setUpToolbar() {

        drawerLayout = findViewById(R.id.drawer);
        drawer = findViewById(R.id.drawermenu);

        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.app_name,R.string.app_name);


        actionBarDrawerToggle.syncState();

    }

    class SliderTimer extends TimerTask {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() <sliderImg.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

}
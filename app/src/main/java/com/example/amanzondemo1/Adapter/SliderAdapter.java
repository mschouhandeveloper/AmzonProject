package com.example.amanzondemo1.Adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.amanzondemo1.CustomVolleyRequest;
import com.example.amanzondemo1.Model.SliderModel;
import com.example.amanzondemo1.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<SliderModel> sliderImg;
    private ImageLoader imageLoader;
    SliderModel sliderModel;


    public SliderAdapter(List sliderImg,Context context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }


    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sliderimage, null);

        SliderModel utils = sliderImg.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);

         imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
         //imageLoader.get(utils.getImage(), ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        Glide.with(context)
                .load(utils.getImage())
                .apply(new RequestOptions().placeholder(R.drawable.amazon).error(R.drawable.amazon))
                .into(imageView);

        Log.i("imageview",utils.getImage());
        Log.i("slidertextview",utils.getName());
       //textView.setText(utils.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}





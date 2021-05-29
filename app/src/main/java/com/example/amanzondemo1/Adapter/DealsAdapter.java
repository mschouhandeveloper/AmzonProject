package com.example.amanzondemo1.Adapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amanzondemo1.CheckoutPayment;
import com.example.amanzondemo1.MainActivity;
import com.example.amanzondemo1.Model.DealsModel;
import com.example.amanzondemo1.ProductClass;
import com.example.amanzondemo1.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder>  {

    ArrayList<DealsModel> hoLista; // = new <HoModel>ArrayList();
    Context activity;

    public DealsAdapter(Context context) {
        activity = context;
        hoLista = new ArrayList<>();

    }
    public void setData(ArrayList<DealsModel> holist) {
        this.hoLista = holist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.deal_image, viewGroup, false);

//       v.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               Log.i("ItemClicked","Value of i = "+Integer.toString(i));
//               //Intent intent = new Intent(this, CheckoutPayment.class);
//
//               Intent intent = new Intent(activity,ProductClass.class);
//               intent.putExtra("valueitemclic", String.valueOf(i));
//
//               v.getContext().startActivity(intent);
//           }
//       });
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Log.i("dealsadapter",hoLista.get(i).getProduct());
        Log.i("dealsadapter",hoLista.toString());


         //viewHolder.product.setText("hello");
        viewHolder.product.setText(hoLista.get(i).getProduct());

        Picasso.get().load(hoLista.get(i).getImage()).into(viewHolder.imgThumbnail);

        String priceh = hoLista.get(i).getPriceh() ,pricel = hoLista.get(i).getPricel();
        String findalPrice = priceh +" - "+ pricel;
        viewHolder.priceh.setText(findalPrice);
        viewHolder.ends.setText(hoLista.get(i).getEnds());

        viewHolder.layout2.setOnClickListener(v -> {

                Intent intent = new Intent(activity,ProductClass.class);
                 intent.putExtra("price", hoLista.get(i).getPriceh());
                 intent.putExtra("name", hoLista.get(i).getProduct());
                  intent.putExtra("image", hoLista.get(i).getImage());

               v.getContext().startActivity(intent);

        });

    }


    @Override
    public int getItemCount() {
        return hoLista.size();
    }

//    @Override
//    public void onClick(View v) {
//
////        Intent intent = new Intent(activity, CheckoutPayment.class);
//
////        getAdapterPosition();
////               Intent intent = new Intent(activity,ProductClass.class);
////           //    intent.putExtra("valueitemclic", String.valueOf);
//
//
//    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

       final ImageView imgThumbnail;
       final TextView product,priceh,ends;
       RelativeLayout layout2;

        ViewHolder(View itemView) {
            super(itemView);

            imgThumbnail = itemView.findViewById(R.id.imagedeals);
            product =      itemView.findViewById(R.id.productdealstxt);
            priceh =       itemView.findViewById(R.id.price);
            ends =         itemView.findViewById(R.id.ends);
            layout2 = itemView.findViewById(R.id.layout2);


        }


    }
}

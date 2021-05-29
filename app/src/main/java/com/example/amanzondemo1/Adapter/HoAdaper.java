package com.example.amanzondemo1.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amanzondemo1.MainActivity;
import com.example.amanzondemo1.Model.HoModel;
import com.example.amanzondemo1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HoAdaper extends RecyclerView.Adapter<HoAdaper.ViewHolder>{


    ArrayList<HoModel> hoLista ; // = new <HoModel>ArrayList();

//    ImageView hoimage1 = (ImageView) findViewById(R.id.hoimage);


    public HoAdaper() {
        hoLista = new <HoModel>ArrayList();
    }

    public void setData(ArrayList<HoModel> hoList1){
        this.hoLista = hoList1;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.horizontalist, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.textView.setText(hoLista.get(i).getName());
        Picasso.get().load(hoLista.get(i).getImage()).into(viewHolder.imgThumbnail);


        System.out.println("Name:-  "+hoLista.get(i).getName());
        System.out.println("Name:-  "+hoLista.get(i).getImage());

        Log.i("adaptertextview",hoLista.get(i).getName());
        Log.i("adapteimageview",hoLista.get(i).getImage());
        Log.i("demomessage","Demo message");

    }

    @Override
    public int getItemCount() {
        return hoLista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView imgThumbnail;
        TextView textView;
      //  private ItemClickListener clickListener;

        ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.hoimage);
            textView = itemView.findViewById(R.id.hotxt);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
        }



    }
}

package com.example.masterchef_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.masterchef_app.OBJs.Category;
import com.example.masterchef_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {
    ArrayList<Category> catrgoryList  = new ArrayList<>();
Context context ;
int resurceId ;
    public CategorySpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resurceId = resource;
    }

    public void setCatrgoryList(ArrayList<Category> catrgoryList) {
        this.catrgoryList = catrgoryList;
        notifyDataSetChanged();
    }

    public Category getCategoryByIndex(int index){
        return catrgoryList.get(index);
    }

    @Override
    public int getCount() {
        return catrgoryList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try{
            if (view == null){
                // inflate View
                view = LayoutInflater.from(context).inflate(resurceId,parent,false);
                Category categoryFood= catrgoryList.get(position);
                if(categoryFood != null){
                    TextView categoryTitle= view.findViewById(R.id.spinner_categroy_title);
                    categoryTitle .setText(categoryFood.getCategoryName());
                }}
        }catch(Exception e){
            Toast.makeText(context, "Ther Is A Wrong ", Toast.LENGTH_SHORT).show();
        }
        return view ;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try{
            if (view == null){
                view = LayoutInflater.from(context).inflate(resurceId,parent,false);
                Category categoryFood= catrgoryList.get(position);
                if(categoryFood != null){
                    TextView categoryTitle= view.findViewById(R.id.spinner_categroy_title);
                    categoryTitle .setText(categoryFood.getCategoryName());
                }}
        }catch(Exception e){
            Toast.makeText(context, "Ther Is A Wrong ", Toast.LENGTH_SHORT).show();
        }
        return view ;
    }
}

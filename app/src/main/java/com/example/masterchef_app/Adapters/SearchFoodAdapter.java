package com.example.masterchef_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masterchef_app.UserActivities.Details_Activity;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchFoodAdapter extends RecyclerView.Adapter<SearchFoodAdapter.SearchingFoodViewHolder> {
ArrayList<Food> foodList = new ArrayList<>();

    public SearchFoodAdapter() {
    }

    public void setFoodList(ArrayList<Food> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchingFoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SearchingFoodViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_food_view_holder,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchingFoodViewHolder searchingFoodViewHolder, int i) {
Food food = foodList.get(i);
        searchingFoodViewHolder.bind(food.getTitle(),food.getPic(),food.getPrice());
        searchingFoodViewHolder.show_detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchingFoodViewHolder.itemContext, Details_Activity.class);
                intent.putExtra("FoodDetails",food);
                searchingFoodViewHolder.itemContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class SearchingFoodViewHolder extends RecyclerView.ViewHolder{
ImageView foodImg ;
TextView foodTitle , foodPrice ;
ImageButton show_detailsBtn ;
Context itemContext;
        public SearchingFoodViewHolder(@NonNull View itemView) {
            super(itemView);
        foodImg = itemView.findViewById(R.id.search_food_pic_VH);
            foodTitle = itemView.findViewById(R.id.search_food_title_VH);
            foodPrice = itemView.findViewById(R.id.search_food_price_VH);
            show_detailsBtn = itemView.findViewById(R.id.search_food_showdetails_VH);
itemContext = itemView.getContext();
        }
        public  void  bind (String title , String pic , String price){
            Picasso.get().load(pic).placeholder(R.drawable.empty_category_icon).into(foodImg);
            foodTitle.setText(title);
            foodPrice.setText(price+ " " +"$");
        }
    }
}

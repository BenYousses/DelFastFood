package com.example.masterchef_app.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masterchef_app.AppControlingActivities.Add_New_Food_Activity;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditFoodAdapter extends RecyclerView.Adapter<EditFoodAdapter.EditFoodAdapterViewHolder> {
private ArrayList<Food> foodsList = new ArrayList<>();
// the foods Reference in RealTime Database for access to All Foods under this path
private DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("Foods");

    public EditFoodAdapter() {
    }

    public void setFoodsList(ArrayList<Food> foodsList) {
        this.foodsList = foodsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EditFoodAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EditFoodAdapterViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_food_view_holder,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EditFoodAdapterViewHolder editFoodAdapterViewHolder, int i) {
Food food = foodsList.get(i);
editFoodAdapterViewHolder.bind(food.getPic(),food.getTitle());
// delete Food Operation
        editFoodAdapterViewHolder.editDeleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete food by key
                foodRef.child(food.getFoodKey()).removeValue();
            }
        });
        // upDate Food Operation
        editFoodAdapterViewHolder.editUpDateFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send Food by Intent  for UpDate Food
                Intent intent = new Intent(v.getContext() , Add_New_Food_Activity.class);
                intent.putExtra("editFood",food);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }

    class EditFoodAdapterViewHolder extends RecyclerView.ViewHolder{
ImageView editFoodImage;
ImageButton editDeleteFood,editUpDateFood;
TextView editFoodTitle;
        public EditFoodAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            editFoodImage = itemView.findViewById(R.id.edit_food_pic);
            editFoodTitle = itemView.findViewById(R.id.edit_food_title);
            editDeleteFood = itemView.findViewById(R.id.delete_editFood_btn);
            editUpDateFood = itemView.findViewById(R.id.update_editFood_btn);
        }
        public void bind (String foodPic , String foodTitle){
            Picasso.get().load(foodPic).placeholder(R.drawable.empty_category_icon).into(editFoodImage);
            editFoodTitle.setText(foodTitle);

        }
    }
}

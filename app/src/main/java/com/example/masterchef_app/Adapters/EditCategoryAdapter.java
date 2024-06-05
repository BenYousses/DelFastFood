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

import com.example.masterchef_app.AppControlingActivities.Add_New_Category;
import com.example.masterchef_app.OBJs.Category;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class EditCategoryAdapter extends RecyclerView.Adapter<EditCategoryAdapter.editCategoryViewHolder> {
ArrayList<Category> categoryList = new ArrayList<>();
// inflate the FirebaseDatabase
    FirebaseDatabase maseterChefDataBase = FirebaseDatabase.getInstance();
    // get Refrence of Categories in RealtimeDatabase for access to Categories
    DatabaseReference categoryRef = maseterChefDataBase.getReference("Categories");
    // get Refrence of Food in RealtimeDatabase for access to Foods
    DatabaseReference foodRef = maseterChefDataBase.getReference("Foods");
//Constructer
    public EditCategoryAdapter() {
    }

    public void setCategoryList(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public editCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new editCategoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_category_view_holder,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull editCategoryViewHolder editCategoryViewHolder, int i) {
Category category = categoryList.get(i);

editCategoryViewHolder.bind(category.getCategoryImage(),category.getCategoryName());
// delate Category Operation
        editCategoryViewHolder.delete_category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove the Category in RealTime by key
                categoryRef.child(category.getCvategoryKey()).removeValue();
// remove All of the Foods have relate with  this Category
                foodRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot foodDataSnapShot : dataSnapshot.getChildren()){
                            Food food = foodDataSnapShot.getValue(Food.class);
                            if (food.getCategoryKey().equals(category.getCvategoryKey())){
                                // remove Food by FoodKey
                                foodRef.child(food.getFoodKey()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
// UpDate Category Operation
 editCategoryViewHolder.update_category_btn.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         // sent The category by Intent to " Category.class" for update it
         Intent intent = new Intent(v.getContext() , Add_New_Category.class);
         intent.putExtra("editCategory",category);
         v.getContext().startActivity(intent);
     }
 });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class editCategoryViewHolder extends RecyclerView.ViewHolder{
TextView edit_category_title;
ImageButton delete_category_btn,update_category_btn;
ImageView edit_category_pic ;
        public editCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            edit_category_title = itemView.findViewById(R.id.edit_category_title);
            delete_category_btn = itemView.findViewById(R.id.delete_editCategory_btn);
            update_category_btn = itemView.findViewById(R.id.update_editcategory_btn);
            edit_category_pic = itemView.findViewById(R.id.edit_category_pic);
        }
        public void bind (String catPic , String catName){
            edit_category_title.setText(catName);
            Picasso.get().load(catPic).placeholder(R.drawable.empty_category_icon).into(edit_category_pic);
        }
    }
}

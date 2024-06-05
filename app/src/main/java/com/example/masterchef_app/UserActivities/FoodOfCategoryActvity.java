package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.masterchef_app.Adapters.FoodAdapter;
import com.example.masterchef_app.OBJs.Category;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.databinding.ActivityFoodOfCategoryActvityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodOfCategoryActvity extends AppCompatActivity {
ActivityFoodOfCategoryActvityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
binding = ActivityFoodOfCategoryActvityBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        relatedFodRVSetting();
        onBtnBackPressed();

    }

    private void onBtnBackPressed() {
        binding.catFoodBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void relatedFodRVSetting() {
        Intent i = getIntent();
        MainActivity.foodAdapter = new FoodAdapter(1);
        Category category = (Category) i.getSerializableExtra("CtegoryClicked");
        binding.cateFoodTitle.setText(category.getCategoryName());
        Picasso.get().load(category.getCategoryImage()).into(binding.catFoodImg);
MainActivity.foodRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        ArrayList<Food> relatedFoodList = new ArrayList<>();
        int counter = 0;
        for (DataSnapshot relatedFoodDataSnapshot :dataSnapshot.getChildren() ){
            Food food1 = relatedFoodDataSnapshot.getValue(Food.class);
            if ( category.getCvategoryKey().equals(food1.getCategoryKey())){
                relatedFoodList.add(food1);
                counter++;
            }
        }
        if (relatedFoodList .size() > 0){
            MainActivity.foodAdapter.setFoodList(relatedFoodList);
            binding.LoadingRelatedCategory.setVisibility(View.GONE);
        }else{
            binding.LoadingRelatedCategory.setVisibility(View.VISIBLE);
            binding.LoadingRelatedCategory.setText("<No Related Food>");
        }




        binding.NumberOfRelatedFood.setText(String.valueOf(counter)+" " + "RelatedFood");
   }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
});

        binding.rerlatedFoodRV.setAdapter(MainActivity.foodAdapter );
        binding.rerlatedFoodRV.setHasFixedSize(true);
        binding.rerlatedFoodRV.setLayoutManager(new GridLayoutManager(getBaseContext(),2));

    }
}
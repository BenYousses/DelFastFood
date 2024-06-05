package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.masterchef_app.Adapters.FoodAdapter;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.databinding.ActivityMenuBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Menu_Activity extends AppCompatActivity {
ActivityMenuBinding binding ;
FoodAdapter foodAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getFoodDataInDb();
        onBtnBackPressed();
    }

    private void onBtnBackPressed() {
        binding.menuBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getFoodDataInDb() {
foodAdapter = new FoodAdapter(2);
        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("Foods");
        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Food> foodList = new ArrayList<>();
            for(DataSnapshot foodDataSnapShot : dataSnapshot.getChildren()){
                Food food = foodDataSnapShot.getValue(Food.class);
                if (food != null){
                    foodList.add(food);
                }
            }
            if (foodList.size()  > 0){
                foodAdapter.setFoodList(foodList);
                binding.LoadingFoodMenu.setVisibility(View.GONE);
            }else{
                binding.LoadingFoodMenu.setVisibility(View.VISIBLE);
                binding.LoadingFoodMenu.setText("<No Foods>");
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        menuRvSetUp();

    }

    private void menuRvSetUp() {
        binding.menuFoodRV.setAdapter(foodAdapter);
        binding.menuFoodRV.setLayoutManager(new GridLayoutManager(this,4));
        binding.menuFoodRV.setHasFixedSize(true);
    }
}
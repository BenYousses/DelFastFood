package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.example.masterchef_app.Adapters.SearchFoodAdapter;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.databinding.ActivityFoodSearchingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Food_Searching_Activity extends AppCompatActivity {
ActivityFoodSearchingBinding binding;
    DatabaseReference masterChefRef;
    FirebaseDatabase masterChefDtabase;
    SearchFoodAdapter  searchFoodAdapter ;
    private String searchText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityFoodSearchingBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        masterChefDtabase = FirebaseDatabase.getInstance();
searchinfoodRvSetUp();
        onBtnBackPressed();
    }

    private void onBtnBackPressed() {
        binding.searchBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void searchinfoodRvSetUp() {
        searchFoodAdapter  = new SearchFoodAdapter();
        masterChefRef = masterChefDtabase.getReference("Foods");
binding.searchBar.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        searchText = binding.searchBar.getText().toString();
masterChefRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        ArrayList<Food> foodList = new ArrayList<>();
        if (dataSnapshot.exists()){
            for (DataSnapshot foodDataSnapshot : dataSnapshot.getChildren()){
                Food food = foodDataSnapshot.getValue(Food.class);
                if (food!= null && food.getTitle().contains(searchText) || food.getTitle().contains(searchText.toUpperCase()) || searchText.equalsIgnoreCase(food.getTitle())){
                    foodList.add(food);
                }
            }
            if (!TextUtils.isEmpty(searchText)){
                if (foodList.size()> 0){
                    binding.searchingFood.setVisibility(View.GONE);
                    binding.searchRv.setVisibility(View.VISIBLE);
                }else {
                    binding.searchingFood.setVisibility(View.VISIBLE);
                    binding.searchRv.setVisibility(View.GONE);
                    binding.searchingFood.setText("<No Result>");

                }
                searchFoodAdapter.setFoodList(foodList);
            }else{
                foodList.clear();
                searchFoodAdapter.setFoodList(foodList);
                binding.searchingFood.setVisibility(View.GONE);

            }
        }


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }
});

binding.searchRv.setAdapter(searchFoodAdapter);
binding.searchRv.setLayoutManager(new LinearLayoutManager(this));
binding.searchRv.setHasFixedSize(true);
    }
}
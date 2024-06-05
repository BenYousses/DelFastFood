package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.masterchef_app.OBJs.BunnerModel;
import com.example.masterchef_app.Adapters.CategoryAdapter;
import com.example.masterchef_app.Adapters.FoodAdapter;
import com.example.masterchef_app.AppControlingActivities.Control_Main_Activity;
import com.example.masterchef_app.OBJs.Category;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.R;
import com.example.masterchef_app.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
Food food ;
    private float dX, dY;
   public static DatabaseReference categoryRef , foodRef;

  public static CategoryAdapter categoryAdapter;
    public static FoodAdapter foodAdapter;

    public  static User user ;
  public static   FirebaseDatabase masterChefDtabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
         masterChefDtabase = FirebaseDatabase.getInstance();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.bottom_navigation_container, new ButtomNavigation_Fragment())
                    .commit();
        }


 receiveUser();
 categoryRvSetting();
foodRvSetting();
showMenuBtnSetUp();
        bunnerSetUp();
        onOrderBtnsClicked();
        refreshMainPage();


    }

    private void refreshMainPage() {
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();            }
        });
    }

    private void bunnerSetUp() {
        DatabaseReference bunnerRef = FirebaseDatabase.getInstance().getReference("Bunner");
        bunnerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot bunnerDataSnapShot : dataSnapshot.getChildren()) {
                        BunnerModel bunnerModel = bunnerDataSnapShot.getValue(BunnerModel.class);
                        if (bunnerModel != null) {
                            Picasso.get().load(bunnerModel.getBunnerImg()).into(binding.mainBunnerImage);
                            food  = bunnerModel.getFood();
                            binding.loadingMainBunnerImage.setVisibility(View.GONE);
                        }
                    }
                } else {
binding.loadingMainBunnerImage.setText("<No Bunner Image>");
binding.loadingMainBunnerImage.setVisibility(View.VISIBLE);
;}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.loadingMainBunnerImage.setText("<There is a wrong>");
                binding.loadingMainBunnerImage.setVisibility(View.VISIBLE);
            }
        });
    }


    private void onOrderBtnsClicked() {
        binding.orderBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , Details_Activity.class);
                intent.putExtra("FoodDetails",food);
               startActivity(intent);
            }
        });
    }




    private void showMenuBtnSetUp() {
        binding.seeMoreTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Menu_Activity.class);
                startActivity(intent);
            }
        });
    }


    private void foodRvSetting() {
        foodAdapter = new FoodAdapter(1);
        foodRef = masterChefDtabase.getReference("Foods");
        foodRef.limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Food> foodList = new ArrayList<>();
                for(DataSnapshot foodDataSnapshot : dataSnapshot.getChildren()){
                    Food food = foodDataSnapshot.getValue(Food.class);
                    foodList.add(food);
                }
                if (foodList.size() >0){
                    foodAdapter.setFoodList(foodList);
                    binding.LoadingFoods.setVisibility(View.GONE);
                }else{
                    binding.LoadingFoods.setText("<No Categories>");
                    binding.LoadingFoods.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        binding.newFoodRv.setAdapter(foodAdapter);
        binding.newFoodRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.newFoodRv.setHasFixedSize(true);
    }

    private void categoryRvSetting() {
        categoryRef = masterChefDtabase.getReference("Categories");
        categoryAdapter = new CategoryAdapter();
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Category> categoriesList = new ArrayList<>();
                for (DataSnapshot foodSnapShot : dataSnapshot.getChildren()){
                    Category category = foodSnapShot.getValue(Category.class);
                    if (category != null){
                        categoriesList.add(category);
                    }
                }
                if (categoriesList.size() >0){
                    categoryAdapter.setCategoriesLiset(categoriesList);
                    binding.LoadingCategories.setVisibility(View.GONE);

                }else{
                    binding.LoadingCategories.setVisibility(View.VISIBLE);
                    binding.LoadingCategories.setText("< No Foods >");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "the oeratin is failed", Toast.LENGTH_SHORT).show();
            }
        });
    binding.categoriesRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        binding.categoriesRv.setAdapter(categoryAdapter);
        binding.categoriesRv.setHasFixedSize(true);}
    private void receiveUser() {
        Intent i = getIntent();
         user = (User) i.getSerializableExtra("User");
         if (user != null){
             Picasso.get().load(user.getUserProfile())
                     .into(binding.mainUserProfile);
             binding.HiUserTxt.setText("Hi, " + user.getName()+" "+"!");
         }

    }
    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                receiveUser();
                categoryRvSetting();
                foodRvSetting();
                showMenuBtnSetUp();
                bunnerSetUp();
                onOrderBtnsClicked();
                // Stop the refreshing animation
                binding.swipe.setRefreshing(false);

            }
        }, 2000);
    }
}
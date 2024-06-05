package com.example.masterchef_app.AppControlingActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.masterchef_app.Adapters.EditCategoryAdapter;
import com.example.masterchef_app.Adapters.EditFoodAdapter;
import com.example.masterchef_app.Adapters.OrderAdapter;
import com.example.masterchef_app.Adapters.UserAdapter;
import com.example.masterchef_app.OBJs.BunnerModel;
import com.example.masterchef_app.OBJs.Category;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.OBJs.MessageModel;
import com.example.masterchef_app.OBJs.Order;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.R;
import com.example.masterchef_app.UserActivities.MainActivity;
import com.example.masterchef_app.UserActivities.UserListActivity;
import com.example.masterchef_app.databinding.ActivityControlMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Control_Main_Activity extends AppCompatActivity {
    ActivityControlMainBinding binding ;
    EditCategoryAdapter editCategoryAdapter;
    EditFoodAdapter editFoodAdapter ;
boolean toolsCardIsShowed = false ;
  private FirebaseAuth mAuth = FirebaseAuth.getInstance();

   private FirebaseUser currentUser = mAuth.getCurrentUser();
   private  DatabaseReference newMessageRef = FirebaseDatabase.getInstance().getReference("Chats").child("NewMessages");


    Handler handler = new Handler();
Runnable runnable ;
UserAdapter userAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityControlMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
            onControlBtnClicked();
            onAddNewCategoryBtnClicked();
            onAddNewFoodBtnClicked();
            onAddNewBunnerBtnClicked();
            usersControlRv();
            bunnerSetUp();
            cetegoryRvSetUp();
            foodRvSetUp();
            getOrders();
            onUserwMessagesClicked();
            newMessagesNumbers ();
            onOrderNotificationBtnCliked();


    }
    private void getOrders() {

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;
                if (dataSnapshot.exists()){
                    for (DataSnapshot ordersDataSnapshot : dataSnapshot.getChildren()){
                        Order order = ordersDataSnapshot.getValue(Order.class);
                        if (order != null){
                            counter++;
                        }
                    }
                    if (counter > 0){
                        binding.oredesNumber.setText(String.valueOf(counter));
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onOrderNotificationBtnCliked() {
        binding.orderNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Orders_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void newMessagesNumbers() {
        newMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter =0 ;
                if (dataSnapshot.exists()){
                    for (DataSnapshot newMessageDataSnapshot : dataSnapshot.getChildren()){
                        MessageModel newMessage = newMessageDataSnapshot.getValue(MessageModel.class);
                        if ( newMessage != null &&newMessage.getRecieverKey().equals(currentUser.getUid())){
                            counter ++ ;
                        }
                    }
                    if (counter > 0){
                        binding.numberMessageText.setVisibility(View.VISIBLE);
                        binding.numberMessageText.setText(String.valueOf(counter));
                    }else {
                        binding.numberMessageText.setVisibility(View.GONE);
                    }
                }else {
                    binding.numberMessageText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void onUserwMessagesClicked() {
        binding.userMessagesRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getBaseContext(), UserListActivity.class);
                startActivity(intent);
            }
        });    }

    private void cetegoryRvSetUp() {
        editCategoryAdapter = new EditCategoryAdapter();
        DatabaseReference categoryRf =FirebaseDatabase.getInstance().getReference("Categories");
        categoryRf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Category> categoryList= new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot categoryDataSnapShot : dataSnapshot.getChildren()){
                        Category category  = categoryDataSnapShot.getValue(Category.class);
                        categoryList.add(category);
                    }
                    editCategoryAdapter.setCategoryList(categoryList);
                    binding.controlLoadingCategories.setVisibility(View.GONE);
                    RvsSetting(editCategoryAdapter , binding.controlCategoriesRv);
                }else{
                    binding.controlLoadingCategories.setVisibility(View.VISIBLE);
                    binding.controlLoadingCategories.setText("<No Categories>");
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.controlLoadingCategories.setVisibility(View.VISIBLE);
                binding.controlLoadingCategories.setText("there is a wrong");
            }
        });
    }
    private void foodRvSetUp() {
        editFoodAdapter = new EditFoodAdapter();
        DatabaseReference foodRf =FirebaseDatabase.getInstance().getReference("Foods");
        foodRf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Food> foodList= new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot categoryDataSnapShot : dataSnapshot.getChildren()){
                        Food food  = categoryDataSnapShot.getValue(Food.class);
                        foodList.add(food);
                    }
                    editFoodAdapter.setFoodsList(foodList);
                    binding.controlLoadingFoods.setVisibility(View.GONE);
                    RvsSetting(editFoodAdapter , binding.controlFoodRv);
                }else{
                    binding.controlLoadingFoods.setVisibility(View.VISIBLE);
                    binding.controlLoadingFoods.setText("<No Foods>");
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.controlLoadingFoods.setVisibility(View.VISIBLE);
                binding.controlLoadingFoods.setText("there is a wrong");
            }
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
                            Picasso.get().load(bunnerModel.getBunnerImg()).into(binding.contronlImageBunner);
                            binding.loadingControlmainBunnerImage.setVisibility(View.GONE);                }
                    }

                } else {
                    binding.loadingControlmainBunnerImage.setText("<No Bunner Image>");
                    binding.loadingControlmainBunnerImage.setVisibility(View.VISIBLE);                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.loadingControlmainBunnerImage.setText("<There is a wrong>");
                binding.loadingControlmainBunnerImage.setVisibility(View.VISIBLE);               }
        });
    }

    private void onAddNewBunnerBtnClicked() {
        binding.goToAddNewBunnerBts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , Add_New_Bunner.class);
                startActivity(intent);
            }
        });
    }

    private void usersControlRv() {
        userAdapter = new UserAdapter(2);
        getAllUsers();
        binding.controlUsersRv.setAdapter(userAdapter);
        binding.controlUsersRv.setHasFixedSize(true);
        binding.controlUsersRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }
    private void getAllUsers() {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("MasterChefDataBase").child("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ArrayList<User> usersList = new ArrayList<>();
                    for (DataSnapshot usersDatasnapshot : dataSnapshot.getChildren()){
                        User user = usersDatasnapshot.getValue(User.class);
                        if(user != null){
                            usersList.add(user);
                        }
                    }
                    if (usersList.size() > 0){
                        userAdapter.setUsersList(usersList);
                        binding.controlLoadingUsers.setVisibility(View.GONE);
                    }else {
                        binding.controlLoadingUsers.setText("<No Users>");
                        binding.controlLoadingUsers.setVisibility(View.VISIBLE);
                    }

                }else {
                    binding.controlLoadingUsers.setText("<No Users>");
                    binding.controlLoadingUsers.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void onAddNewFoodBtnClicked() {
        binding.goToAddNewFoodBts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Add_New_Food_Activity.class);
                startActivity(intent);
                hideToolsCard();

            }
        });
    }
    private void onAddNewCategoryBtnClicked() {
        binding.goToAddNewCategoryBts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Add_New_Category.class);
                startActivity(intent);
                hideToolsCard();
            }
        });

    }

    private void onControlBtnClicked() {
        binding.controlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toolsCardIsShowed){

                    showToolsCard();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            hideToolsCard();
                        }
                    };
                    handler.postDelayed(runnable , 3*1000);
                }else{
                    hideToolsCard();
                }
            }
        });
    }
    private void hideToolsCard() {
        binding.toolsCard.setVisibility(View.GONE);
        binding.controlBtn.setImageDrawable(getResources().getDrawable(R.drawable.white_add_icon));
        toolsCardIsShowed = false ;
    }

    private void showToolsCard() {
        binding.toolsCard.setVisibility(View.VISIBLE);
        binding.controlBtn.setImageDrawable(getResources().getDrawable(R.drawable.white_close_icon));
        toolsCardIsShowed = true ;
    }
    private void RvsSetting(RecyclerView.Adapter adapter, RecyclerView recyclerView) {
        if (adapter == editCategoryAdapter){
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }else if (adapter == editFoodAdapter){
            recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }
}
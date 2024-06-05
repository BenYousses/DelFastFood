package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.masterchef_app.Adapters.UserAdapter;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.databinding.ActivityUserListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    ActivityUserListBinding binding ;
    DatabaseReference userRef;
    UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        messangerRvSetUp();

        onBtnBackPressed();
    }

    private void onBtnBackPressed() {
        binding.messangerBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void messangerRvSetUp() {
        userAdapter = new UserAdapter(1);
        userRef =FirebaseDatabase.getInstance().getReference("MasterChefDataBase").child("Users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> usersList = new ArrayList<>();
                for (DataSnapshot userDataSnapshot :dataSnapshot.getChildren()){
                    User user = userDataSnapshot.getValue(User.class);
                    if(user!= null && !user.getUserKey().equals("B7uIdtC8TbZP2X0M5wmm6nT2y3F3")){
                        usersList.add(user);
                    }
                }
                userAdapter.setUsersList(usersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
binding.MessangerUserRv.setAdapter(userAdapter);
        binding.MessangerUserRv.setHasFixedSize(true);
        binding.MessangerUserRv.setLayoutManager(new LinearLayoutManager(this));
    }
}
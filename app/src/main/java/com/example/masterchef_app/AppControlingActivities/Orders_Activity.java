package com.example.masterchef_app.AppControlingActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.masterchef_app.Adapters.OrderAdapter;
import com.example.masterchef_app.OBJs.Order;
import com.example.masterchef_app.R;
import com.example.masterchef_app.databinding.ActivityOrdersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Orders_Activity extends AppCompatActivity {
ActivityOrdersBinding binding ;
    OrderAdapter orderAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
    super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getOrders();
        onBackBtnClicked();
    }

    private void onBackBtnClicked() {
        binding.orderGoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getOrders() {
         orderAdapter = new OrderAdapter();
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ArrayList<Order> orderList = new ArrayList<>();
                    for (DataSnapshot ordersDataSnapshot : dataSnapshot.getChildren()){
                        Order order = ordersDataSnapshot.getValue(Order.class);
                        if (order != null){
                            orderList.add(order);
                        }
                    }
                    orderAdapter.setOrdersList(orderList);
                    ordersRvSetUp();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ordersRvSetUp() {
        binding.ordersRv.setAdapter(orderAdapter);
        binding.ordersRv.setHasFixedSize(true);
        binding.ordersRv.setLayoutManager(new LinearLayoutManager(this));
    }
}
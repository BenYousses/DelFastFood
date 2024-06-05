package com.example.masterchef_app.AppControlingActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.masterchef_app.Adapters.CardsAdapter;
import com.example.masterchef_app.OBJs.Card;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.OBJs.Order;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.R;
import com.example.masterchef_app.UserActivities.MainActivity;
import com.example.masterchef_app.databinding.ActivityOrderDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class order_details extends AppCompatActivity {
ActivityOrderDetailsBinding binding;
Order order ;
CardsAdapter cardsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getOrder();
        cardRvSetting() ;
        customerIformationCardSetUp();
        onPhoneBotnCliked();
        getOrderTotalPrice();
        onBackBtnClicked();
    }

    private void getOrderTotalPrice() {
        binding.detailsOrderTotalPrice.setText(order.getTotalPrice() + "$");
   }
    private void onPhoneBotnCliked() {
        binding.phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + order.getCustomerPhone()));
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(order_details.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    startActivity(intent);
                }

            }
        });
    }

    private void customerIformationCardSetUp() {
        if(order != null){
          binding.custmerName.setText(order.getCustomerName());
            binding.custmerEmail.setText(order.getCustomerEmail());
            binding.custmerPhone.setText(order.getCustomerPhone());
        }
    }

    private void getOrder() {
        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("orderDetails");
    }
    private void cardRvSetting() {
        cardsAdapter = new CardsAdapter(2);
        if (order != null){
            cardsAdapter.setCardsList((ArrayList)order.getOrderCardList());
            binding.orderDetailsRv.setAdapter(cardsAdapter);
            binding.orderDetailsRv.setLayoutManager(new LinearLayoutManager(this));
            binding.orderDetailsRv.setHasFixedSize(true);
        }

    }
    private void onBackBtnClicked() {
        binding.orderDetailsGoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
}}
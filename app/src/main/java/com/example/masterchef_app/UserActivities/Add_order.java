package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.masterchef_app.Adapters.CardsAdapter;
import com.example.masterchef_app.OBJs.Card;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.OBJs.Order;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.databinding.ActivityAddOrderBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Add_order extends AppCompatActivity {
CardsAdapter cardsAdapter;
ActivityAddOrderBinding binding ;
String costomerPhone , costomerEmail ,costomerName , orderTotalPrice;
boolean nameIsReady , phoneIsReady ,emailIsReady = false ;
    List<Card> cardsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddOrderBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        cardRvSetting() ;
        customerInformation();
        saveOrder();
        orderTotalPrice();

    }

    private void orderTotalPrice() {
        Intent i = getIntent();
         orderTotalPrice = i.getStringExtra("orderTotalPrice");
        binding.orderTotalPrice.setText("Total Price :" + orderTotalPrice + " " + "$");
    }
    private void saveOrder() {
        binding.orderBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameIsReady && phoneIsReady && emailIsReady){
                 DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
                    String newOrderKey = orderRef.push().getKey();
                    Order newOrder = new Order(MainActivity.user,cardsList,newOrderKey,costomerPhone,costomerEmail,costomerName,orderTotalPrice);
                    orderRef.child(newOrderKey).setValue(newOrder);
                    deleteCards();

                }else{
                    Toast.makeText(Add_order.this, "Please full all information ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteCards() {
        DatabaseReference cardsRef = FirebaseDatabase.getInstance().getReference("Cards");
        cardsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot cardsDataSnapshot : dataSnapshot.getChildren()) {
                    Card card = cardsDataSnapshot.getValue(Card.class);
                    User user = card.getUserOwner();
                    if (user.getUserKey().equals(MainActivity.user.getUserKey())) {
                        cardsRef.child(card.getCardKey()).removeValue();
                    }
                }
              finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "there is a wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void customerInformation() {
        binding.custmerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
costomerName  = binding.custmerName.getText().toString().trim();
if (TextUtils.isEmpty(costomerName)){
    binding.cusyomerNameError.setText("name can't be Empty !");
    binding.cusyomerNameError.setVisibility(View.VISIBLE);
    nameIsReady = false ;
}else {
    binding.cusyomerNameError.setVisibility(View.GONE);
    nameIsReady = true ;
}
            }
        });
        binding.custmerPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
costomerPhone  = binding.custmerPhone.getText().toString().trim();
if (costomerPhone.startsWith("07") || costomerPhone.startsWith("06")|| costomerPhone.startsWith("+212")){
    if (TextUtils.isEmpty(costomerPhone)){
        binding.custmerPhoneError.setText("phone can't be Empty !");
        binding.custmerPhoneError.setVisibility(View.VISIBLE);
        phoneIsReady = false ;
    }else if(costomerPhone.length() < 10){
        binding.custmerPhoneError.setText("your phone is failer!");
        binding.custmerPhoneError.setVisibility(View.VISIBLE);
        phoneIsReady = false ;

    }else {
        binding.custmerPhoneError.setVisibility(View.GONE);
        phoneIsReady = true ;
    }
}else{
    binding.custmerPhoneError.setText("your phone is failer!");
    binding.custmerPhoneError.setVisibility(View.VISIBLE);
    phoneIsReady = false ;
}


            }
        });
        binding.custmerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
costomerEmail  = binding.custmerEmail.getText().toString().trim();
if (TextUtils.isEmpty(costomerEmail)){
    binding.custmerEmailError.setText("phone can't be Empty !");
    binding.custmerEmailError.setVisibility(View.VISIBLE);
    emailIsReady = false ;
}else if(!costomerEmail.endsWith("@gmail.com")){
    binding.custmerEmailError.setText("email should end whit (@gmail.com)");
    binding.custmerEmailError.setVisibility(View.VISIBLE);
    emailIsReady = false ;
}else {
    binding.custmerEmailError.setVisibility(View.GONE);
    emailIsReady = true ;
}
            }
        });

    }

    private void cardRvSetting() {
        cardsAdapter = new CardsAdapter(2);
       DatabaseReference cardsRef = FirebaseDatabase.getInstance().getReference("Cards");
        cardsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Card> cardList = new ArrayList<>();
                for (DataSnapshot cardsDataSnapshot : dataSnapshot.getChildren()) {
                    Card card = cardsDataSnapshot.getValue(Card.class);
                    User user = card.getUserOwner();
                    if (user.getUserKey().equals(MainActivity.user.getUserKey())) {
                        cardList.add(card);

                    }
                }
                cardsList = cardList ;
                cardsAdapter.setCardsList(cardList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "there is a wrong !", Toast.LENGTH_SHORT).show();
            }
        });
        binding.addOrderRv.setAdapter(cardsAdapter);
        binding.addOrderRv.setLayoutManager(new LinearLayoutManager(this));
        binding.addOrderRv.setHasFixedSize(true);
    }

}
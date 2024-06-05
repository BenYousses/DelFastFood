package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.masterchef_app.Adapters.CardsAdapter;
import com.example.masterchef_app.Listeners.onNumberCardChangeListenr;
import com.example.masterchef_app.OBJs.Card;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.databinding.ActivityCardsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cards extends AppCompatActivity {
ActivityCardsBinding binding ;
public static DatabaseReference cardsRef;
CardsAdapter cardsAdapter ;
String totlatPrice ;
    @Override
protected void onCreate(Bundle savedInstanceState) {
binding  = ActivityCardsBinding.inflate(getLayoutInflater());
super.onCreate(savedInstanceState);
setContentView(binding.getRoot());

cardRvSetting();
numberCardsControlSetting();
billCalculation();
onBtnBackPressed();
checkOutBtnSetUp();
    }

    private void checkOutBtnSetUp() {
        binding.checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Add_order.class);
                intent.putExtra("orderTotalPrice" , totlatPrice);
                startActivity(intent);
            }
        });
    }

    private void onBtnBackPressed() {
        binding.cardsBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void billCalculation() {
        cardsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Card> listCard = new ArrayList<>();
                double totalItem = 0 ;
                double delevryService = 0;
                for (DataSnapshot cardDataSnapshot : dataSnapshot.getChildren()){
                    Card card1 = cardDataSnapshot.getValue(Card.class);
                    if(card1.getUserOwner().getUserKey().equals(MainActivity.user.getUserKey())){
                        listCard.add(card1);
                    }
                }
                for (Card cards : listCard){
                    double foodPrice =Double.parseDouble(cards.getFoodType().getPrice());
                    int foodNumberCard = cards.getFoodType().getNumberCards();
                   totalItem +=(foodPrice * foodNumberCard);
                   delevryService += Double.parseDouble(cards.getFoodType().getDelSer());
                }

                binding.itemTotlaNum.setText(String.valueOf(totalItem + " " +"$"));
                binding.delevryServiceNum.setText(String.valueOf(delevryService+ " " +"$"));
                binding.TotalNum.setText(String.valueOf(totalItem + delevryService+ " " +"$"));
                totlatPrice = String.valueOf(totalItem + delevryService);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void numberCardsControlSetting() {
        cardsAdapter.setChangeListenr(new onNumberCardChangeListenr() {
            @Override
            public void onNumberCardChanged(int num, Card card, Food editFood) {
                if(num ==1 ){
                    editFood.setNumberCards(editFood.getNumberCards()+1);
                }else{
                    editFood.setNumberCards(editFood.getNumberCards()-1);
                }
                Card newCard = new Card(card.getUserOwner(),editFood,card.getCardKey());
                cardsRef.child(card.getCardKey()).setValue(newCard);
            }
        });

    }

    private void cardRvSetting() {
 cardsAdapter = new CardsAdapter(1);
 cardsRef = MainActivity.masterChefDtabase.getReference("Cards");
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
         if (cardList.size() > 0) {
             cardsAdapter.setCardsList(cardList);
             binding.LoadingCards.setVisibility(View.GONE);
             binding.cardScroll.setVisibility(View.VISIBLE);
         } else {
             binding.LoadingCards.setVisibility(View.VISIBLE);
             binding.LoadingCards.setText("<No Cards>");
             binding.cardScroll.setVisibility(View.GONE);
         }

     }

     @Override
     public void onCancelled(@NonNull DatabaseError databaseError) {
         Toast.makeText(Cards.this, "there is a wrong !", Toast.LENGTH_SHORT).show();
     }
 });
 binding.cardsRv.setAdapter(cardsAdapter);
        binding.cardsRv.setLayoutManager(new LinearLayoutManager(this));
        binding.cardsRv.setHasFixedSize(true);
    }
}
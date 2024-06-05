package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.masterchef_app.Adapters.CardsAdapter;
import com.example.masterchef_app.OBJs.Card;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.R;
import com.example.masterchef_app.databinding.ActivityDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Details_Activity extends AppCompatActivity {
ActivityDetailsBinding binding ;
public static Food food;
private int numberCard = 1 ;
    private float dX, dY;

    Card card ;
 public    boolean cardIsExests = false ;
private FirebaseDatabase masterChefDatabase;
public static DatabaseReference cardsRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
masterChefDatabase = FirebaseDatabase.getInstance();
        receiveFoodData();
        detailBtnsSetUp();
        addToCardSetUp();
        onBtnBackPressed();
        dragBtnCardSetUp ();
        getNumberCards();
        onNumberCardsBtnClicked();
    }

    private void onNumberCardsBtnClicked() {
        binding.cardsImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Cards.class);
                startActivity(intent);
            }
        });

    }

    private void dragBtnCardSetUp() {
       binding.draggableText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - motionEvent.getRawX();
                        dY = view.getY() - motionEvent.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(motionEvent.getRawX() + dX)
                                .y(motionEvent.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
    }

    private void onBtnBackPressed() {
        binding.detailsBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addToCardSetUp() {

        cardsRef = masterChefDatabase.getReference("Cards");
        String cardKey = cardsRef.push().getKey();
         card = new Card(MainActivity.user , food ,cardKey);

        binding.detailsAddToCardText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.detailsAddToCardText.setText("Adding...");
cardsRef.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Card card1;
   for(DataSnapshot searchDataSpanshot : dataSnapshot.getChildren()){
        card1 = searchDataSpanshot.getValue(Card.class);
        if(card1.getFoodType().getFoodKey().equals(card.getFoodType().getFoodKey()) && card1.getUserOwner().getUserKey().equals(card.getUserOwner().getUserKey())){
           cardIsExests = true;
           card = card1 ;
        }
   }
       if(!cardIsExests){
           card.getFoodType().setNumberCards(numberCard);
           cardsRef.child(cardKey).setValue(card).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()){
                       Toast.makeText(Details_Activity.this, "Added To Your Cards", Toast.LENGTH_SHORT).show();
                  finish();
                   }
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(Details_Activity.this, "Added to Your Cards Is Failure", Toast.LENGTH_SHORT).show();
               }
           });


       }else{
           card.getFoodType().setNumberCards( card.getFoodType().getNumberCards()  + numberCard);
           cardsRef.child(card.getCardKey()).setValue(card);
           finish();
      }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

});

            }
        });

    }

    private void detailBtnsSetUp() {
binding.detailsNumCardText.setText(String.valueOf(numberCard));
binding.detailsFoodPrice.setText(food.getPrice());
binding.detailsTotalPriceNum.setText(food.getPrice());
binding.detailsPlusNumCardBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        numberCard = numberCard + 1;
        binding.detailsNumCardText.setText(String.valueOf(numberCard));
     double foodPrice =Double.parseDouble(food.getPrice());
    double foodTotalPrice = Double.parseDouble(String.valueOf(foodPrice * numberCard));
    binding.detailsTotalPriceNum.setText(String.valueOf(foodTotalPrice));

    }
});
binding.detailsMinusNumCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberCard >= 1){
                    numberCard = numberCard - 1;
                    binding.detailsNumCardText.setText(String.valueOf(numberCard));
                    double foodPrice =Double.parseDouble(food.getPrice());
                    double foodTotalPrice = Double.parseDouble(String.valueOf(foodPrice * numberCard));
                    binding.detailsTotalPriceNum.setText(String.valueOf(foodTotalPrice));
                }


            }
        });
binding.detailsBackBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
});
    }

    private void receiveFoodData() {
        Intent i = getIntent();
         food = (Food) i.getSerializableExtra("FoodDetails");
        Picasso.get().load(food.getPic()).placeholder(R.drawable.empty_category_icon).into(binding.detailsFoodPic);
        binding.detailsFoodPrice.setText(food.getPrice());
        binding.detailsFoodCalory.setText(food.getCalories() + " Cal");
        binding.detailsFoodDescription.setText(food.getDescription());
        binding.detailsFoodTitle.setText(food.getTitle());
        binding.detailsFoodTime.setText(food.getTime() + " min");
        binding.detailsFoodLikes.setText(String.valueOf(food.getLikesNumbers() + " Like"));

    }
    private void getNumberCards() {
        cardsRef = MainActivity.masterChefDtabase.getReference("Cards");
        cardsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter =0;
                for (DataSnapshot cardsDataSnapshot : dataSnapshot.getChildren()) {
                    Card card = cardsDataSnapshot.getValue(Card.class);
                    User user = card.getUserOwner();
                    if (user.getUserKey().equals(MainActivity.user.getUserKey())) {
                        counter ++ ;
                    }
                    if (counter > 0){
                        binding.detailsCardsNum.setText(String.valueOf(counter));
                        binding.detailsCardsNum.setVisibility(View.VISIBLE);
                    }else{
                        binding.detailsCardsNum.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


}
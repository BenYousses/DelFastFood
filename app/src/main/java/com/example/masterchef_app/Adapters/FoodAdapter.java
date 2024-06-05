package com.example.masterchef_app.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masterchef_app.OBJs.FoodLikes;
import com.example.masterchef_app.UserActivities.Details_Activity;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.R;
import com.example.masterchef_app.UserActivities.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
ArrayList<Food> foodList = new ArrayList<>();
private int foodViewHolderType ;

    public FoodAdapter(int foodViewHolderType) {
        this.foodViewHolderType = foodViewHolderType;
    }
    public void setFoodList(ArrayList<Food> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (foodViewHolderType == 1 ){
            return new FoodViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_view_holder,viewGroup,false));
        }else{
            return new MenuFoodViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_menu_view_holder,viewGroup,false));

        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Food food = foodList.get(i);
        if (viewHolder instanceof FoodViewHolder){
            ((FoodViewHolder) viewHolder).bind(food.getPic(),food.getTitle(),food.getPrice());
            // go to show Food Details
            ((FoodViewHolder) viewHolder).showfoodDetailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext() , Details_Activity.class);
                    intent.putExtra("FoodDetails",food);
                    v.getContext().startActivity(intent);
                }
            });
            ((FoodViewHolder) viewHolder).likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FoodLikes newLike = new FoodLikes(MainActivity.user.getUserKey() , food.getFoodKey());
                    // access to foodLike by his Refrence
                    DatabaseReference foodUsersLikesRef = FirebaseDatabase.getInstance().getReference("foodLikes");
                    foodUsersLikesRef.child(food.getFoodKey()).setValue(newLike);
                    food.setLikesNumbers(food.getLikesNumbers()+1);
                    DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("Foods");
                    foodRef.child(food.getFoodKey()).setValue(food);

                }
            });
            ((FoodViewHolder) viewHolder).getUserLikesNumber(food);

        }else if (viewHolder instanceof MenuFoodViewHolder){
            ((MenuFoodViewHolder) viewHolder).bind(food.getPic(),food.getTitle(),food.getPrice());
            ((MenuFoodViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext() , Details_Activity.class);
                    intent.putExtra("FoodDetails",food);
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


    class MenuFoodViewHolder extends RecyclerView.ViewHolder{
private TextView foodName , foodPrice ;
private ImageView foodImg ;
private View itemView ;
        public MenuFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodmenu_name_VH);
            foodPrice = itemView.findViewById(R.id.foodmenu_price_VH);
            foodImg = itemView.findViewById(R.id.foodmenu_pic_VH);
            this.itemView = itemView ;
        }
        private void bind(String Img , String name , String price){
            Picasso.get().load(Img).placeholder(R.drawable.empty_category_icon).into(foodImg);
            foodName.setText(name);
            foodPrice.setText(price+ " " +"$");
        }
    }
    class FoodViewHolder extends RecyclerView.ViewHolder{
        private TextView foodName , foodPrice ;
        ImageButton showfoodDetailsBtn ;
        private ImageView foodImg ;
        ImageView likeBtn ;
        TextView likesNumberText ;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.holder_food_title);
            foodPrice = itemView.findViewById(R.id.holder_food_price);
            foodImg = itemView.findViewById(R.id.holder_food_pic);
            showfoodDetailsBtn = itemView.findViewById(R.id.holder_add_food_btn);
            likeBtn = itemView.findViewById(R.id.likes_btn);
            likesNumberText = itemView.findViewById(R.id.likesNumber);
        }
        private void bind(String Img , String name , String price){
            Picasso.get().load(Img).placeholder(R.drawable.empty_category_icon).into(foodImg);
            foodName.setText(name);
            foodPrice.setText(price+ " " +"$");

        }

        public void getUserLikesNumber(Food food) {
            // refence of foodLikes
            DatabaseReference foodUsersLikesRef = FirebaseDatabase.getInstance().getReference("foodLikes");

            foodUsersLikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    boolean userLiked = false;

                    for (DataSnapshot likeUserdataSnapShot : dataSnapshot.getChildren()) {
                        FoodLikes likesUsers = likeUserdataSnapShot.getValue(FoodLikes.class);
                        if (likesUsers.getUserKey().equals(MainActivity.user.getUserKey()) && likesUsers.getFoodKey().equals(food.getFoodKey())){
                            userLiked = true;
                        }
                    }

                    if (userLiked) {
                    likeBtn.setEnabled(false);
                   likeBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.red_full_heart));
                    } else if (!userLiked){
                   likeBtn.setEnabled(true);
                     likeBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ed_empty_favoret)); // Optional: Set default icon
                    }
                    if (food.getLikesNumbers() > 0){
                        if (food.getLikesNumbers() == 1){
                            likesNumberText.setText(String.valueOf(food.getLikesNumbers()+ " Like"));

                        }else{
                            likesNumberText.setText(String.valueOf(food.getLikesNumbers()+ " Likes"));
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }


    }
}

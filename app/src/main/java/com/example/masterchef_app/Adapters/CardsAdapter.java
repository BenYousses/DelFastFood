package com.example.masterchef_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masterchef_app.Listeners.onNumberCardChangeListenr;
import com.example.masterchef_app.OBJs.Card;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //list Of Cards
ArrayList<Card> cardsList = new ArrayList<>();
// I make This Listener  for listning to btns plus and minus Cards
private onNumberCardChangeListenr changeListenr;
//has a role fo returned to me the exact View Holder
  private int viewHolderType ;
// Adapter Constructer
    public CardsAdapter(int viewHolderType) {
        this.viewHolderType = viewHolderType;
    }

    public void setChangeListenr(onNumberCardChangeListenr changeListenr) {
        this.changeListenr = changeListenr;
    }

    public void setCardsList(ArrayList<Card> cardsList) {
        this.cardsList = cardsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (viewHolderType == 1){
            // return the View If ViewHoderType (CardsViewHolder)
            return new CardsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards_view_holder, viewGroup,false));
        }else if (viewHolderType == 2){
            // return the View If ViewHoderType (OrderCardsViewHolder)

            return new OrderCardsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_card_view_holder, viewGroup,false));

        }else{
            return null ;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        // get Card by Index "i"
        Card card = cardsList.get(i);
        // get cardFood into Card
        Food food = card.getFoodType();
        int numberCard  =  food.getNumberCards();
        double foodPrice =Double.parseDouble(food.getPrice());
        double totalPrice = numberCard * foodPrice ;
        if (viewHolder instanceof CardsViewHolder){
            ((CardsViewHolder) viewHolder).bind(food.getPic(),food.getTitle(),numberCard,String.valueOf(foodPrice), String.valueOf(totalPrice) );
            ((CardsViewHolder) viewHolder).plusNumberCardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // listened to plusBtn
                    changeListenr.onNumberCardChanged(1,card,food);
                }        });
            ((CardsViewHolder) viewHolder).minusnNumberCardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (food.getNumberCards() > 1){
                        // listened to plusBtn
                        changeListenr.onNumberCardChanged(-1, card,food);
                    }
                }
            }) ;
        } else if (viewHolder instanceof OrderCardsViewHolder) {

            ((OrderCardsViewHolder) viewHolder).bind(food.getTitle(),food.getPrice(),String.valueOf(totalPrice),String.valueOf(food.getNumberCards()),food.getPic());
        }
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    class  CardsViewHolder extends RecyclerView.ViewHolder{
ImageButton plusNumberCardBtn , minusnNumberCardBtn;
TextView NumberCardText , foodTitle , foodPrice , foodTotlePrice;
ImageView foodPic ;
    public CardsViewHolder(@NonNull View itemView) {
        super(itemView);
        // Inflate View into View
        plusNumberCardBtn = itemView.findViewById(R.id.card_plus_numCard_btn);
        minusnNumberCardBtn = itemView.findViewById(R.id.card_minus_numCard_btn);
        NumberCardText = itemView.findViewById(R.id.card_numCard_text);
        foodTitle = itemView.findViewById(R.id.card_food_title_VH);
        foodPrice = itemView.findViewById(R.id.card_food_price_VH);
        foodTotlePrice = itemView.findViewById(R.id.card_food_total_price_VH);
        foodPic = itemView.findViewById(R.id.card_food_pic_VH);


    }
    // paste the Data into Views
    public void bind (String pic , String title , int numCard , String price , String totalPrice ){
        // Picasso is the Lebrary have Role of get "ImgUri" and put it in ImageView
        Picasso.get().load(pic).placeholder(R.drawable.empty_category_icon).into(foodPic);
        foodTitle.setText(title);
        NumberCardText.setText(String.valueOf(numCard));
        foodPrice.setText(price + " " +"$");
        foodTotlePrice.setText(totalPrice + " " +"$");
    }
    }

    class OrderCardsViewHolder extends RecyclerView.ViewHolder {
TextView orderCardName , orderCardPrice, orderCardTotalPrice,orderPieceNumber ;
ImageView orderCardsPic;
        public OrderCardsViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCardName = itemView.findViewById(R.id.order_food_title_VH);
            orderCardPrice = itemView.findViewById(R.id.order_food_price_VH);
            orderCardTotalPrice = itemView.findViewById(R.id.order_food_total_price_VH);
            orderPieceNumber = itemView.findViewById(R.id.order_food_number_cardes_VH);
            orderCardsPic = itemView.findViewById(R.id.order_food_pic_VH);
        }
        public void bind (String name ,String price ,String totalPrice ,String pieceNum,String pic){
            orderCardName.setText(name);
            orderCardPrice.setText(price +" " + "$");
            orderCardTotalPrice.setText(totalPrice+" " + "$");
            orderPieceNumber.setText(pieceNum +" " + "Pieces");
            Picasso.get().load(pic).placeholder(R.drawable.empty_category_icon).into(orderCardsPic);
        }

    }

}

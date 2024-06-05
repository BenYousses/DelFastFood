package com.example.masterchef_app.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masterchef_app.AppControlingActivities.order_details;
import com.example.masterchef_app.OBJs.Food;
import com.example.masterchef_app.OBJs.Order;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.UserOrderViewHolder> {
ArrayList<Order> ordersList  = new ArrayList<>();
    @NonNull
    @Override
    public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserOrderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_user_view_holder,viewGroup,false));
    }

    public void setOrdersList(ArrayList<Order> ordersList) {
        this.ordersList = ordersList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderViewHolder orderViewHolder, int i) {
        Order order = ordersList.get(i);
        int  itemNumber = order.getOrderCardList().size();
        User user = order.getCustomer();
        orderViewHolder.bind(user.getName(), String.valueOf(itemNumber), user.getUserProfile());
        orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), order_details.class);
                intent.putExtra("orderDetails",order);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class UserOrderViewHolder extends RecyclerView.ViewHolder{

TextView userName , ItemNumber ;
ImageView userPic;
View itemView ;
        public UserOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            userName= itemView.findViewById(R.id.order_user_title_VH);
            ItemNumber= itemView.findViewById(R.id.order_number_items_VH);
            userPic= itemView.findViewById(R.id.orders_user_pic_VH);
            this.itemView = itemView;
        }
        public void bind (String name ,String itemNum , String pic){
            userName.setText(name);
            ItemNumber.setText(itemNum + " " +"Items");
            Picasso.get().load(pic).placeholder(R.drawable.empty_category_icon).into(userPic);
        }
    }
}

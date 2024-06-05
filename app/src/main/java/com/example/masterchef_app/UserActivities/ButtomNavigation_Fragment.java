package com.example.masterchef_app.UserActivities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.masterchef_app.OBJs.MessageModel;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ButtomNavigation_Fragment extends Fragment {
User supporte  ;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = mAuth.getCurrentUser();
    DatabaseReference newMessageRef = FirebaseDatabase.getInstance().getReference("Chats").child("NewMessages");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getSupport();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_bottom_navigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);LinearLayout cardBtns = view.findViewById(R.id.card_btns);
        RelativeLayout supportBtns = view.findViewById(R.id.support_btns);
        TextView newMessageSymbol = view.findViewById(R.id.newMessageSymbol);
        LinearLayout searchBtns = view.findViewById(R.id.search_btns);


        searchBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Food_Searching_Activity.class);
                startActivity(intent);
            }
        });

        cardBtns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Cards.class);
                startActivity(intent);
            }
        });
supportBtns.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        newMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()){
                  for (DataSnapshot newMessageDataSnapShot : dataSnapshot.getChildren()){
                      MessageModel messageModel = newMessageDataSnapShot.getValue(MessageModel.class);
                      if (messageModel.getRecieverKey().equals(currentUser.getUid())){
                          newMessageRef.child(messageModel.getMessageKey()).removeValue();
                      }
                  }
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (supporte != null){

            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("userConversation" ,supporte);
            startActivity(intent);

        }


    }
});
newMessagesAvaible(newMessageSymbol);
    }

    private void getSupport() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("MasterChefDataBase").child("Users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userDataSnapShot : dataSnapshot.getChildren()){
                    User user = userDataSnapShot.getValue(User.class);
                    if(user.getUserKey().equals("B7uIdtC8TbZP2X0M5wmm6nT2y3F3")){
                        supporte = user;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void newMessagesAvaible(TextView newMessagesIsExitst) {
        newMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for(DataSnapshot newMessageDataSnapshot : dataSnapshot.getChildren()){
                        MessageModel messageModel = newMessageDataSnapshot.getValue(MessageModel.class);
if (messageModel.getRecieverKey().equals(MainActivity.user.getUserKey())){
    newMessagesIsExitst.setVisibility(View.VISIBLE);
    break;
}else {
    newMessagesIsExitst.setVisibility(View.GONE);
}
 }
                }else {
                    newMessagesIsExitst.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                newMessagesIsExitst.setVisibility(View.GONE);
            }
        });
    }

}
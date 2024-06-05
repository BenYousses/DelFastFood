package com.example.masterchef_app.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masterchef_app.AppControlingActivities.Control_Main_Activity;
import com.example.masterchef_app.UserActivities.ChatActivity;
import com.example.masterchef_app.UserActivities.MainActivity;
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

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
ArrayList<User> usersList = new ArrayList<>();
private int userViewHolderType ;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = mAuth.getCurrentUser();
    public UserAdapter(int userViewHolderType) {
        this.userViewHolderType = userViewHolderType;
    }

    public void setUsersList(ArrayList<User> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (userViewHolderType == 1){
            return new ChatUserViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_user_view_holder,viewGroup,false));
        }else{
            return new ControlUserViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_view_holder,viewGroup,false));

        }

    }


    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder userViewHolder, int i) {
User user = usersList.get(i);


if(userViewHolder instanceof ChatUserViewHolder ){
    ((ChatUserViewHolder) userViewHolder).bind(user);
} else if (userViewHolder instanceof  ControlUserViewHolder) {
    ((ControlUserViewHolder) userViewHolder).bind(user.getName(), user.getUserProfile());
}

        userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , ChatActivity.class);
                intent.putExtra("userConversation",user);
           v.getContext().startActivity(intent);
                ((ChatUserViewHolder) userViewHolder).itemUserClicked(true,user.getUserKey() ,currentUser.getUid());

            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class ChatUserViewHolder extends RecyclerView.ViewHolder{
private TextView userName , lastMessageText, newMessageCounter;
private ImageView userImg ;
private View itemView;
public ChatUserViewHolder(@NonNull View itemView) {
            super(itemView);
    userName = itemView.findViewById(R.id.user_name_VH);
    lastMessageText = itemView.findViewById(R.id.last_message_VH);
    userImg = itemView.findViewById(R.id.user_profile_VH);
    newMessageCounter = itemView.findViewById(R.id.new_message_Number);
    this.itemView = itemView ;
        }
        public  void bind (User user){
    userName.setText(user.getName());
    Picasso.get().load(user.getUserProfile()).placeholder(R.drawable.empty_category_icon).into(userImg);
    getLastMessageModel(currentUser.getUid(), user , lastMessageModel -> {
        if (lastMessageModel != null){
            String lastMessage =currentUser.getUid().equals(lastMessageModel.getSenderKey()                               )?
                    "You : "+ lastMessageModel.getMessageText() : lastMessageModel.getMessageText();
            lastMessageText.setText(lastMessage);
        }else{
            lastMessageText.setText("");
        }
    });
        }

        public void getLastMessageModel (String CurrentUserKey , User recieverUser ,onLastMessageModelChangeListener listener){
    String senderRoomKey = CurrentUserKey+recieverUser.getUserKey();
            DatabaseReference newMessageRef = FirebaseDatabase.getInstance().getReference("Chats").child("NewMessages");
            DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("Chats").child(senderRoomKey);
    senderRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                for (DataSnapshot lastMessageModelDtabase : dataSnapshot.getChildren()){
                    MessageModel lastMessageModel = lastMessageModelDtabase.getValue(MessageModel.class);
                    listener.lastMessageModelChanged(lastMessageModel);
                }
            }else{
                listener.lastMessageModelChanged(null);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            listener.lastMessageModelChanged(null);
        }
    });
newMessageRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
             int newMessageConter = 0;
for (DataSnapshot newMessageDataSnapShot : dataSnapshot.getChildren()){
    MessageModel newMessagesModel = newMessageDataSnapShot.getValue(MessageModel.class);
    if (newMessagesModel.getRecieverKey().equals(CurrentUserKey) && newMessagesModel.getSenderKey().equals(recieverUser.getUserKey())){
        newMessageConter++;
    }
}
if (newMessageConter > 0){
    newMessageCounter.setVisibility(View.VISIBLE);
    newMessageCounter.setText(String.valueOf(newMessageConter));
}else{
    newMessageCounter.setVisibility(View.GONE);
}
        }else{
            newMessageCounter.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
        }

    public void itemUserClicked (boolean isClicked ,String reciverUserKey , String senderUserKey){
    if (isClicked){
        DatabaseReference newMessageRef = FirebaseDatabase.getInstance().getReference("Chats").child("NewMessages");
newMessageRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot newMessageDataSnapShot : dataSnapshot.getChildren()){
            MessageModel newMessagesModel = newMessageDataSnapShot.getValue(MessageModel.class);
            if (newMessagesModel.getRecieverKey().equals(senderUserKey) && newMessagesModel.getSenderKey().equals(reciverUserKey)){
                newMessageRef.child(newMessagesModel.getMessageKey()).removeValue();
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
    class ControlUserViewHolder extends RecyclerView.ViewHolder{
private ImageView userImage;
private TextView userName ;
        public ControlUserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_profile_control_VH);
            userName = itemView.findViewById(R.id.control_user_name);
        }
        public void bind (String name , String userImg){
            Picasso.get().load(userImg).into(userImage);
            userName.setText(name);
        }
    }
    interface  onLastMessageModelChangeListener {
        void lastMessageModelChanged(MessageModel lastMessageModel);
    }
}

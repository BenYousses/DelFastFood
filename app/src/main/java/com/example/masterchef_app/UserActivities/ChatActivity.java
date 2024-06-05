package com.example.masterchef_app.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.masterchef_app.Adapters.MessageAdapter;
import com.example.masterchef_app.AppControlingActivities.Control_Main_Activity;
import com.example.masterchef_app.OBJs.MessageModel;
import com.example.masterchef_app.OBJs.User;
import com.example.masterchef_app.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class ChatActivity extends AppCompatActivity {

    private DatabaseReference senderRef, receiverRef , newMessagesRef;
    private String senderKey,receiverKey, senderRoom, receiverRoom, messageText;
    private MessageAdapter messageAdapter;
    private ActivityChatBinding binding;
   private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        receiveUserData();
        sendMessageBtnSetUp();
    }
    private void receiveUserData() {
        Intent userIntent = getIntent();
        User user = (User) userIntent.getSerializableExtra("userConversation");
if (user!= null){
    senderKey = currentUser.getUid();
    receiverKey = user.getUserKey();
    binding.chatUserName.setText(user.getName());
    Picasso.get().load(user.getUserProfile()).into(binding.chatUserPic);
    chatRoomsSetUp();
}
    }

    private void chatRoomsSetUp() {
        if (receiverKey != null){
            //make a sender Room
            senderRoom = senderKey + receiverKey;
            //make a receiver Room
            receiverRoom = receiverKey + senderKey ;
        }

        senderRef = FirebaseDatabase.getInstance().getReference("Chats").child(senderRoom);
        receiverRef  = FirebaseDatabase.getInstance().getReference("Chats").child(receiverRoom);
        newMessagesRef = FirebaseDatabase.getInstance().getReference("Chats").child("NewMessages");
        loadSenderMessages();
    }

    private void loadSenderMessages() {

            messageAdapter = new MessageAdapter(currentUser.getUid());


        senderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<MessageModel> messageModelList = new ArrayList<>();
                for (DataSnapshot messageDataSnapShot : dataSnapshot.getChildren()){
                    MessageModel message = messageDataSnapShot.getValue(MessageModel.class);
                    messageModelList.add(message);

                }
                if (messageModelList.size() > 0){
                    messageAdapter.setMessagesList(messageModelList);
                    chatRvSetUp();
                    binding.loadingMessages.setVisibility(View.GONE);
    binding.chatRv.setVisibility(View.VISIBLE);
                }else{
                    binding.loadingMessages.setVisibility(View.VISIBLE);
                    binding.loadingMessages.setText("<No Messages>");
                    binding.chatRv.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chatRvSetUp() {
        binding.chatRv.setAdapter(messageAdapter);
        binding.chatRv.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRv.setHasFixedSize(true);
        binding.chatRv.scrollToPosition(messageAdapter.getItemCount()-1);


    }
    private void sendMessageBtnSetUp() {
        binding.sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageText = binding.messageInput.getText().toString().trim();
                if (!TextUtils.isEmpty(messageText)){
                    snedMessage();
                }else{
                    Toast.makeText(ChatActivity.this, "message is empty !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void snedMessage() {
        String messageKey = senderRef.push().getKey();
        if (messageKey == null) return;
        MessageModel messageModel = new MessageModel(currentUser.getUid(), messageKey,receiverKey,messageText);
        senderRef.child(messageKey).setValue(messageModel);
        receiverRef.child(messageKey).setValue(messageModel);
       newMessagesRef.child(messageKey).setValue(messageModel);
        binding.messageInput.setText("");
    }

}
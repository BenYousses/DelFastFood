package com.example.masterchef_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masterchef_app.OBJs.MessageModel;
import com.example.masterchef_app.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
private ArrayList<MessageModel>  messagesList = new ArrayList<>();
private String currentUserKey ;

    public MessageAdapter(String currentUserKey) {
        this.currentUserKey = currentUserKey;
    }
private static  final int messageIsSent =1 ;
private static final  int messageIsReceived = 2 ;

    @Override
    public int getItemViewType(int position) {
        MessageModel message= messagesList.get(position);
        if (message.getSenderKey().equals(currentUserKey)){
            return messageIsSent;
        }else{
            return messageIsReceived;
        }

    }

    public void setMessagesList(ArrayList<MessageModel> messagesList) {
        this.messagesList = messagesList;
    notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == messageIsReceived){
            return new receicedMessageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receive_message_view_holder, viewGroup , false));
        }else{
            return new sentMessageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.send_message_view_holder, viewGroup , false));

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MessageModel messageModel = messagesList.get(i);
        if (viewHolder instanceof sentMessageViewHolder){
            ((sentMessageViewHolder) viewHolder).bind(messageModel.getMessageText());
        }else if (viewHolder instanceof receicedMessageViewHolder){
            ((receicedMessageViewHolder) viewHolder).bind(messageModel.getMessageText());
        }

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    class sentMessageViewHolder extends RecyclerView.ViewHolder{
        TextView sentMessageText ;
        public sentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessageText = itemView.findViewById(R.id.textViewSendMessage);
        }
        public void bind (String sentMessage){
            sentMessageText.setText(sentMessage);
        }
    }
    class receicedMessageViewHolder extends RecyclerView.ViewHolder{
        TextView receiveMessageText ;

        public receicedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            receiveMessageText = itemView.findViewById(R.id.textViewRecieveMessage);
        }

        public void bind (String receiveMessage){
            receiveMessageText.setText(receiveMessage);
        }
    }
}

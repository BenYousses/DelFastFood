package com.example.masterchef_app.OBJs;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoomModel {
 private String chatRoomKey ;
 private List<String> usersKey;
 private Timestamp lastMessageTimestamp;
 private String lastMessagesenderKey;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomKey, List<String> usersKey, Timestamp lastMessageTimestamp, String lastMessagesenderKey) {
        this.chatRoomKey = chatRoomKey;
        this.usersKey = usersKey;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessagesenderKey = lastMessagesenderKey;
    }

    public String getChatRoomKey() {
        return chatRoomKey;
    }

    public void setChatRoomKey(String chatRoomKey) {
        this.chatRoomKey = chatRoomKey;
    }

    public List<String> getUsersKey() {
        return usersKey;
    }

    public void setUsersKey(List<String> usersKey) {
        this.usersKey = usersKey;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessagesenderKey() {
        return lastMessagesenderKey;
    }

    public void setLastMessagesenderKey(String lastMessagesenderKey) {
        this.lastMessagesenderKey = lastMessagesenderKey;
    }
}

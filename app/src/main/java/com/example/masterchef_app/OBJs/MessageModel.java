package com.example.masterchef_app.OBJs;

public class MessageModel {
    private String senderKey;
    private String recieverKey;
    private String messageKey ;
    private String messageText ;

    public MessageModel() {
    }

    public MessageModel(String senderKey, String messageKey,String recieverKey, String messageText) {
        this.senderKey = senderKey;
        this.messageKey = messageKey;
        this.messageText = messageText;
        this.recieverKey = recieverKey;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public String getMessageKry() {
        return messageKey;
    }

    public void setMessageKry(String messageKry) {
        this.messageKey = messageKry;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }



    public String getRecieverKey() {
        return recieverKey;
    }

    public void setRecieverKey(String recieverKey) {
        this.recieverKey = recieverKey;
    }
}

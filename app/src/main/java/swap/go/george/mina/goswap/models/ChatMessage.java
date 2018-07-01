package swap.go.george.mina.goswap.models;

import java.util.Date;


public class ChatMessage {

    private String messageText;
    private String messageSender;
    private String messageReciever;
    private long messageTime;


    public ChatMessage(String messageText, String messageSender, String messageReciever) {
        this.messageText = messageText;
        this.messageSender = messageSender;
        this.messageReciever = messageReciever;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessageReciever() {
        return messageReciever;
    }

    public void setMessageReciever(String messageReciever) {
        this.messageReciever = messageReciever;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

}
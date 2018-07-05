package swap.go.george.mina.goswap.models;

public class ChatFragmentMessages {

    private String senderId;
    private String reciverId;
    private String senderName;
    private String reciverName;
    private String itemId;
    private String itemName;
    private String itemPic;

    public ChatFragmentMessages(String senderId, String reciverId,
                                String senderName, String reciverName, String itemId, String itemName, String itemPic) {
        this.senderId = senderId;
        this.reciverId = reciverId;
        this.senderName = senderName;
        this.reciverName = reciverName;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPic = itemPic;
    }

    public String getItemPic() {
        return itemPic;
    }

    public void setItemPic(String itemPic) {
        this.itemPic = itemPic;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

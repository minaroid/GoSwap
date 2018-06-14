package swap.go.george.mina.goswap.models;

public class AdsHomeRecyclerItems {

    private int itemImage;
    private String price;
    private String title;


    public AdsHomeRecyclerItems(String title, String price, int itemImage) {
        this.title = title;
        this.price = price;
        this.itemImage = itemImage;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

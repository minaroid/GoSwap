package swap.go.george.mina.goswap.models;

import java.util.ArrayList;

import swap.go.george.mina.goswap.rest.apiModel.Item;

public class HomeRecyclerItems {
    private String header;
    private String subHeader;
    private String type;
    private ArrayList<Item> items;


    public HomeRecyclerItems(String header, String subHeader, String type ,ArrayList<Item> items) {
        this.header = header;
        this.subHeader = subHeader;
        this.type = type;
        this.items = items;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSubHeader() {
        return subHeader;
    }

    public void setSubHeader(String subHeader) {
        this.subHeader = subHeader;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}

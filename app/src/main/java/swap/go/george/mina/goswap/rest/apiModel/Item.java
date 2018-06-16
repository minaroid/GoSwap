
package swap.go.george.mina.goswap.rest.apiModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item implements Serializable{

    private Integer itemId;
    private String itemTitle;
    private String itemDesc;
    private String itemGover;
    private String itemCity;
    private String itemCate;
    private String date;
    private Integer authId;
    private ArrayList<String> itemPics = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemGover() {
        return itemGover;
    }

    public void setItemGover(String itemGover) {
        this.itemGover = itemGover;
    }

    public String getItemCity() {
        return itemCity;
    }

    public void setItemCity(String itemCity) {
        this.itemCity = itemCity;
    }

    public String getItemCate() {
        return itemCate;
    }

    public void setItemCate(String itemCate) {
        this.itemCate = itemCate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public ArrayList<String> getItemPics() {
        return itemPics;
    }

    public void setItemPics(ArrayList<String> itemPics) {
        this.itemPics = itemPics;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

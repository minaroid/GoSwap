
package swap.go.george.mina.goswap.rest.apiModel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swap.go.george.mina.goswap.db.DataConverter;


@Entity(tableName = "items")
public class Item implements Serializable{

    @PrimaryKey
    @ColumnInfo(name = "item_id")
    private int itemId;
    @ColumnInfo(name = "item_title")
    private String itemTitle;
    @ColumnInfo(name = "item_des")
    private String itemDesc;
    @ColumnInfo(name = "item_gover")
    private String itemGover;
    @ColumnInfo(name = "item_city")
    private String itemCity;
    @ColumnInfo(name = "item_cate")
    private String itemCate;
    @ColumnInfo(name = "item_date")
    private String date;
    @ColumnInfo(name = "item_auth_id")
    private int authId;
    @ColumnInfo(name = "item_views")
    private int views;
    @ColumnInfo(name = "item_lat")
    private String lat;
    @ColumnInfo(name = "item_lon")
    private String lon;
    @ColumnInfo(name = "item_auth_name")
    private String authName;
    @ColumnInfo(name = "item_phone")
    private int phone;
    @ColumnInfo(name = "item_pics")
    @TypeConverters(DataConverter.class)
    private ArrayList<String> itemPics = null;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
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

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public ArrayList<String> getItemPics() {
        return itemPics;
    }

    public void setItemPics(ArrayList<String> itemPics) {
        this.itemPics = itemPics;
    }
}

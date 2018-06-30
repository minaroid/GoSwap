package swap.go.george.mina.goswap.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import swap.go.george.mina.goswap.rest.apiModel.Item;

@Dao
public interface ItemDao {

    @Insert
    void insertItem(Item item);

    @Query("select * from items")
    List<Item> getItems();

    @Delete
    void deleteItem(Item item);

    @Update
    void updateItem(Item item);

    @Query("DELETE FROM items")
    void truncateTable();

    @Query("select * from items where item_id = :itemId")
    List<Item> getItemById(int itemId);
}

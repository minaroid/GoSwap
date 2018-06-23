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
    public void insertItem(Item item);

    @Query("select * from items")
    public List<Item> getItems();

    @Delete
    public void deleteItem(Item item);

    @Update
    public void updateItem(Item item);

    @Query("select * from items where item_id = :itemId")
    public List<Item> getItemById(int itemId);
}

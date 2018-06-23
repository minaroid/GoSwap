package swap.go.george.mina.goswap.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import swap.go.george.mina.goswap.rest.apiModel.Item;

@Database(entities = {Item.class},version = 1)
public abstract class AppDB extends RoomDatabase{

    public abstract ItemDao itemDao();

}

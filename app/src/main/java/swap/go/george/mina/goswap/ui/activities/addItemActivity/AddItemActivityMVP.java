package swap.go.george.mina.goswap.ui.activities.addItemActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.ArrayList;

public interface AddItemActivityMVP {

    interface View {

        void showMessage(String msg);
        void showProgress();
        void hideProgress();

    }

    interface Presenter{
        void setView(AddItemActivityMVP.View view);
        void uploadItem(Context context, ArrayList<Bitmap> bitmaps,
                        SharedPreferences userPref,
                        SharedPreferences LocationPref,
                        String title , String category,
                        String description,String name,
                        String phone,String itemId
        );
    }
}

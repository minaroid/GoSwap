package swap.go.george.mina.goswap.ui.activities.myAdsActivity;

import java.util.ArrayList;

import swap.go.george.mina.goswap.rest.apiModel.Item;

public interface MyAdsActivityMVP {

    interface View {

        void openItemActivity(Item item);

        void notifyAdapter(ArrayList<Item> items);
    }

    interface Presenter {
        void setView(MyAdsActivityMVP.View view);

        void loadAds(String id);
    }
}

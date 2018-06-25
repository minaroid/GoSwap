package swap.go.george.mina.goswap.ui.activities.myAdsActivity;

import swap.go.george.mina.goswap.rest.apiModel.Item;

public interface MyAdsActivityMVP {

    interface View {

        void openItemActivity(Item item);
    }
}

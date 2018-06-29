package swap.go.george.mina.goswap.ui.activities.userAdsActivity;

import swap.go.george.mina.goswap.rest.apiModel.Item;

public interface UserAdsActivityMVP {
    interface View {
        void openItemActivity(Item item);
    }
}

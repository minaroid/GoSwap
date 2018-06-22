package swap.go.george.mina.goswap.ui.activities.listActivity;

import swap.go.george.mina.goswap.rest.apiModel.Item;

public interface ListActivityMVP {

    interface View {

        void openItemActivity(Item item);
    }
}

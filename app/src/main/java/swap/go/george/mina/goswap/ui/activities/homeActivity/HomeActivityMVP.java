package swap.go.george.mina.goswap.ui.activities.homeActivity;

import swap.go.george.mina.goswap.models.HomeRecyclerItems;
import swap.go.george.mina.goswap.rest.apiModel.Category;

public interface HomeActivityMVP {

    interface View {

        void openListActivity(HomeRecyclerItems c);
    }
}

package swap.go.george.mina.goswap.ui.fragments.homeFragment;

import java.util.ArrayList;

import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.Item;

public interface HomeFragmentMVP {


    interface View {

        void showMessage(String msg);
        void notifyAdapter(ArrayList<Category> categories);
        void openItemActivity(Item item);
    }

    interface Presenter {

        void loadAllCountry();
        void loadByGovernate(String governate);
        void loadByCity(String city);
        void setView(HomeFragmentMVP.View v);
        HomeFragmentMVP.View getView();

    }

    interface Model {

//        Observable<HomeItem> results();
    }
}

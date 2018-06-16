package swap.go.george.mina.goswap.ui.fragments.homeFragment;

import java.util.ArrayList;

import rx.Observable;
import swap.go.george.mina.goswap.rest.apiModel.Category;

public interface HomeFragmentMVP {


    interface View {

        void showMessage(int msg);
        void notifyAdapter(ArrayList<Category> categories);
    }

    interface Presenter {

        void loadAllCountry();
        void loadByGovernate(String governate);
        void loadByCity(String city);
        void setView(HomeFragmentMVP.View v);
        HomeFragmentMVP.View getView();
        void rxUnsubscribe();

    }

    interface Model {

//        Observable<HomeItem> results();
    }
}

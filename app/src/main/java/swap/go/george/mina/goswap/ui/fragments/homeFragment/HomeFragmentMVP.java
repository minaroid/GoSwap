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

        void loadData();
        void setView(HomeFragmentMVP.View v);
        HomeFragmentMVP.View getView();
        void rxUnsubscribe();

    }

    interface Model {

//        Observable<HomeItem> results();
    }
}

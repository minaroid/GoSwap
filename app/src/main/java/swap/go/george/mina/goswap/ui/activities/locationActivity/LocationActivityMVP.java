package swap.go.george.mina.goswap.ui.activities.locationActivity;

import java.util.ArrayList;

import swap.go.george.mina.goswap.rest.apiModel.Governate;

public interface LocationActivityMVP {

    interface View{

        void notifyAdapter(ArrayList<Governate> governates);
        void wholeCityGovernate(String governate );
        void cityIsSelected(String city,String governate);
    }

    interface Model{

    }

    interface Presenter{

        void loadData();
        void setView(LocationActivityMVP.View view);
    }
}

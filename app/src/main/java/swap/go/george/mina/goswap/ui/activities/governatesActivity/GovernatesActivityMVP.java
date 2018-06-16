package swap.go.george.mina.goswap.ui.activities.governatesActivity;

import java.util.ArrayList;

import swap.go.george.mina.goswap.rest.apiModel.Governate;

public interface GovernatesActivityMVP {

    interface View{

        void notifyAdapter(ArrayList<Governate> governates);
        void finishActivity();
        void openCitiesActivity(String governate,ArrayList<String> cities);
    }

    interface Model{

    }

    interface Presenter{

        void loadData();
        void setView(GovernatesActivityMVP.View view);
    }
}

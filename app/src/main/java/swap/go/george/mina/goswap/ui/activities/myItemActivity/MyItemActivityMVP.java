package swap.go.george.mina.goswap.ui.activities.myItemActivity;

public interface MyItemActivityMVP {

    interface View {
        void finishActivity();
    }

    interface Presenter{

        void setView(MyItemActivityMVP.View view);
        void deleteItem(int id);
    }
}

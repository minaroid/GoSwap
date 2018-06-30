package swap.go.george.mina.goswap.ui.activities.settingsActivity;

public interface SettingsActivityMVP {

    interface View {

        void IsDeleted(Boolean isDeleted);
    }

    interface Presenter {

        void setView(SettingsActivityMVP.View view);

        void deleteAccount(String id);

    }
}

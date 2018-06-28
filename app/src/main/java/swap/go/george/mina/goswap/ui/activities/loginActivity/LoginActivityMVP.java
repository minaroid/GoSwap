package swap.go.george.mina.goswap.ui.activities.loginActivity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public interface LoginActivityMVP {

    interface View{

        void showMessage(int msg);

    }

    interface Presenter{

        void setView(LoginActivityMVP.View v );
        void login (String email , String password);
        void loginFB (LoginButton fbButton, CallbackManager callbackManager);
    }
}

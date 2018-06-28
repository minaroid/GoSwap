package swap.go.george.mina.goswap.ui.activities.signupActivity;

import android.graphics.Bitmap;

public interface SignUpActivityMVP {

    interface View{

        void showMessage(int msg);
        void showLoading();
    }

    interface Presenter{

        void setView(SignUpActivityMVP.View view);
        void uploadData(Bitmap bitmap,String name,String email,String pass,String phone);

    }
}

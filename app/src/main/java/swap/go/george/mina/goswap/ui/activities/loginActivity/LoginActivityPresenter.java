package swap.go.george.mina.goswap.ui.activities.loginActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.rest.API;
import swap.go.george.mina.goswap.rest.apiModel.UserInfo;

import static android.content.Context.MODE_PRIVATE;

public class LoginActivityPresenter implements LoginActivityMVP.Presenter {

    private LoginActivityMVP.View view;
    private Context context;
    private SharedPreferences.Editor editor ;
    private LoginActivity loginActivity;
    private Call<ArrayList<UserInfo>> call;
    @Override
    public void setView(LoginActivityMVP.View v) {
        this.view = v;
        this.context = (Context) v;
        this.loginActivity = (LoginActivity) v ;
        this.editor = context.getSharedPreferences("user", MODE_PRIVATE).edit();
    }

    @Override
    public void login(String email, String password) {
        call = API.getItems().checkLogin(email, password);
        call.enqueue(new Callback<ArrayList<UserInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<UserInfo>> call, Response<ArrayList<UserInfo>> response) {
                ArrayList<UserInfo> info = response.body();
                String state = info.get(0).getState();
                if (state.equals("founded")) {
                    editor.putString("name", info.get(0).getInfo().get(0).getName());
                    editor.putString("email", info.get(0).getInfo().get(0).getEmail());
                    editor.putString("fbId", info.get(0).getInfo().get(0).getFbId());
                    editor.putString("id", String.valueOf(info.get(0).getInfo().get(0).getSwapperId()));
                    editor.putString("pass", info.get(0).getInfo().get(0).getPassword());
                    editor.putString("phone", String.valueOf(info.get(0).getInfo().get(0).getPhone()));
                    editor.putString("pic", String.valueOf(info.get(0).getInfo().get(0).getPic()));
                    editor.commit();
                    loginActivity.finish();
                } else if (state.equals("password")) {
                    view.showMessage(0);
                } else if (state.equals("not")) {
                    view.showMessage(1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserInfo>> call, Throwable t) {
                view.showMessage(2);
            }
        });
    }

    @Override
    public void loginFB(LoginButton fbButton ,CallbackManager callbackManager) {
        fbButton.setReadPermissions(Arrays.asList("email"));
        fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                final String name = profile.getName();
                final String userId = loginResult.getAccessToken().getUserId();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                try {
                                    call = API.getItems().checkLoginFb(object.getString("email"));
                                    call.enqueue(new Callback<ArrayList<UserInfo>>() {
                                        @Override
                                        public void onResponse(Call<ArrayList<UserInfo>> call,
                                                               Response<ArrayList<UserInfo>> response) {
                                            ArrayList<UserInfo> info = response.body();
                                            String state = info.get(0).getState();
                                            if(state.equals("not")){
                                                try {
                                                    call = API.getItems()
                                                            .signUpUsingFb(name,object.getString("email"),userId);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                call.enqueue(new Callback<ArrayList<UserInfo>>() {
                                                    @Override
                                                    public void onResponse(Call<ArrayList<UserInfo>> call,
                                                                           Response<ArrayList<UserInfo>> response) {
                                                        ArrayList<UserInfo> info = response.body();
                                                        String state = info.get(0).getState();

                                                        if(state.equals("failed")){
                                                            view.showMessage(3);
                                                        }
                                                        else if(state.equals("inserted")){
                                                            editor.putString("name",info.get(0).getInfo().get(0).getName());
                                                            editor.putString("email",info.get(0).getInfo().get(0).getEmail());
                                                            editor.putString("fbId",info.get(0).getInfo().get(0).getFbId());
                                                            editor.putString("id", String.valueOf(info.get(0).getInfo().get(0).getSwapperId()));
                                                            editor.putString("pass",info.get(0).getInfo().get(0).getPassword());
                                                            editor.putString("phone", String.valueOf(info.get(0).getInfo().get(0).getPhone()));
                                                            editor.putString("pic", String.valueOf(info.get(0).getInfo().get(0).getPic()));
                                                            editor.commit();
                                                            loginActivity.finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ArrayList<UserInfo>> call, Throwable t) {
                                                        view.showMessage(3);
                                                    }
                                                });
                                            }
                                            else if(state.equals("founded")){
                                                editor.putString("name",info.get(0).getInfo().get(0).getName());
                                                editor.putString("email",info.get(0).getInfo().get(0).getEmail());
                                                editor.putString("fbId",info.get(0).getInfo().get(0).getFbId());
                                                editor.putString("id", String.valueOf(info.get(0).getInfo().get(0).getSwapperId()));
                                                editor.putString("pass",info.get(0).getInfo().get(0).getPassword());
                                                editor.putString("phone", String.valueOf(info.get(0).getInfo().get(0).getPhone()));
                                                editor.putString("pic", String.valueOf(info.get(0).getInfo().get(0).getPic()));
                                                editor.commit();
                                                loginActivity.finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<UserInfo>> call, Throwable t) {
                                            view.showMessage(3);
                                        }
                                    });
                                } catch (JSONException e) {

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                view.showMessage(3);
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                view.showMessage(3);
            }
        });
    }
}

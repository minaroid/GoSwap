package swap.go.george.mina.goswap.ui.activities.activitySignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.API;
import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.SignUpInfo;

public class SignUpActivity extends AppCompatActivity implements SignUpActivityMVP.View{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_signup_fb)
    LoginButton loginButton;

    final String pic = "https://graph.facebook.com/" +""
            + "/picture?type=small";

    private Call<ArrayList<SignUpInfo>> call;
    private CallbackManager callbackManager;
    private SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        callbackManager = CallbackManager.Factory.create();
        initFbBtn();
    }

    protected void initFbBtn(){
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();

                final String name = profile.getName();
                final String userId = loginResult.getAccessToken().getUserId();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    call = API.getItems()
                                            .signUpUsingFb(name,object.getString("email"),userId);
                                    call.enqueue(new Callback<ArrayList<SignUpInfo>>() {
                                        @Override
                                        public void onResponse(Call<ArrayList<SignUpInfo>> call,
                                                               Response<ArrayList<SignUpInfo>> response) {
                                            ArrayList<SignUpInfo> info = response.body();
                                            String state = info.get(0).getState();
                                            if(state.equals("founded")){
                                                SignUpActivity.this.showMesaage("this user is exist try forget password");
                                            }
                                            else if(state.equals("failed")){
                                                SignUpActivity.this.showMesaage("sorry try again later !");
                                            }
                                            else if(state.equals("inserted")){
                                                editor.putString("name",info.get(0).getInfo().get(0).getName());
                                                editor.putString("email",info.get(0).getInfo().get(0).getEmail());
                                                editor.putString("fbId",info.get(0).getInfo().get(0).getFbId());
                                                editor.putString("id", String.valueOf(info.get(0).getInfo().get(0).getSwapperId()));
                                                editor.putString("pass",info.get(0).getInfo().get(0).getPassword());
                                                editor.putString("phone", String.valueOf(info.get(0).getInfo().get(0).getPhone()));
                                                editor.commit();
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<SignUpInfo>> call, Throwable t) {
                                            t.printStackTrace();
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

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void showMesaage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }
}

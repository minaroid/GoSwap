package swap.go.george.mina.goswap.ui.activities.activityLoginAndSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.API;
import swap.go.george.mina.goswap.rest.apiModel.UserInfo;
import swap.go.george.mina.goswap.utils.CommonUtils;

public class LoginActivity extends AppCompatActivity implements LoginActivityMVP.view{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_layout_email)
    TextInputLayout emailLayout;
    @BindView(R.id.text_layout_pass)
    TextInputLayout passLayout;

    @BindView(R.id.et_email)
    AppCompatEditText email;
    @BindView(R.id.et_pass)
    AppCompatEditText pass;

    @BindView(R.id.tv_login_fb)
    LoginButton fbsLoginUpButton;
    private Call<ArrayList<UserInfo>> call;
    private CallbackManager callbackManager;
    private SharedPreferences.Editor editor ;
    private CommonUtils utils = new CommonUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        callbackManager = CallbackManager.Factory.create();
        initFbBtn();
        intInputLayouts();
    }

    protected void intInputLayouts(){
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(email.getText().toString().isEmpty()){
                    emailLayout.setErrorEnabled(true);
                    email.setError("please type your name");
                }else {
                    emailLayout.setErrorEnabled(false);
                }
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(email.getText().toString().isEmpty()){
                    emailLayout.setErrorEnabled(true);
                    email.setError("please type your email");
                }else {
                    emailLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(pass.getText().toString().isEmpty()){
                    passLayout.setErrorEnabled(true);
                    pass.setError("please type password");
                }else {
                    passLayout.setErrorEnabled(false);
                }
            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pass.getText().toString().isEmpty()){
                    passLayout.setErrorEnabled(true);
                    pass.setError("please type password");
                }
                else if (pass.getText().length() > 8){
                    passLayout.setErrorEnabled(true);
                    pass.setError("password length");
                }
                else {
                    passLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    protected void initFbBtn(){
        fbsLoginUpButton.setReadPermissions(Arrays.asList("email"));
        fbsLoginUpButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
                                                    LoginActivity.this.showMessage("sorry try again later !");
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
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<UserInfo>> call, Throwable t) {
                                            t.printStackTrace();
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
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<UserInfo>> call, Throwable t) {
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
              Log.d("ddddddd","cancel");
            }

            @Override
            public void onError(FacebookException error) {
              error.printStackTrace();
            }
        });
    }

    public void loginBtnClicked(View view) {

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

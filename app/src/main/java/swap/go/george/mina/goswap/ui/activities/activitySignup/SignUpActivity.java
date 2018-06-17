package swap.go.george.mina.goswap.ui.activities.activitySignup;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
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
    LoginButton fbsSignUpButton;
    @BindView(R.id.imge_signUp)
    CircleImageView imageView;
    @BindView(R.id.et_name)
    AppCompatEditText name;
    @BindView(R.id.et_pass)
    AppCompatEditText pass;
    @BindView(R.id.et_con_pass)
    AppCompatEditText conPass;
    @BindView(R.id.et_email)
    AppCompatEditText email;
    @BindView(R.id.et_phone)
    AppCompatEditText phone;

    private Call<ArrayList<SignUpInfo>> call;
    private CallbackManager callbackManager;
    private SharedPreferences.Editor editor ;
    private CharSequence importWay[];
    private Bitmap b;

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
        fbsSignUpButton.setReadPermissions(Arrays.asList("email"));
        fbsSignUpButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
                                                SignUpActivity.this.showMessage("this user is exist try forget password");
                                            }
                                            else if(state.equals("failed")){
                                                SignUpActivity.this.showMessage("sorry try again later !");
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
        Bitmap b1;
        if (requestCode == 100 && resultCode == RESULT_OK) {
            b = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(b);

        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            try {
                b1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                b = scaleDown(b1, 500, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }

    public void signUpBtnClicked(View view) {

    }

    public void selectImage(View view) {
        importWay = new CharSequence[]{"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setItems(importWay, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (importWay[which].equals("Camera")) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, 100);
                } else {

                    Intent i = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 200);
                }
            }
        });
        builder.show();
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

}

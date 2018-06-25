package swap.go.george.mina.goswap.ui.activities.activityLoginAndSignup;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.VolleyMultipartRequest;
import swap.go.george.mina.goswap.rest.VolleySingleton;
import swap.go.george.mina.goswap.rest.apiModel.UserInfo;
import swap.go.george.mina.goswap.utils.CommonUtils;

public class SignUpActivity extends AppCompatActivity implements SignUpActivityMVP.View{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    @BindView(R.id.text_layout_name)
    TextInputLayout nameLayout;
    @BindView(R.id.text_layout_pass)
    TextInputLayout passLayout;
    @BindView(R.id.text_layout_con_pass)
    TextInputLayout conPassLayout;
    @BindView(R.id.text_layout_email)
    TextInputLayout emailLayout;
    @BindView(R.id.text_layout_phone)
    TextInputLayout phoneLayout;

    private Call<ArrayList<UserInfo>> call;
    private SharedPreferences.Editor editor ;
    private CharSequence importWay[];
    private Bitmap selectedImage;
    private CommonUtils utils = new CommonUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        intInputLayouts();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap b1;
        if (requestCode == 100 && resultCode == RESULT_OK) {

            selectedImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(selectedImage);

        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            try {
                b1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                selectedImage = scaleDown(b1, 500, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    public void signUpBtnClicked(View view) {
        String nameText = name.getText().toString();
        String emailText = email.getText().toString();
        String passText = pass.getText().toString();
        String phoneText = phone.getText().toString();
        if (nameText.equals("") || emailText.equals("") || passText.equals("") || nameText.equals("")
                || phoneText.equals("") || selectedImage == null) {
            this.showMessage("check Inputs Fields !!");
        } else {
            uploadToServer(codec(selectedImage, Bitmap.CompressFormat.PNG, 3),
                    nameText, emailText, passText, phoneText);
        }

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

    public void uploadToServer(Bitmap bitmap, final String name, final String email, final String pass, final String phone) {
        String url = "http://192.168.1.7:5000/signup_data";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(response.data));
                            JSONObject object = jsonArray.getJSONObject(0);
                            String uploadState = object.getString("state");
                            if (uploadState.equals("inserted")) {
                                JSONArray info = object.getJSONArray("info");
                                JSONObject infoo = info.getJSONObject(0);
                                editor.putString("name", infoo.getString("name"));
                                editor.putString("email", infoo.getString("email"));
                                editor.putString("fbId", infoo.getString("fbId"));
                                editor.putString("id", infoo.getString("swapperId"));
                                editor.putString("pass", infoo.getString("password"));
                                editor.putString("phone", infoo.getString("phone"));
                                editor.putString("pic", infoo.getString("pic"));
                                editor.commit();
                                finish();
                            } else if (uploadState.equals("founded")) {
                                SignUpActivity.this.showMessage("this user is exist try forget password");
                            } else {
                                SignUpActivity.this.showMessage("sorry try again later !");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("filename", getBitmapAsByteArray(selectedImage).toString());
                params.put("name", name);
                params.put("email", email);
                params.put("pass", pass);
                params.put("phone", phone);
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                long name = System.currentTimeMillis();
                params.put("pic", new VolleyMultipartRequest.DataPart(name + ".png",
                        getBitmapAsByteArray(selectedImage)));
                return params;
            }

        };
        VolleySingleton.getInstance(this).addRequestQue(volleyMultipartRequest);
    }

    protected void intInputLayouts(){
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(name.getText().toString().isEmpty()){
                    nameLayout.setErrorEnabled(true);
                    name.setError("please type your name");
                }else {
                    nameLayout.setErrorEnabled(false);
                }
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(name.getText().toString().isEmpty()){
                    nameLayout.setErrorEnabled(true);
                    name.setError("please type your name");
                }else {
                    nameLayout.setErrorEnabled(false);
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

        conPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(conPass.getText().toString().isEmpty()){
                    conPassLayout.setErrorEnabled(true);
                    conPass.setError("please type confirm password");
                }
                else {
                    conPassLayout.setErrorEnabled(false);
                }
            }
        });
        conPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(conPass.getText().toString().isEmpty()){
                    conPassLayout.setErrorEnabled(true);
                    conPass.setError("please type confirm password");
                }
                else if (!pass.getText().toString().equals(conPass.getText().toString())){
                    conPassLayout.setErrorEnabled(true);
                    conPass.setError("not Matches");
                }

                else {
                    conPassLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(email.getText().toString().isEmpty()){
                    emailLayout.setErrorEnabled(true);
                    email.setError("please type your email");
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
                if(email.getText().toString().isEmpty()) {
                    emailLayout.setErrorEnabled(true);
                    email.setError("please type your name");
                } else if (!CommonUtils.isEmailValid(email.getText().toString())) {
                    emailLayout.setErrorEnabled(true);
                    email.setError("Invalid Email");
                    }
                else {
                    emailLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(phone.getText().toString().isEmpty()){
                    phoneLayout.setErrorEnabled(true);
                    phone.setError("please type your phone");
                }else {
                    phoneLayout.setErrorEnabled(false);
                }
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone.getText().toString().isEmpty()){
                    phoneLayout.setErrorEnabled(true);
                    phone.setError("please type your name");
                }else {
                    phoneLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passLayout.setCounterEnabled(true);
        passLayout.setCounterMaxLength(8);
    }

    private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
                                int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);

        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        } else
            return null;
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

}

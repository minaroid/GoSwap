package swap.go.george.mina.goswap.ui.activities.signupActivity;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import retrofit2.Call;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.VolleyMultipartRequest;
import swap.go.george.mina.goswap.rest.VolleySingleton;
import swap.go.george.mina.goswap.rest.apiModel.UserInfo;
import swap.go.george.mina.goswap.utils.CommonUtils;

public class SignUpActivity extends AppCompatActivity implements SignUpActivityMVP.View
        ,View.OnClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_signUp)
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
    @BindView(R.id.progress_load)
    ProgressBar progressBar;

    private Bitmap selectedImage;
    private SignUpActivityMVP.Presenter presenter;
    private ArrayList<String> ImagesUri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.btn_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intInputLayouts();

        presenter = new SignUpActivityPresenter();
        presenter.setView(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE:

                if(resultCode==RESULT_OK && data!=null)
                {
                    ImagesUri = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);

                    try
                    {
                            selectedImage = loadBitmap(Uri.fromFile(new File(ImagesUri.get(0))).toString());
                            imageView.setImageBitmap(selectedImage);
                            imageView.setBorderColor(getResources().getColor(R.color.green));

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void showMessage(int msg) {
        progressBar.setVisibility(View.GONE);

        switch (msg){

            case 0:
                Toast.makeText(this, R.string.msg_user_is_exist,Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this,R.string.msg_have_problem,Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public boolean checkForm() {
        imageView.setBorderColor(getResources().getColor(R.color.green));
        if(name.getText().toString().isEmpty()){
            nameLayout.setErrorEnabled(true);
            name.setError(getString(R.string.error_name_invalid));
            return false;
        }

        if(email.getText().toString().isEmpty()||!CommonUtils.isEmailValid(email.getText().toString())){
            emailLayout.setErrorEnabled(true);
            email.setError(getString(R.string.error_email_invalid));
            return false;
        }

        if(pass.getText().toString().isEmpty()||pass.getText().toString().length() < 8){
            passLayout.setErrorEnabled(true);
            pass.setError(getString(R.string.error_pass_invalid));
            return false;
        }

        if(conPass.getText().toString().isEmpty()||!conPass.getText().toString().equals(pass.getText().toString())){
            conPassLayout.setErrorEnabled(true);
            conPass.setError(getString(R.string.error_pass_matches));
            return false;
        }
        if(phone.getText().toString().isEmpty()||phone.getText().length() > 14){
            phoneLayout.setErrorEnabled(true);
            phone.setError(getString(R.string.error_phone_invalid));
            return false;
        }

        if(selectedImage == null){
            imageView.setBorderColor(getResources().getColor(R.color.red));
            Toast.makeText(this, R.string.error_image,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    protected void intInputLayouts(){
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                if(name.getText().toString().isEmpty()){
                    nameLayout.setErrorEnabled(true);
                    name.setError(getString(R.string.error_name_invalid));
                }else {
                    nameLayout.setErrorEnabled(false);
                }
            }}
        });

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                 if(pass.getText().toString().isEmpty()){
                    passLayout.setErrorEnabled(true);
                    pass.setError(getString(R.string.error_pass_type));
                }
                    if (pass.getText().length() > 8){
                        passLayout.setErrorEnabled(true);
                        pass.setError(getString(R.string.error_pass_big));
                    }
                else {
                    passLayout.setErrorEnabled(false);
                }
            }}
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pass.getText().length() > 8){
                    passLayout.setErrorEnabled(true);
                    pass.setError(getString(R.string.error_pass_big));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        conPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                if(conPass.getText().toString().isEmpty()){
                    conPassLayout.setErrorEnabled(true);
                    conPass.setError(getString(R.string.error_pass_confirm));
                }
                else if(!pass.getText().toString().equals(conPass.getText().toString())){
                    conPassLayout.setErrorEnabled(true);
                    conPass.setError(getString(R.string.error_pass_matches));
                }
                else {
                    conPassLayout.setErrorEnabled(false);
                }
            }}
        });


        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                if(email.getText().toString().isEmpty()){
                    emailLayout.setErrorEnabled(true);
                    email.setError(getString(R.string.error_email_type));

                }
                else if (!CommonUtils.isEmailValid(email.getText().toString())) {
                    emailLayout.setErrorEnabled(true);
                    email.setError(getString(R.string.error_email_invalid));
                }
                else {
                    emailLayout.setErrorEnabled(false);
                }
            }}
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                if(phone.getText().toString().isEmpty()){
                    phoneLayout.setErrorEnabled(true);
                    phone.setError(getString(R.string.error_phone_type));
                }
                else if(phone.getText().length()>14){
                    phoneLayout.setErrorEnabled(true);
                    phone.setError(getString(R.string.error_phone_length));
                }
                else {
                    phoneLayout.setErrorEnabled(false);
                }
            }}
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(phone.getText().length()>14){
                    phoneLayout.setErrorEnabled(true);
                    phone.setError(getString(R.string.error_phone_length));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passLayout.setCounterEnabled(true);
        passLayout.setCounterMaxLength(8);
        phoneLayout.setCounterEnabled(true);
        phoneLayout.setCounterMaxLength(14);
    }

    @OnClick({R.id.btn_register_signup,R.id.img_signUp})
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_register_signup:
                if(checkForm()){
                    presenter.uploadData(selectedImage,name.getText().toString(),email.getText().toString()
                    ,pass.getText().toString(),phone.getText().toString());
                }
                break;
            case R.id.img_signUp:
                selectedImage = null;
                FilePickerBuilder.getInstance().setMaxCount(1)
                        .setSelectedFiles(ImagesUri)
                        .setActivityTheme(R.style.FilePickerTheme)
                        .pickPhoto(this);
                break;
        }
    }

    public Bitmap loadBitmap(String url) {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }
}

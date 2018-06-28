package swap.go.george.mina.goswap.ui.activities.loginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.ui.activities.signupActivity.SignUpActivity;
import swap.go.george.mina.goswap.utils.CommonUtils;

public class LoginActivity extends AppCompatActivity implements LoginActivityMVP.View,
        View.OnClickListener{

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
    private CallbackManager callbackManager;
    private LoginActivityMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.btn_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        callbackManager = CallbackManager.Factory.create();
        intInputLayouts();
        presenter = new LoginActivityPresenter();
        presenter.setView(this);
        presenter.loginFB(fbsLoginUpButton,callbackManager);
    }

    protected void intInputLayouts() {
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (email.getText().toString().isEmpty()) {
                        emailLayout.setErrorEnabled(true);
                        email.setError(getString(R.string.error_email_type));
                    } else if (!CommonUtils.isEmailValid(email.getText().toString())) {
                        emailLayout.setErrorEnabled(true);
                        email.setError(getString(R.string.error_email_invalid));
                    }
                }
            }
        });

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pass.getText().toString().isEmpty()) {
                        passLayout.setErrorEnabled(true);
                        pass.setError(getString(R.string.error_pass_type));
                    }
                }
            }
        });
    }

    @Override
    public void showMessage(int msg) {
        switch (msg){
            case 0:
                Toast.makeText(this, R.string.msg_incorrect_pass,Toast.LENGTH_SHORT).show();
                break;
            case 1 :
                Toast.makeText(this, R.string.msg_mail_exist,Toast.LENGTH_SHORT).show();
                break;
            case 2 :
                Toast.makeText(this, R.string.msg_have_problem,Toast.LENGTH_SHORT).show();
                break;
            case 3 :
                Toast.makeText(this, R.string.msg_fb_cant_login,Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkForm(){
        emailLayout.setErrorEnabled(false);
        passLayout.setErrorEnabled(false);
        if(email.getText().toString().isEmpty()||!CommonUtils.isEmailValid(email.getText().toString())){
            emailLayout.setErrorEnabled(true);
            email.setError(getString(R.string.error_email_invalid));
            return false;
        }
        if(pass.getText().toString().isEmpty()){
            emailLayout.setErrorEnabled(true);
            pass.setError(getString(R.string.error_pass_type));
            return false;
        }

        return true;
    }

    @OnClick({R.id.btn_register,R.id.btn_login})
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_register:
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
            case R.id.btn_login:
                if(checkForm()){
                  presenter.login(email.getText().toString(),pass.getText().toString());
                }
                break;
        }

    }
}

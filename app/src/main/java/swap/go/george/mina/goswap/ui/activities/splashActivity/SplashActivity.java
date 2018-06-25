package swap.go.george.mina.goswap.ui.activities.splashActivity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.ui.activities.homeActivity.HomeActivity;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.image_logo)
    ImageView logoImage;
    @BindView(R.id.image_loading)
    ImageView loadingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        logoImage.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo));
        loadingImage.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_loading));
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }, 4000);
    }
}

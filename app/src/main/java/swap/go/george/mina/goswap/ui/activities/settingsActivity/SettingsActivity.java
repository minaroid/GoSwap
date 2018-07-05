package swap.go.george.mina.goswap.ui.activities.settingsActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.ui.activities.homeActivity.HomeActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, SettingsActivityMVP.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_load)
    ProgressBar progressBar;

    private SharedPreferences.Editor userPrefEditor;
    private SharedPreferences userPref;
    private SettingsActivityMVP.Presenter presenter;
    private String userId;

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    progressBar.setVisibility(View.VISIBLE);
                    presenter.deleteAccount(userId);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.btn_delete_account);
        userPrefEditor = getSharedPreferences("user", MODE_PRIVATE).edit();
        userPref = getSharedPreferences("user", MODE_PRIVATE);
        userId = userPref.getString("id", null);
        presenter = new SettingsActivityPresenter();
        presenter.setView(this);
    }

    @OnClick(R.id.btn_delete_account)
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.warn_delete_confirm)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public void IsDeleted(Boolean isDeleted) {
        userPrefEditor.clear();
        userPrefEditor.commit();
        HomeActivity.appDB.itemDao().truncateTable();

        progressBar.setVisibility(View.GONE);
        finish();
    }
}

package swap.go.george.mina.goswap.ui.activities.addItemActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import swap.go.george.mina.goswap.R;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener
,AddItemActivityMVP.View{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_category)
    Spinner categorySpinner;
    @BindView(R.id.image_error)
    ImageView selectedImagesImageError;
    @BindView(R.id.email_error)
    ImageView emailImageError;
    @BindView(R.id.phone_error)
    ImageView phoneImageError;
    @BindView(R.id.title_error)
    ImageView titleImageError;
    @BindView(R.id.des_error)
    ImageView desImageError;
    @BindView(R.id.category_error)
    ImageView categoryImageError;
    @BindView(R.id.name_error)
    ImageView nameImageError;
    @BindView(R.id.tv_selected_images_count)
    TextView selectedImagesCount;
    @BindView(R.id.add_item_layout)
    RelativeLayout screenLayout;
    @BindView(R.id.title_layout)
    TextInputLayout titleLayout;
    @BindView(R.id.ed_title)
    AppCompatEditText title;
    @BindView(R.id.des_layout)
    TextInputLayout desLayout;
    @BindView(R.id.ed_des)
    AppCompatEditText description;
    @BindView(R.id.name_layout)
    TextInputLayout nameLayout;
    @BindView(R.id.ed_name)
    AppCompatEditText name;
    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;
    @BindView(R.id.ed_email)
    AppCompatEditText email;
    @BindView(R.id.phone_layout)
    TextInputLayout phoneLayout;
    @BindView(R.id.ed_phone)
    AppCompatEditText phone;
    @BindView(R.id.tv_current_location)
    TextView currentLocationText;
    @BindView(R.id.progress_loading)
    ProgressBar progressBar ;
    private ArrayList<Bitmap> selectedImagesBitmap = new ArrayList<>();
    private ArrayList<String> selectedImagesUri = new ArrayList<>();
    private SharedPreferences userPref,locationPref;
    private AddItemActivityPresenter presenter;
    private ArrayList<String> categories ;
    private String selectedCategory ="Select Category";
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    presenter.uploadItem(AddItemActivity.this,
                            selectedImagesBitmap,
                            userPref,locationPref,
                            title.getText().toString(),
                            selectedCategory,description.getText().toString(),
                            name.getText().toString(),
                            phone.getText().toString(),String.valueOf(System.currentTimeMillis()));
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
        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Swap your Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userPref = getSharedPreferences("user", MODE_PRIVATE);
        locationPref = getSharedPreferences("currentLocation", MODE_PRIVATE);
        categories = (ArrayList<String>) getIntent().getSerializableExtra("categories");
        init();
        presenter = new AddItemActivityPresenter();
        presenter.setView(this);

    }

    protected  void init(){
        titleLayout.setCounterEnabled(true);
        titleLayout.setCounterMaxLength(30);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(title.getText().toString().length() > 30){
                    titleLayout.setErrorEnabled(true);
                    title.setError("Title length is very big");
                    titleImageError.setBackground(getResources().getDrawable(R.drawable.error_badge));
                }else {
                    titleLayout.setErrorEnabled(false);
                    titleImageError.setBackground(getResources().getDrawable(R.drawable.accept_badge));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        desLayout.setCounterEnabled(true);
        desLayout.setCounterMaxLength(500);
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(description.getText().toString().length() > 500){
                    desLayout.setErrorEnabled(true);
                    description.setError("Description length is very big");
                    desImageError.setBackground(getResources().getDrawable(R.drawable.error_badge));
                }else {
                    desLayout.setErrorEnabled(false);
                    desImageError.setBackground(getResources().getDrawable(R.drawable.accept_badge));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nameLayout.setCounterEnabled(true);
        nameLayout.setCounterMaxLength(20);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(name.getText().toString().length() > 20){
                    nameLayout.setErrorEnabled(true);
                    name.setError("name length is very big");
                    nameImageError.setBackground(getResources().getDrawable(R.drawable.error_badge));
                }else {
                    nameLayout.setErrorEnabled(false);
                    nameImageError.setBackground(getResources().getDrawable(R.drawable.accept_badge));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneLayout.setCounterEnabled(true);
        phoneLayout.setCounterMaxLength(14);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone.getText().toString().length() > 14){
                    phoneLayout.setErrorEnabled(true);
                    phone.setError("Phone length is very big");
                    phoneImageError.setBackground(getResources().getDrawable(R.drawable.error_badge));
                }else {
                    phoneLayout.setErrorEnabled(false);
                    phoneImageError.setBackground(getResources().getDrawable(R.drawable.accept_badge));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        email.setEnabled(false);
        emailImageError.setBackground(getResources().getDrawable(R.drawable.accept_badge));
        email.setText(userPref.getString("email",null));
        phone.setText(userPref.getString("phone",null));
        name.setText(userPref.getString("name",null));
        currentLocationText.setText(locationPref.getString("city",null));
        List<String> list = new ArrayList<>();
        list.clear();
        list.add("Select Category");
        for (String ca :categories)
            list.add(ca);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        list);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayAdapter.notifyDataSetChanged();
        categorySpinner.setAdapter(spinnerArrayAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                if(!selectedCategory.equals("Select Category")){
                    categoryImageError.setBackground(getResources().getDrawable(R.drawable.accept_badge));
                }
                else {
                    categoryImageError.setBackground(getResources().getDrawable(R.drawable.default_badge));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory ="Select Category";
            }
        });

    }

    public boolean checkForm(){
        boolean checked = true;
        selectedImagesImageError.setBackground(getResources().getDrawable(R.drawable.default_badge));
        titleImageError.setBackground(getResources().getDrawable(R.drawable.default_badge));
        desImageError.setBackground(getResources().getDrawable(R.drawable.default_badge));
        categoryImageError.setBackground(getResources().getDrawable(R.drawable.default_badge));
        if(selectedImagesBitmap.size() == 0){
            selectedImagesImageError.setBackground(getResources().getDrawable(R.drawable.error_badge));
            checked = false;
        }

        if(title.getText().toString().isEmpty()||title.getText().toString().length()>30){
            titleImageError.setBackground(getResources().getDrawable(R.drawable.error_badge));
            checked = false;
        }
        if(description.getText().toString().isEmpty()||description.getText().toString().length()>500){
            desImageError.setBackground(getResources().getDrawable(R.drawable.error_badge));
            checked = false;
        }
        if(name.getText().toString().isEmpty()||name.getText().toString().length()>20){
            nameImageError.setBackground(getResources().getDrawable(R.drawable.error_badge));
            checked = false;
        }
        if(phone.getText().toString().isEmpty()||phone.getText().toString().length()>14){
            phoneImageError.setBackground(getResources().getDrawable(R.drawable.error_badge));
            checked = false;
        }
        if(selectedCategory.equals("Select Category")){
            categoryImageError.setBackground(getResources().getDrawable(R.drawable.error_badge));
            checked = false;
        }

        return checked;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE:

                if(resultCode==RESULT_OK && data!=null)
                {
                    selectedImagesUri = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);

                    try
                    {
                        for (String path:selectedImagesUri) {
                            selectedImagesBitmap.add(loadBitmap(Uri.fromFile(new File(path)).toString()));
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

        }
        selectedImagesCount.setText(String.valueOf(selectedImagesBitmap.size()));
        if(selectedImagesBitmap.size()>0){
            selectedImagesImageError.setImageResource(R.drawable.accept_badge);
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

    @OnClick({R.id.btn_submit,R.id.view_select_image})
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_submit :
                if(checkForm()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Are you sure you want add this Item")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                else {
                    Snackbar.make(screenLayout, "Check Form Fields", Snackbar.LENGTH_LONG).show();
                }

                break;
            case R.id.view_select_image:
                selectedImagesUri.clear();
                selectedImagesBitmap.clear();
                selectedImagesCount.setText(String.valueOf(selectedImagesBitmap.size()));
                selectedImagesImageError.setImageResource(R.drawable.default_badge);
                FilePickerBuilder.getInstance().setMaxCount(6)
                        .setSelectedFiles(selectedImagesUri)
                        .setActivityTheme(R.style.FilePickerTheme)
                        .pickPhoto(this);
                break;
        }
    }


    @Override
    public void showMessage(String msg) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Item Published Successfully")
                .setIcon(R.drawable.icon_done)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home :
                this.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.finish();
    }

}

package swap.go.george.mina.goswap.ui.activities.addItemActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import swap.go.george.mina.goswap.R;

public class AddItemActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_category)
    AppCompatSpinner categorySpinner;
    @BindView(R.id.image_error)
    ImageView selectedImagesImageError;
    @BindView(R.id.tv_selected_images_count)
    TextView selectedImagesCount;

    private ArrayList<Bitmap> selectedImagesBitmap = new ArrayList<>();
    private ArrayList<String> selectedImagesUri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Swap your Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }


    protected  void init(){



    }

    public void selectImage(View view) {
        selectedImagesUri.clear();
        selectedImagesBitmap.clear();
        selectedImagesCount.setText(String.valueOf(selectedImagesBitmap.size()));
        selectedImagesImageError.setImageResource(R.drawable.default_badge);
        FilePickerBuilder.getInstance().setMaxCount(8)
                .setSelectedFiles(selectedImagesUri)
                .setActivityTheme(R.style.FilePickerTheme)
                .pickPhoto(this);

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

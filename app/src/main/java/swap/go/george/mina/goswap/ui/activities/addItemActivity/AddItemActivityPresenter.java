package swap.go.george.mina.goswap.ui.activities.addItemActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.rest.API;
import swap.go.george.mina.goswap.rest.VolleyMultipartRequest;
import swap.go.george.mina.goswap.rest.VolleySingleton;

public class AddItemActivityPresenter implements AddItemActivityMVP.Presenter{
    private  AddItemActivityMVP.View view ;
    int x=0;

    @Override
    public void setView(AddItemActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void uploadItem(Context context, final ArrayList<Bitmap> bitmaps,
                           final  SharedPreferences userPref,
                           final SharedPreferences LocationPref,
                           final String title , final String category,
                           final String description, final String name,
                           final String phone, final String itemId) {
        view.showProgress();
        final String item_Id = itemId;
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");
        final String date  = df.format(d);
        Log.d("wdqfeqf", String.valueOf(date));

        Call<String> c = API.getItems().uploadItemData(title,description,
                LocationPref.getString("governate",null),
                LocationPref.getString("city",null),
                category,date,userPref.getString("id",null),
                LocationPref.getString("lat",null),
                LocationPref.getString("lon",null),name,phone);
        c.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


        for ( Bitmap b : bitmaps){

            final Bitmap  bb= scaleDown(b,500,true);
            String url = "http://192.168.43.254:5000/upladItemImages";
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                           x++;
                           if(x==bitmaps.size()){
                               view.hideProgress();
                           }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }

            })
            {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("filename", getBitmapAsByteArray(bb).toString());
                    return params;
                }

                @Override
                protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                    Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                    long name = System.currentTimeMillis();
                    params.put("pic", new VolleyMultipartRequest.DataPart(name + ".png",
                            getBitmapAsByteArray(bb)));
                    return params;
                }

            };
            VolleySingleton.getInstance(context).addRequestQue(volleyMultipartRequest);

        }

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

package swap.go.george.mina.goswap.ui.activities.signupActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import swap.go.george.mina.goswap.rest.VolleyMultipartRequest;
import swap.go.george.mina.goswap.rest.VolleySingleton;

import static android.content.Context.MODE_PRIVATE;

public class SignUpActivityPresenter implements SignUpActivityMVP.Presenter{

    private SignUpActivityMVP.View view;
    private Context context;
    private SharedPreferences.Editor editor ;
    private SignUpActivity signUpActivity;
    @Override
    public void setView(SignUpActivityMVP.View view) {

        this.view = view;
        this.context = (Context) view;
        this.signUpActivity = (SignUpActivity) view ;
        editor = context.getSharedPreferences("user", MODE_PRIVATE).edit();
    }

    @Override
    public void uploadData(final Bitmap bitmap, final String name, final String email, final String pass, final String phone) {
        String url = "http://192.168.1.4:5000/signup_data";
        view.showLoading();
        final Bitmap  bm = scaleDown(bitmap,500,true);
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
                                signUpActivity.finish();
                            } else if (uploadState.equals("founded")) {
                                view.showMessage(0);
                            } else {
                                view.showMessage(1);
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
                params.put("filename", getBitmapAsByteArray(bm).toString());
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
                        getBitmapAsByteArray(bm)));
                return params;
            }

        };
        VolleySingleton.getInstance(context).addRequestQue(volleyMultipartRequest);
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

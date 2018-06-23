package swap.go.george.mina.goswap.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import swap.go.george.mina.goswap.R;

import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private Context context;

    private ArrayList<String> images = new ArrayList<>();

    public ImagePagerAdapter(Context context,ArrayList<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.item_pager_image, container, false);
        ImageView im = (ImageView) v.findViewById(R.id.pager_image);
        String photoUrl = "http://192.168.1.2:5000"
                + images.get(position);
        Glide.with(context)
                .load(photoUrl)
                .into(im);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    public void swapData(ArrayList<String> im){
        images.clear();
        images.addAll(im);
        notifyDataSetChanged();
    }
}

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

import java.util.ArrayList;

import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.ui.dialogs.ImagesPagerDialog;

public class ImagePagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private boolean largeSize = false;

    private ArrayList<String> images = new ArrayList<>();

    public ImagePagerAdapter(Context context, ArrayList<String> images, boolean largeSize) {
        this.context = context;
        this.images = images;
        this.largeSize = largeSize;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;
        if (largeSize) {
            v = layoutInflater.inflate(R.layout.item_pager_image2, container, false);
        } else {
            v = layoutInflater.inflate(R.layout.item_pager_image, container, false);
        }

        ImageView im = v.findViewById(R.id.pager_image);
        String photoUrl = "http://192.168.43.254:5000"
                + images.get(position);
        Glide.with(context)
                .load(photoUrl)
                .into(im);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagesPagerDialog dialog = new ImagesPagerDialog(context, images, position);
                dialog.show();
            }
        });
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

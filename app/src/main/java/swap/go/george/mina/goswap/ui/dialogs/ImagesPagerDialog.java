package swap.go.george.mina.goswap.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.ui.adapters.ImagePagerAdapter;

public class ImagesPagerDialog extends Dialog {
    private ViewPager viewPager;
    private ImagePagerAdapter pagerAdapter;

    public ImagesPagerDialog(@NonNull Context context, ArrayList<String> urls, int pos) {
        super(context);
        setContentView(R.layout.dialog_images_pager);
        getWindow().getAttributes().windowAnimations = R.style.MyCustomTheme;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ImagePagerAdapter(context, urls, true);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(pos);
    }
}

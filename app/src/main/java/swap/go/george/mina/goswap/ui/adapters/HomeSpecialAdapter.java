package swap.go.george.mina.goswap.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.models.SpecialHomeRecyclerItems;

public class HomeSpecialAdapter extends RecyclerView.Adapter<HomeSpecialAdapter.MyViewHolder> {
    private ArrayList<SpecialHomeRecyclerItems> recyclerItems;
    private Context mcontext;

    public HomeSpecialAdapter(ArrayList<SpecialHomeRecyclerItems> recyclerItems) {

        this.recyclerItems = recyclerItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_special_it, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Glide.with(mcontext)
//                .load(recyclerItems.get(position).getItemImage())
//                .into(holder.iv_card_graphics);

        holder.tv_card_header.setText(recyclerItems.get(position).getItemHeader());
        holder.tv_card_sub_header.setText(recyclerItems.get(position).getItemSubHeader());
        holder.tv_card_number.setText(recyclerItems.get(position).getItemNumber());
    }

    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_image)
        ImageView iv_card_graphics;
        @BindView(R.id.item_header)
        TextView tv_card_header;
        @BindView(R.id.item_sub_header)
        TextView tv_card_sub_header;
        @BindView(R.id.tv_card_number)
        TextView tv_card_number;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}

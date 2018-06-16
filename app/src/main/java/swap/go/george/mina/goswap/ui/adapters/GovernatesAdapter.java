package swap.go.george.mina.goswap.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Governate;
import swap.go.george.mina.goswap.ui.activities.governatesActivity.GovernatesActivityMVP;

public class GovernatesAdapter extends RecyclerView.Adapter<GovernatesAdapter.MyViewHolder> implements View.OnClickListener {

    private ArrayList<Governate> governates = new ArrayList<>();
    private Context context;
    private GovernatesActivityMVP.View activityView;

    public  GovernatesAdapter(Context context){
        this.context = context ;
        this.activityView = (GovernatesActivityMVP.View )context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_governorate, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.GoverText.setText(governates.get(position).getGovernate());
        holder.GoverText.setTag(position);
        holder.GoverText.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return governates.size();
    }

    public void swapData(ArrayList<Governate> governates){
        this.governates = governates;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Governate g = governates.get((int)v.getTag());
        activityView.openCitiesActivity(g.getGovernate(),g.getCities());
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_gover)
        TextView GoverText;

        public MyViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}

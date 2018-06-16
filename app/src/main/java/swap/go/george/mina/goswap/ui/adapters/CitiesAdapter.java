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
import swap.go.george.mina.goswap.ui.activities.citiesActivity.CitiesActivityMVP;
import swap.go.george.mina.goswap.ui.activities.governatesActivity.GovernatesActivityMVP;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.MyViewHolder> implements View.OnClickListener {

    private ArrayList<String> cities = new ArrayList<>();
    private Context context;
    private CitiesActivityMVP.View activityView;
    private String governate ;

    public CitiesAdapter(Context context){
        this.context = context ;
        this.activityView = (CitiesActivityMVP.View )context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.cityText.setText(cities.get(position));
        holder.cityText.setTag(position);
        holder.cityText.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public void swapData(ArrayList<String> cities ,String governate){
        this.cities = cities;
        this.governate = governate;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        String c = cities.get((int)v.getTag());
        activityView.selectedCity(governate,c);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_city)
        TextView cityText;

        public MyViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}

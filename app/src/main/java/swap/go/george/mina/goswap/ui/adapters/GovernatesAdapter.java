package swap.go.george.mina.goswap.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Governate;
import swap.go.george.mina.goswap.ui.activities.locationActivity.LocationActivityMVP;

public class GovernatesAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LocationActivityMVP.View activityView;

    private ArrayList<String> header = new ArrayList<>();
    private HashMap<String,ArrayList<String>> child = new HashMap<>();

    public  GovernatesAdapter(Context context){
        this.context = context ;
        this.activityView = (LocationActivityMVP.View) context;
    }

    public void swapData(ArrayList<Governate> governates){
        for (Governate g : governates){
            this.header.add(g.getGovernate());
            this.child.put(g.getGovernate(),g.getCities());
        }
        notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return this.header.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.child.get(this.header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.header.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.child.get(this.header.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String governate = (String) getGroup(groupPosition);
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_governorate,null);
        TextView text = (TextView) convertView.findViewById(R.id.tv_gover);

        text.setText(governate);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityView.wholeCityGovernate(governate);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String city = (String) getChild(groupPosition,childPosition);

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_city,null);
        TextView text = (TextView) convertView.findViewById(R.id.tv_city);

        text.setText(city);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityView.cityIsSelected(city,header.get(groupPosition));
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}

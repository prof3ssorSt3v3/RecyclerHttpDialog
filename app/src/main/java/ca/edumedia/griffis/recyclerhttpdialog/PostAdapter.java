package ca.edumedia.griffis.recyclerhttpdialog;

/**
 * Created by griffis on 2017-12-16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import ca.edumedia.griffis.recyclerhttpdialog.models.BuildingPOJO;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public static final String BUILDING_KEY = "building_key";

    private Context mContext;
    private List<BuildingPOJO> mBuildings;

    //Constructor
    public PostAdapter(Context context, List<BuildingPOJO> buildings) {
        this.mContext = context;
        this.mBuildings = buildings;
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        final BuildingPOJO aBuilding = mBuildings.get(position);

        holder.tvName.setText(aBuilding.getNameEN());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //put the aBuilding into an Intent as an Extra
                //and pass it to another activity...
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBuildings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public View mView;

        public ViewHolder(View buildingView) {
            super(buildingView);

            tvName = (TextView) buildingView.findViewById(R.id.item_text);
            mView = buildingView;
        }
    }
}

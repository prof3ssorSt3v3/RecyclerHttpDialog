package ca.edumedia.griffis.recyclerhttpdialog;

/**
 * Created by griffis on 2017-12-16.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import ca.edumedia.griffis.recyclerhttpdialog.models.BuildingPOJO;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final String BUILDING_KEY = "building_key";
    private static final String TAG = "TAG";

    private Context mContext;
    private ArrayList<BuildingPOJO> mBuildings;

    //Constructor
    public MyAdapter(Context context, ArrayList<BuildingPOJO> buildings) {
        this.mContext = context;
        this.mBuildings = buildings;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
        //holder and position have to be declared final so they can be used inside the Dialog.

        final BuildingPOJO aBuilding = mBuildings.get(position);

        holder.tvName.setText(aBuilding.getNameEN());

        //We are using the whole view to listen for the click
        //this could be just a delete button though
        //the variable for the delete button would be declared inside the MyAdapter.ViewHolder static class
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //put the aBuilding into an Intent as an Extra
                //and pass it to another activity...
                //OR... Let's call our Dialog to ask the user something
                //like confirm that you want to delete this building

                Log.i(TAG, "onClick: " + aBuilding.getNameEN());

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // TODO - externalize strings to strings.xml
                builder.setTitle("Confirm")
                        .setMessage("Are you sure?")

                        // Displays: OK
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete this course
                                Toast.makeText(mContext, "Deleted Course: " + aBuilding.getNameEN(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Deleted Course: " + aBuilding.getNameEN());
                                //since they clicked OK
                                //we will delete the building from the List of BuildingPOJOs.
                                mBuildings.remove(position);
                                //then call notifyDataSetChanged( )
                                MyAdapter.this.notifyDataSetChanged();
                                //hide the delete button so it can't be clicked twice
                                //holder.bDeleteCourse.setVisibility(View.INVISIBLE);

                                //we can also call the IntentService to DELETE the building on the Server here...
                                dialog.dismiss();
                            }
                        })

                        // Displays: Cancel
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();

                                Toast.makeText(mContext, "CANCELLED: Deleted Course: " + aBuilding.getNameEN(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "CANCELLED: Deleted Course: " + aBuilding.getNameEN());
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

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

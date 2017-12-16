package ca.edumedia.griffis.recyclerhttpdialog;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import ca.edumedia.griffis.recyclerhttpdialog.models.BuildingPOJO;


/**
 * Created by griffis on 2017-12-12.
 */

public class DeleteDialog extends DialogFragment {

    public interface DeleteDialogListener {
        //this listener will be used back in the MainActivity to know when the Dialog closes
        void onFinishDeleteDialog(int inputNum);
    }

    private static final String BUILDING_KEY = "building_key";
    private static final String TAG = "TAG";

    public DeleteDialog(){
        //Empty constructor required
    }

    public static DeleteDialog newInstance(String key, BuildingPOJO aBuilding) {
        DeleteDialog frag = new DeleteDialog();
        Bundle args = new Bundle();
        args.putParcelable(key, aBuilding);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {

        final BuildingPOJO aBuilding = getArguments().getParcelable(BUILDING_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage( "Are you sure you want to do something to this item?" )
                .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // show me your Intent to delete it.
                        //Intent to launch new Activity
                        // receive DataItem/BuildingPOJO Bundle from clicked item in the RecyclerView
                        // positive button id is -1
                        //passing the building ID back to the MainActivity
                        //call the method back in the MainActivity to remove the DataItem from the Array of DataItems

                        DeleteDialogListener activity = (DeleteDialogListener) getActivity();
                        activity.onFinishDeleteDialog( aBuilding.getBuildingId() );
                        Toast.makeText(getActivity(), "Clicked OK " +id + " " + aBuilding.getNameEN(), Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //just make the dialog go away
                        Activity activity = getActivity();
                        Toast.makeText(activity, "Clicked Cancel " +id, Toast.LENGTH_SHORT).show();
                        // negative button id is -2
                        dialog.cancel();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }


}

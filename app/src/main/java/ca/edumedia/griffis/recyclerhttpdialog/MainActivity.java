package ca.edumedia.griffis.recyclerhttpdialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;

import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.edumedia.griffis.recyclerhttpdialog.models.BuildingPOJO;
import ca.edumedia.griffis.recyclerhttpdialog.services.MyService;
import ca.edumedia.griffis.recyclerhttpdialog.utils.NetworkHelper;
import ca.edumedia.griffis.recyclerhttpdialog.utils.RequestPackage;

//NOTE: FragmentActivity instead of AppCompatActivity
public class MainActivity extends AppCompatActivity {
//public class MainActivity extends FragmentActivity{


    private static final String JSON_URL = "https://doors-open-ottawa.mybluemix.net/buildings/secure";
    private static final int    NO_SELECTED_CATEGORY_ID = -1;
    private static final String TAG = "TAG";
    private MyAdapter mBuildingAdapter;
    private ArrayList<BuildingPOJO> mBuildingsList;
    private RecyclerView mRecyclerView;
    //For the Dialog
    //public static FragmentManager mFManager;

    private BroadcastReceiver mBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(MyService.MY_SERVICE_PAYLOAD)) {
                BuildingPOJO[] buildingsArray = (BuildingPOJO[]) intent.getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
                Toast.makeText(MainActivity.this,
                        "Received " + buildingsArray.length + " buildings from service",
                        Toast.LENGTH_SHORT).show();
                //Save the Buildings as a global member field in a List or ArrayList
                mBuildingsList = new ArrayList<>(Arrays.asList(buildingsArray));

                displayBuildings();
            } else if (intent.hasExtra(MyService.MY_SERVICE_EXCEPTION)) {
                String message = intent.getStringExtra(MyService.MY_SERVICE_EXCEPTION);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.data_list);  //inside activity_main.xml
        //get the adapter for the recyclerview ready
        displayBuildings();

        //create a FragmentManager for the Delete Dialog to be used later from the PostAdapter
        //mFManager = getSupportFragmentManager();

        //register the service
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBR, new IntentFilter(MyService.MY_SERVICE_MESSAGE));
        //try to retrieve the list of buildings
        fetchBuildings(NO_SELECTED_CATEGORY_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBR);
    }

    private void displayBuildings() {
        Log.i(TAG, "displayBuildings: display the building recyclerview");
        if (mBuildingsList != null) {
            mBuildingAdapter = new MyAdapter(this, mBuildingsList);
            mRecyclerView.setAdapter(mBuildingAdapter);
        }else{
            //get the adapter ready for the recyclerview when the data isn't here yet
            BuildingPOJO[] temp = new BuildingPOJO[0];
            //this could be a different method to just add the data
            ArrayList<BuildingPOJO> tempAL = new ArrayList<>(Arrays.asList(temp));
            mBuildingAdapter = new MyAdapter(MainActivity.this, tempAL);
            mRecyclerView.setAdapter( mBuildingAdapter );
        }
    }

    private void fetchBuildings(int selectedCategoryId) {
        if (NetworkHelper.hasNetworkAccess(this)) {
            // prepare requestPackage for Intent
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(JSON_URL);
            //Default Method is HttpMethod.GET
            //add the categoryId param if one was picked...
            //if (selectedCategoryId != NO_SELECTED_CATEGORY_ID) {
                //requestPackage.setParam("categoryId", selectedCategoryId + "");
            //}
            //call on the Service to make the call
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
            //Such sad. No Network
        }
    }


//    @Override
//    public void onFinishDeleteDialog(int inputNum) {
//        //to handle the returned data from the dialog
//
//    }
}

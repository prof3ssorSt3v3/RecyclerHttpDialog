package ca.edumedia.griffis.recyclerhttpdialog.services;


import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import ca.edumedia.griffis.recyclerhttpdialog.models.BuildingPOJO;
import ca.edumedia.griffis.recyclerhttpdialog.utils.HttpHelper;
import ca.edumedia.griffis.recyclerhttpdialog.utils.RequestPackage;
import com.google.gson.Gson;

import java.io.IOException;

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";
    public static final String MY_SERVICE_EXCEPTION = "myServiceException";
    public static final String REQUEST_PACKAGE = "requestPackage";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        RequestPackage requestPackage = intent.getParcelableExtra(REQUEST_PACKAGE);
        Log.i(TAG, "MyService.onHandleIntent: " + requestPackage.getParams().size());
        String response;
        try {
            //the 2nd and 3rd params are for authorization username, password
            response = HttpHelper.downloadUrl(requestPackage, "username goes here", "password goes here");

        } catch (IOException e) {
            e.printStackTrace();
            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(MY_SERVICE_EXCEPTION, e.getMessage());
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            return;
        }

        Gson gson = new Gson();
        BuildingPOJO[] dataItems = gson.fromJson(response, BuildingPOJO[].class);

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "MyService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "MyService onDestroy");
    }

}

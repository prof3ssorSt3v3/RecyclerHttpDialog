package ca.edumedia.griffis.recyclerhttpdialog.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by griffis on 2017-12-16.
 */

public class HttpHelper {


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //Constructor without username and password
    public static String downloadUrl(RequestPackage requestPackage) throws IOException {
        return downloadUrl(requestPackage, "", "");
    }


    //Constructor WITH username and password
    public static String downloadUrl(RequestPackage requestPackage, @NonNull String user, @NonNull String pass) throws IOException {

        //create the OkHttpClient object
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();


        //Add Authorization if provided
        if ( !user.equals("") && !pass.equals("") ) {
            String credentials = Credentials.basic(user, pass);
            requestBuilder.header("Authorization", credentials);
        }

        //Find the endpoint
        String address = requestPackage.getEndpoint();

        String encodedParams = requestPackage.getEncodedParams();
        Log.i(TAG, "downloadUrl: " + encodedParams);

        //Handle any GET params
        if (requestPackage.getMethod().equals("GET")  && encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        //Handle any POST or PUT params as a JSON String to send to the server


        if ( requestPackage.getMethod().equals("POST") || requestPackage.getMethod().equals("PUT") ){

            JSONObject json = new JSONObject(requestPackage.getParams());
            String params = json.toString();

             if(params.length() > 0) {
                 //add the JSON object to the OkHttpClient requestBuilder
                 RequestBody body = RequestBody.create(JSON, params);
                 requestBuilder.post(body);
             }
        }


        //Add the endpoint and build the request to send
        requestBuilder.url(address);
        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute(); //synchronous request

        //Do something with the response...
        if (response.isSuccessful()) {
            return response.body().string();    //NOT toString
        } else {
            throw new IOException("Exception: Response code " + response.code());
        }
    }


}

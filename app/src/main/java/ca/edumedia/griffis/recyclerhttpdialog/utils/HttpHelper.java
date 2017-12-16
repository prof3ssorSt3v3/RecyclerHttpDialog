package ca.edumedia.griffis.recyclerhttpdialog.utils;

import android.support.annotation.Nullable;
import android.util.Base64;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by griffis on 2017-12-16.
 */

public class HttpHelper {


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String downloadUrl(RequestPackage requestPackage, @Nullable String user, @Nullable String pass) throws IOException {

        //create the OkHttpClient object
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();

        //Add Authorization if provided
        if (user != null && pass != null) {
            InputStream is = null;
            byte[] loginBytes = (user + ":" + pass).getBytes();
            StringBuilder loginHeader = new StringBuilder( )
                    .append("Basic ")
                    .append( Base64.encodeToString( loginBytes, Base64.DEFAULT ) );
            String authorization = loginHeader.toString();
            requestBuilder.addHeader("Authorization", authorization);
        }

        //Find the endpoint
        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        //Handle any GET params
        if (requestPackage.getMethod() == HttpMethod.GET  && encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        //Handle any POST or PUT params as a JSON String to send to the server
        JSONObject json = new JSONObject(requestPackage.getParams());
        String params = json.toString();

        if ( (requestPackage.getMethod() == HttpMethod.POST ||
                requestPackage.getMethod() == HttpMethod.PUT) &&
                params.length() > 0) {

            //add the JSON object to the OkHttpClient requestBuilder
            RequestBody body = RequestBody.create(JSON, params);
            requestBuilder.post(body);
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

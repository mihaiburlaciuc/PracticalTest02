package ro.pub.cs.systems.eim.practicaltest02;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class RequestData extends Thread {
    boolean running = true;
    Context context;
    ServerThread serverThread;

    String TAG = "DEBUGAPP_REQ_DATA";

    public RequestData(Context context, ServerThread serverThread) {
        this.context = context;
        this.serverThread = serverThread;
    }

    @Override
    public void run() {
        while (running) {
            volleyRequest();
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void volleyRequest() {

        RequestQueue queue = Volley.newRequestQueue(context);
//        String url ="http://www.google.com";
        String url = "https://api.coindesk.com/v1/bpi/currentprice/EUR.json";
        Log.d(TAG, "Started volley request to " + url );

        // Request a string response ---- use StringRequest from the provided URL.

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", "AbCdEfGh123456");
        JSONObject jsonObjectRequest = new JSONObject(params);

        // Json request
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonObjectRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
//                        volleyResponseTextView.setText("Response is: "+ response.toString());
                        Log.d(TAG, "response is " + response.toString());

                        try {
                            JSONObject jsonObject = response.getJSONObject("bpi");

                            JSONObject usdObj = jsonObject.getJSONObject("USD");
                            String usdRate = usdObj.getString("rate");

                            JSONObject eurObj = jsonObject.getJSONObject("EUR");
                            String eurRate = eurObj.getString("rate");

                            JSONObject timeObj = response.getJSONObject("time");
                            String updatedTime = timeObj.getString("updated");



                            SavedData savedData = new SavedData(eurRate, usdRate, updatedTime);
                            serverThread.setSavedData(savedData);
                            Log.d(TAG, "setting saved data to " + savedData.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, " JSON ERRROR !!!" + e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                volleyResponseTextView.setText("That didn't work!");
                Log.d(TAG, "error is " + error.toString());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
    }
}

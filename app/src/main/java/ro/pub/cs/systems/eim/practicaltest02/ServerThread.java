package ro.pub.cs.systems.eim.practicaltest02;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {
    int serverPort;

    private boolean isRunning;
    String TAG = "DEBUGAPP_SERVER";

    String eur = "";
    String usd = "";
    String time = "";

    SavedData savedData = new SavedData(eur, usd, time);

    private ServerSocket serverSocket;
    Context context;

    public ServerThread(int serverPort, Context context) {
        this.serverPort = serverPort;
        this.context =context;
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(TAG, "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e(TAG, "An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        }
        Log.v(TAG, "stopServer() method was invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort, 50, InetAddress.getByName("0.0.0.0"));

            // Get data every minute
            RequestData requestData = new RequestData(context, this);
            requestData.start();

            while (isRunning) {
                Socket socket = serverSocket.accept();
                Log.v(TAG, "accept()-ed: " + socket.getInetAddress());

                if (socket != null) {
                    HandleClientThread handleClientThread = new HandleClientThread(socket, this);
                    handleClientThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e(TAG, "An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        }
    }

    synchronized void setData(String eur, String usd, String time) {
        this.eur = eur;
        this.usd = usd;
        this.time = time;
    }

    public synchronized SavedData getSavedData() {
        return savedData;
    }

    public synchronized void setSavedData(SavedData savedData) {
        this.savedData = savedData;
    }
}
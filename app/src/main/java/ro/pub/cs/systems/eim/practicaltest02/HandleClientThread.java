package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;



public class HandleClientThread extends Thread {
    private Socket socket;

    ServerThread serverThread;

    public HandleClientThread(Socket socket, ServerThread serverThread) {
        this.socket = socket;
        this.serverThread = serverThread;
    }

    String TAG = "DEBUGAPP_HANDLE_CLIENT";
    @Override
    public void run() {
        // Init reader to read CLIENT REQUEST
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            // Init writer to write SERVER RESPONSE
            PrintWriter printWriter = Utilities.getWriter(socket);

            String rate = bufferedReader.readLine();
            Log.e(TAG, "[COMMUNICATION THREAD]Rate received is " + rate);


//            if (!"eur".equals(rate) || !"usd".equals(rate) || rate == null) {
//                Log.e(TAG, "[COMMUNICATION THREAD] Error receiving parameters from client");
//                return;
//            }

            SavedData savedData = serverThread.getSavedData();
            Log.d(TAG, "saved data obtained is " + savedData.toString());

            String value = rate.equals("eur") ? savedData.eur : savedData.usd;

            Log.d(TAG, "sending to client value " + value);

            printWriter.println(value);
            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}

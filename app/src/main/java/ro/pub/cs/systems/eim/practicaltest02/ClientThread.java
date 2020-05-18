package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    String TAG = "DEBUGAPP_CLIENT_THREAD";

    private String address;
    private int port;
    private String rate;
    private TextView resultFromServerTextView;

    private Socket socket;

    public ClientThread(TextView resultFromServerTextView, String address, int port, String rate) {
        this.address = address;
        this.port = port;
        this.rate = rate;
        this.resultFromServerTextView = resultFromServerTextView;
    }

    @Override
    public void run() {
        try {
            // Open socket
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }

            // Get writer to write data to send to server
            PrintWriter printWriter = Utilities.getWriter(socket);

            // Get reader to read data received from server
            BufferedReader bufferedReader = Utilities.getReader(socket);

            if (bufferedReader == null || printWriter == null) {
                Log.e(TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            // Send to server

            Log.e(TAG, "[CLIENT THREAD] Printing rate " + rate);
            // Write first line - rate
            printWriter.println(rate);
            printWriter.flush();

            String responseFromServer;

            // Get from server
            while ((responseFromServer = bufferedReader.readLine()) != null) {
                final String serverResponseData = responseFromServer;
                resultFromServerTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        resultFromServerTextView.setText(serverResponseData);
                    }
                });
            }

        } catch (IOException ioException) {
            Log.e(TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    ioException.printStackTrace();
                }
            }
        }
    }
}

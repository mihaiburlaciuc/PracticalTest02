package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // MODEL
    private ServerThread serverThread;
    private ClientThread clientThread = null;

    String TAG = "DEBUGAPP_MAIN";

    EditText serverPortEditText;
    EditText address;
    EditText clientPortEditText;
    EditText rateEditText;

    Button startServerButton;
    Button getButton;
    TextView resultFromServerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverPortEditText = findViewById(R.id.portEditText);
        address = findViewById(R.id.addressEditText);
        clientPortEditText = findViewById(R.id.portClientEditText);
        rateEditText = findViewById(R.id.rateEditText);

        startServerButton = findViewById(R.id.startButton);
        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServer();
            }
        });

        resultFromServerTextView = findViewById(R.id.resultFromServerTextView);
        getButton = findViewById(R.id.getButton);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToServer();
            }
        });
    }

    private void startServer() {
        int port = getPort();

        serverThread = new ServerThread(port, this);
        serverThread.startServer();
        Log.v(TAG, "Starting server... on port: " + port);
    }

    int getPort() {
        return Integer.parseInt(serverPortEditText.getText().toString());
    }

    private void stopServer() {
        serverThread.stopServer();
        Log.v(TAG, "Stopping server...");
    }

    public void sendRequestToServer() {
        // Check server is running
        if (serverThread == null || !serverThread.isAlive()) {
            Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Port & address
        String clientAddress = address.getText().toString();
        int clientPort = Integer.parseInt(clientPortEditText.getText().toString());
        if (clientAddress == null
                || clientAddress.isEmpty()
                || clientPortEditText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get client request data from UI
        String rate = rateEditText.getText().toString();

        if (!("eur".equals(rate) || "usd".equals(rate))) {
            Toast.makeText(this, "enter correct rate form", Toast.LENGTH_LONG).show();
        }


        Log.d(TAG, "rate is |" + rate);

        // Start client thread to execute request
        clientThread = new ClientThread(
                resultFromServerTextView,
                clientAddress,
                clientPort,
                rate);
        clientThread.start();
    }


    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }
}

package ro.pub.cs.systems.eim.practical2test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int serverPort = 8040;
    private static final int clientPort = 8040;
    private static final String clientAddress = "127.0.0.1";

    private EditText pokemonNameEditText = null;
    private TextView pokemonStatsTextView = null;
    private Button pokemonStatsButton = null;
    private Button clientConnectButton = null;
    private Button startServerButton = null;
    private ImageView pokemonImageView = null;

    private ServerThread serverThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_main);

        pokemonNameEditText = (EditText) findViewById(R.id.pokemonNameEditText);
        pokemonStatsTextView = (TextView) findViewById(R.id.pokemonStatsTextView);
        pokemonStatsButton = (Button) findViewById(R.id.pokemonStatsButton);
        clientConnectButton = (Button) findViewById(R.id.clientConnectButton);
        startServerButton = (Button) findViewById(R.id.startServerButton);
        pokemonImageView = (ImageView) findViewById(R.id.pokemonImageView);

        pokemonStatsButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                String clientAddress = "127.0.0.1";
                String clientPort = "8040";
                if (clientAddress.isEmpty() || clientPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serverThread == null || !serverThread.isAlive()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pokemonName = pokemonNameEditText.getText().toString();
                if (pokemonName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                pokemonStatsTextView.setText(Constants.EMPTY_STRING);

                ClientThread clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), pokemonName, pokemonStatsTextView, pokemonImageView);
                clientThread.start();
            }
        }));

        startServerButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                serverThread = new ServerThread(8040);
                if (serverThread.getServerSocket() == null) {
                    Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                    return;
                }
                serverThread.start();
            }
        }));
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}

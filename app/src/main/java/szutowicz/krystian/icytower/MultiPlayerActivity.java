package szutowicz.krystian.icytower;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.UUID;

import szutowicz.krystian.icytower.Bluetooth.Client;
import szutowicz.krystian.icytower.Bluetooth.Connection;
import szutowicz.krystian.icytower.Bluetooth.Host;
import szutowicz.krystian.icytower.Views.Game;

public class MultiPlayerActivity extends Activity {

    private Game game;
    private Host host;
    private Client client;
    private UUID uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.multi_player_activity);
        uuid = UUID.fromString("49cd4af4-ed50-11e7-8c3f-9a214cf093ae");

        findViewById(R.id.bluetooth_host_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.bluetooth_lobby).setVisibility(View.GONE);
                findViewById(R.id.bluetooth_host).setVisibility(View.VISIBLE);

                host=new Host(MultiPlayerActivity.this, uuid);
            }
        });
        findViewById(R.id.bluetooth_client_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.bluetooth_lobby).setVisibility(View.GONE);
                findViewById(R.id.bluetooth_client).setVisibility(View.VISIBLE);

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(broadcastReceiver, filter);
                client = new Client(MultiPlayerActivity.this, uuid);
            }
        });
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                client.addDevice(bluetoothDevice);
            }
        }
    };

    public void beginConnection(BluetoothSocket bluetoothSocket){
        game = new Game(this, new Connection(bluetoothSocket));
        host = null ;
        client = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1){
            if(resultCode==Activity.RESULT_CANCELED) {
                startActivity(new Intent(this, MainMenuActivity.class));
                Toast.makeText(this, "Bluetooth is required, try again",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }

    @Override
    protected void onDestroy(){
        if(client!=null){
            client.cancel();
            unregisterReceiver(broadcastReceiver);
        }
        if(host!=null)
            host.cancel();
        super.onDestroy();
    }
}

package szutowicz.krystian.icytower.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import szutowicz.krystian.icytower.R;

public class Host extends Thread{
    public Activity activity;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket bluetoothServerSocket;
    private UUID uuid;

    public TextView status;

    public Host(Activity activity, UUID uuid){
        this.activity=activity;
        this.uuid = uuid;
        initUIComponents();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);
        }
        else
            status.setText("Enabled");
    }

    @Override
    public void run(){
        BluetoothSocket bluetoothSocket= null;

        while(true){
            try{
                bluetoothSocket = bluetoothServerSocket.accept();
            }
            catch (IOException e){
                Log.d("Host", "Hosting canceled");
                break;
            }

            if(bluetoothSocket != null){
                try{
                    bluetoothServerSocket.close();
                }
                catch (IOException e){
                    Log.d("Host", "Closing Host failed");
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Connected",
                                Toast.LENGTH_LONG).show();
                    }
                });
                //TODO start connection
                break;
            }
        }
    }

    public void cancel(){
        if(bluetoothServerSocket!=null)
            try{
                bluetoothServerSocket.close();
            }
            catch (IOException e){
                Log.d("Host", "Closing Host failed");
            }
    }

    private void initUIComponents(){
        status = (TextView)activity.findViewById(R.id.bluetooth_status);
        activity.findViewById(R.id.bluetooth_visible).setOnClickListener(new DiscoverEnableButtonListener());
    }

    private class DiscoverEnableButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            activity.findViewById(R.id.bluetooth_visible).setOnClickListener(null);
            if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                Intent discoverableIntent =
                        new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                activity.startActivity(discoverableIntent);
            }
            try{
                bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("IcyTower", uuid);
            }
            catch (IOException e){
                Log.d("Host", "Server socked exception");
            }
            start();
        }
    }
}
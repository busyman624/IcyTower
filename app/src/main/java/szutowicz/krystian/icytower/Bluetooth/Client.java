package szutowicz.krystian.icytower.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import szutowicz.krystian.icytower.R;

public class Client extends Thread{

    public Activity activity;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private UUID uuid;

    public TextView status;
    private LinearLayout discoveredDevicesLayout;

    public Client(Activity activity, UUID uuid){
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

        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void run(){
        try{
            bluetoothSocket.connect();
        }
        catch (IOException connectException){
            try{
                bluetoothSocket.close();
            }
            catch (IOException closeException){
                Log.d("Client", "Cannot close socker");
            }

            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Connected",
                        Toast.LENGTH_LONG).show();
            }
        });
        //TODO start connection
    }

    public void cancel(){
        if(bluetoothSocket!=null)
            try{
                bluetoothSocket.close();
            }
            catch (IOException closeException){
                Log.d("Client", "Cannot close socker");
            }
    }

    private void initUIComponents(){
        status = (TextView)activity.findViewById(R.id.bluetooth_status);
        discoveredDevicesLayout = (LinearLayout)activity.findViewById(R.id.bluetooth_client);
    }

    public void addDiscoveredDevice(BluetoothDevice bluetoothDevice){
        Button connectDevice = new Button(activity);
        connectDevice.setText(bluetoothDevice.getName());
        connectDevice.setOnClickListener(new ConnectButtonListener(bluetoothDevice));
        discoveredDevicesLayout.addView(connectDevice, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    private class ConnectButtonListener implements View.OnClickListener{

        BluetoothDevice bluetoothDevice;

        ConnectButtonListener(BluetoothDevice bluetoothDevice){
            this.bluetoothDevice = bluetoothDevice;
        }

        @Override
        public void onClick(View view) {
            for(int i = 0; i< discoveredDevicesLayout.getChildCount(); i++){
                discoveredDevicesLayout.getChildAt(i).setOnClickListener(null);
            }
            bluetoothAdapter.cancelDiscovery();
            try{
                bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            }
            catch(IOException e){
                Log.d("Client", "Cannot create socket");
            }
            start();
        }
    }
}

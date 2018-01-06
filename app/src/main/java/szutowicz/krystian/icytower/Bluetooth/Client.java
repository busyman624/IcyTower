package szutowicz.krystian.icytower.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import szutowicz.krystian.icytower.MainMenuActivity;
import szutowicz.krystian.icytower.MultiPlayerActivity;
import szutowicz.krystian.icytower.R;

public class Client extends Thread{

    public MultiPlayerActivity activity;
    private BluetoothAdapter bluetoothAdapter;
    public BluetoothSocket bluetoothSocket;
    private UUID uuid;

    private LinearLayout discoveredDevicesLayout;

    public Client(MultiPlayerActivity activity, UUID uuid){
        this.activity=activity;
        this.uuid = uuid;
        initUIComponents();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);
        }

        removeBondedDevices();
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

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Connection failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            activity.startActivity(new Intent(activity, MainMenuActivity.class));
            activity.finish();
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Connected",
                        Toast.LENGTH_LONG).show();
                activity.beginConnection(bluetoothSocket);
            }
        });
    }

    public void cancel(){
        if(bluetoothSocket!=null)
            try{
                bluetoothSocket.close();
            }
            catch (IOException closeException){
                Log.d("Client", "Cannot close socker");
            }
           if(bluetoothAdapter.isDiscovering())
               bluetoothAdapter.cancelDiscovery();
    }

    private void initUIComponents(){
        discoveredDevicesLayout = (LinearLayout)activity.findViewById(R.id.bluetooth_devices);
        activity.findViewById(R.id.bluetooth_search).setOnClickListener(new SearchButtonListener());
        activity.findViewById(R.id.bluetooth_search_stop).setOnClickListener(new StopButtonListener());
        activity.findViewById(R.id.bluetooth_main_menu).setOnClickListener(new MainMenuButtonListener());
    }

    private void removeBondedDevices(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                try {
                        device.getClass().getMethod("removeBond", (Class[]) null)
                                .invoke(device, (Object[]) null);
                } catch (Exception e) {
                    Log.e("fail", e.getMessage());
                }
            }
        }
    }

    public void addDevice(BluetoothDevice bluetoothDevice){
        if(bluetoothDevice.getName()!=null){
            Button connectDevice = new Button(activity);
            connectDevice.setText(bluetoothDevice.getName());
            connectDevice.setOnClickListener(new ConnectButtonListener(bluetoothDevice));
            discoveredDevicesLayout.addView(connectDevice, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private class SearchButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            bluetoothAdapter.startDiscovery();
            activity.findViewById(R.id.bluetooth_search_progress_bar).setVisibility(View.VISIBLE);
        }
    }

    private class StopButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
                activity.findViewById(R.id.bluetooth_search_progress_bar).setVisibility(View.INVISIBLE);
            }
        }
    }

    private class MainMenuButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            activity.startActivity(new Intent(activity, MainMenuActivity.class));
            activity.finish();
        }
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
            activity.findViewById(R.id.bluetooth_search_progress_bar).setVisibility(View.INVISIBLE);
            start();
        }
    }
}

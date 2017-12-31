package szutowicz.krystian.icytower.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Connection extends Thread{

    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Message lastMessage;

    public Connection(BluetoothSocket bluetoothSocket){
        this.bluetoothSocket=bluetoothSocket;

        try{
            inputStream=bluetoothSocket.getInputStream();
        }
        catch (IOException e){
            Log.d("Connection", "Cannot get Input Stream");
        }
        try{
            outputStream=bluetoothSocket.getOutputStream();
        }
        catch (IOException e){
            Log.d("Connection", "Cannot get Output Stream");
        }
    }

    @Override
    public void run(){
        byte[] buffer = new byte[1024];
        int numBytes;

        while(true){
            try{
                numBytes = inputStream.read(buffer);
                lastMessage = new Message(new String(buffer, 0, numBytes));
            }
            catch(IOException e){
                Log.d("Connection", "Input stream disconnected");
                break;
            }
        }
    }

    public void write(Message message){
        byte[] buffer = message.fullMessage.getBytes();
        try{
            outputStream.write(buffer);
        }
        catch (IOException e){  //TODO disconnect after some time?
            Log.d("Connection", "Outputstream disconnected");
        }
    }

    public void cancel(){
        try{
            bluetoothSocket.close();
        }
        catch (IOException closeException){
            Log.d("Client", "Cannot close socker");
        }
    }

    public Message getLastMessage(){
        return lastMessage;
    }
}

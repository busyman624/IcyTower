package szutowicz.krystian.icytower.Bluetooth;

public class Message {

    public boolean start;
    public int x;
    public int y;

    public Message(String message){
        String[] splittedMessage = message.split(",");
        if(Integer.parseInt(splittedMessage[0])==0)
            start=false;
        else
            start=true;
        x=Integer.parseInt(splittedMessage[1]);
        y=Integer.parseInt(splittedMessage[2]);
    }
}

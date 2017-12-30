package szutowicz.krystian.icytower.Bluetooth;

public class Message {

    public String fullMessage;
    public boolean start;
    public int x;
    public int y;

    Message(String fullMessage){
        this.fullMessage=fullMessage;
        String[] splittedMessage = fullMessage.split(",");
        if(Integer.parseInt(splittedMessage[0])==0)
            start=false;
        else
            start=true;
        x=Integer.parseInt(splittedMessage[1]);
        y=Integer.parseInt(splittedMessage[2]);
    }

    public Message(boolean start, int x, int y){
        this.start=start;
        this.x=x;
        this.y=y;
        if(!start)
            fullMessage="0,"+x+","+y;
        else
            fullMessage="1,"+x+","+y;
    }
}

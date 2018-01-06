package szutowicz.krystian.icytower.Bluetooth;

public class Message {

    String fullMessage;
    public boolean isValid;
    public boolean start;
    public float x;
    public int y;
    public int maxFloor;

    Message(){
        start=true;
        isValid=false;
    }

    Message(String fullMessage){
        this.fullMessage=fullMessage;
        isValid = parseMessage();
    }

    public Message(boolean start, float x, int y, int maxFloor){
        this.start=start;
        this.x=x;
        this.y=y;
        this.maxFloor=maxFloor;
        if(!start)
            fullMessage="0,"+x+","+y+","+maxFloor;
        else
            fullMessage="1,"+x+","+y+","+maxFloor;
    }

    private boolean parseMessage(){
        String[] splittedMessage = fullMessage.split(",");
        for(int i=1; i<splittedMessage.length; i++){
            if(!canParseNumber(splittedMessage[i]))
                return false;
        }
        if(Integer.parseInt(splittedMessage[0])==0)
            start=false;
        else
            start=true;
        x=Float.parseFloat(splittedMessage[1]);
        y=Integer.parseInt(splittedMessage[2]);
        maxFloor = Integer.parseInt(splittedMessage[3]);
        return true;
    }

    private boolean canParseNumber(String msg){
        try{
            Float.parseFloat(msg);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }
}

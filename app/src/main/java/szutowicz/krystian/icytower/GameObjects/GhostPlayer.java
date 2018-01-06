package szutowicz.krystian.icytower.GameObjects;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import szutowicz.krystian.icytower.Bluetooth.Message;
import szutowicz.krystian.icytower.MainMenuActivity;
import szutowicz.krystian.icytower.SinglePlayerActivity;

public class GhostPlayer extends GameObject{

    private Paint paint;
    private int maxFloor;
    private int borderWidth;

    public GhostPlayer(Bitmap image, int borderWidth){
        this.image=image;
        width=image.getWidth();
        height=image.getHeight();
        this.borderWidth=borderWidth;
        x= MainMenuActivity.displaySize.x/2;
        y= MainMenuActivity.displaySize.y*3/4-height-5;
        maxFloor=0;
        paint = new Paint();
        paint.setAlpha(60);
    }

    public void update(Message lastMessage, int playerY, int playerTotalY){
        if(lastMessage.isValid){
            x=(int)(borderWidth+(MainMenuActivity.displaySize.x - 2* borderWidth)*lastMessage.x);
            y=playerY-playerTotalY+lastMessage.y;
            maxFloor=lastMessage.maxFloor;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, paint);
    }

    public int getMaxFloor() { return maxFloor; }
}

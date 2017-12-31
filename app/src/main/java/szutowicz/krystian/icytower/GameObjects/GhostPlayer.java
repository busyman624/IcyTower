package szutowicz.krystian.icytower.GameObjects;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import szutowicz.krystian.icytower.Bluetooth.Message;
import szutowicz.krystian.icytower.MainMenuActivity;
import szutowicz.krystian.icytower.SinglePlayerActivity;

public class GhostPlayer extends GameObject{

    private Paint paint;

    public GhostPlayer(Bitmap image){
        this.image=image;
        width=image.getWidth();
        height=image.getHeight();
        x= MainMenuActivity.displaySize.x/2;
        y= MainMenuActivity.displaySize.y*3/4-height-5;
        paint = new Paint();
        paint.setAlpha(60);
    }

    public void update(Message lastMessage, int playerY, int playerTotalY){
        if(lastMessage!=null){
            x=lastMessage.x;
            y=playerY-playerTotalY+lastMessage.y;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, paint);
    }
}

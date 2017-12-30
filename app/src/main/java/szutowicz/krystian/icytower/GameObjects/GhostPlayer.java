package szutowicz.krystian.icytower.GameObjects;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import szutowicz.krystian.icytower.Bluetooth.Message;
import szutowicz.krystian.icytower.SinglePlayerActivity;

public class GhostPlayer extends GameObject{

    private Paint paint;

    public GhostPlayer(Bitmap image){
        this.image=image;
        x= SinglePlayerActivity.displaySize.x/2;
        y= SinglePlayerActivity.displaySize.y*3/4-height-5;
        paint = new Paint();
        paint.setAlpha(60);
    }

    public void update(Message lastMessage){
        if(lastMessage!=null){
            x=lastMessage.x;
            y=lastMessage.y;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, paint);
    }
}

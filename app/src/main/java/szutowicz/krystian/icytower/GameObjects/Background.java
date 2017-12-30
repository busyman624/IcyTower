package szutowicz.krystian.icytower.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import szutowicz.krystian.icytower.SinglePlayerActivity;

public class Background {

    private Bitmap image;
    private int y;

    public Background(Bitmap image){
        this.image=Bitmap.createScaledBitmap(image, SinglePlayerActivity.displaySize.x, SinglePlayerActivity.displaySize.y, true);
    }

    public void update(int dy){
        y=y+dy;

        if(y> SinglePlayerActivity.displaySize.y){
            y=0;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, 0, y, null);
        if(y>0){
            canvas.drawBitmap(image, 0, y- SinglePlayerActivity.displaySize.y, null);
        }
    }
}

package szutowicz.krystian.icytower.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import szutowicz.krystian.icytower.GameActivity;

public class Background {

    private Bitmap image;
    private int y;

    public Background(Bitmap image){
        this.image=Bitmap.createScaledBitmap(image, GameActivity.displaySize.x, GameActivity.displaySize.y, true);
    }

    public void update(int dy){
        y=y+dy;

        if(y>GameActivity.displaySize.y){
            y=0;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, 0, y, null);
        if(y>0){
            canvas.drawBitmap(image, 0, y-GameActivity.displaySize.y, null);
        }
    }
}

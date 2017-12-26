package szutowicz.krystian.icytower;

import android.graphics.Bitmap;
import android.graphics.Canvas;

class Background {

    private Bitmap image;
    private int y;

    Background(Bitmap image){
        this.image=image;
    }

    void update(int dy){
        y=y+dy;

        if(y>GameActivity.displaySize.y){
            y=0;
        }
    }

    void draw(Canvas canvas){
        canvas.drawBitmap(image, 0, y, null);
        if(y>0){
            canvas.drawBitmap(image, 0, y-GameActivity.displaySize.y, null);
        }
    }
}

package szutowicz.krystian.icytower;

import android.graphics.Bitmap;
import android.graphics.Canvas;

class Border extends GameObject {

    Border(Bitmap image, int x, int y){
        this.image=image;
        this.x=x;
        this.y=y;
        width=image.getWidth();
        height=image.getHeight();
    }

    void update(int dy){
        y=y+dy;
    }

    void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }
}

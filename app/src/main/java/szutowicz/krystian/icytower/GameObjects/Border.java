package szutowicz.krystian.icytower.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Border extends GameObject {

    public Border(Bitmap image, int x, int y){
        this.image=image;
        this.x=x;
        this.y=y;
        width=image.getWidth();
        height=image.getHeight();
    }

    public void update(int dy){
        y=y+dy;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }
}

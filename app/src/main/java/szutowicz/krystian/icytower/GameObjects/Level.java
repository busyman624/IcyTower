package szutowicz.krystian.icytower.GameObjects;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

import szutowicz.krystian.icytower.SinglePlayerActivity;

public class Level extends GameObject {

    private int number;

    public Level(Bitmap image, int y, int number, int borderWidth){
        this.number=number;
        this.y=y;
        this.image= Bitmap.createScaledBitmap(image, scale(image, borderWidth), image.getHeight()/2, true);
        width=this.image.getWidth();
        height=this.image.getHeight();
        if(number==0 || number%50==0){
            x= borderWidth;
        }
        else{
            Random random = new Random();
            x= random.nextInt(SinglePlayerActivity.displaySize.x - 2 * borderWidth - width)
                    +borderWidth;
        }
    }

    public void update(int dy){
        y=y+dy;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

    private int scale(Bitmap image, int borderWidth){
        if(number==0 || number%50==0){
            return SinglePlayerActivity.displaySize.x - 2 * borderWidth;
        }
        else{
            return image.getWidth()/(3+number/100);
        }
    }

    @Override
    public Rect getRect(){
        return new Rect(x, y, x+width, y);
    }

    public int getNumber(){
        return number;
    }
}

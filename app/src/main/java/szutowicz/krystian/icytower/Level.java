package szutowicz.krystian.icytower;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

class Level extends GameObject {

    private int number;

    Level(Bitmap image, int y, int number){
        this.number=number;
        this.y=y;
        this.image= Bitmap.createScaledBitmap(image, scale(image), image.getHeight()/2, true);
        width=this.image.getWidth();
        height=this.image.getHeight();
        if(number==0 || number%50==0){
            x=Game.images[1].getWidth();
        }
        else{
            Random random = new Random();
            x= random.nextInt(GameActivity.displaySize.x - 2 * Game.images[1].getWidth() - width)
                    +Game.images[1].getWidth();
        }
    }

    void update(int dy){
        y=y+dy;
    }

    void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

    private int scale(Bitmap image){
        if(number==0 || number%50==0){
            return GameActivity.displaySize.x - 2 * Game.images[1].getWidth();
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

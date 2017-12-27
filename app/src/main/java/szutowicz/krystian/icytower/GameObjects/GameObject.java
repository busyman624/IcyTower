package szutowicz.krystian.icytower.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Rect;

abstract class GameObject {

    int x;
    int y;
    int dy;
    int width;
    int height;
    Bitmap image;

    public int getY(){return y;  }
    public int getDy(){return dy;  }
    public int getWidth(){return width;  }
    public int getHeight(){return height;  }
    public Rect getRect(){return new Rect(x, y, x+width, y+height);  }
}

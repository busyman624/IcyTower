package szutowicz.krystian.icytower;

import android.graphics.Bitmap;
import android.graphics.Rect;

abstract class GameObject {

    int x;
    int y;
    int dy;
    int width;
    int height;
    Bitmap image;

    int getY(){return y;  }
    int getDy(){return dy;  }
    int getWidth(){return width;  }
    int getHeight(){return height;  }
    Rect getRect(){return new Rect(x, y, x+width, y+height);  }
}

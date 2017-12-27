package szutowicz.krystian.icytower;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import szutowicz.krystian.icytower.Views.Game;

public class GameActivity extends Activity{

    public static Point displaySize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        displaySize=new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);

        setContentView(R.layout.game_activity);
        new Game(this);
    }
}

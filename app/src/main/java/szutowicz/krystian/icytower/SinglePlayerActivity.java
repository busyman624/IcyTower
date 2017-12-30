package szutowicz.krystian.icytower;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import szutowicz.krystian.icytower.Views.Game;

public class SinglePlayerActivity extends Activity{

    public static Point displaySize; //TODO move to main menu
    public Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        displaySize=new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);

        setContentView(R.layout.game_activity);
        game = new Game(this);
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }
}

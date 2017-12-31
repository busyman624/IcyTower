package szutowicz.krystian.icytower;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainMenuActivity extends Activity {

    public static Point displaySize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.main_menu_activity);

        displaySize=new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);

        findViewById(R.id.main_single).setOnClickListener(new SingleButtonListener());
        findViewById(R.id.main_multi).setOnClickListener(new MultiButtonListener());
        findViewById(R.id.main_exit).setOnClickListener(new ExitButtonListener());
    }

    private class SingleButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainMenuActivity.this, SinglePlayerActivity.class));
            finish();
        }
    }

    private class MultiButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainMenuActivity.this, MultiPlayerActivity.class));
            finish();
        }
    }

    private class ExitButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            finish();
        }
    }
}

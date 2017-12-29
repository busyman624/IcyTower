package szutowicz.krystian.icytower;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main_menu_activity);

        findViewById(R.id.main_start).setOnClickListener(new StartButtonListener());
        findViewById(R.id.main_exit).setOnClickListener(new ExitButtonListener());
    }

    private class StartButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainMenuActivity.this, GameActivity.class));
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

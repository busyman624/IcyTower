package szutowicz.krystian.icytower.Views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.LinearLayout;

import szutowicz.krystian.icytower.MainMenuActivity;
import szutowicz.krystian.icytower.R;

class PauseMenu {

    private Game game;
    private LinearLayout layout;

    PauseMenu(Game game) {
        this.game=game;
        LayoutInflater layoutInflater = (LayoutInflater) game.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) layoutInflater.inflate(R.layout.pause_menu, null);
        layout.findViewById(R.id.pause_restart).setOnClickListener(new RestartButtonListener());
        layout.findViewById(R.id.pause_resume).setOnClickListener(new ResumeButtonListener());
        layout.findViewById(R.id.pause_exit).setOnClickListener(new ExitButtonListener());
    }

    LinearLayout getLayout(){
        return layout;
    }

    private class RestartButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ((ViewManager)layout.getParent()).removeView(layout);
            game.gameThread.setRunning(false);
            game.startNew();
        }
    }

    private class ResumeButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            ((ViewManager)layout.getParent()).removeView(layout);
            game.setPaused(false);
        }
    }

    private class ExitButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            game.activity.startActivity(new Intent(game.activity, MainMenuActivity.class));
            game.activity.finish();
        }
    }
}

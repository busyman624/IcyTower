package szutowicz.krystian.icytower.Views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import szutowicz.krystian.icytower.MainMenuActivity;
import szutowicz.krystian.icytower.R;

class EndMenu{

    private Game game;
    private LinearLayout layout;
    private TextView textFloor;

    EndMenu(Game game) {
        this.game=game;
        LayoutInflater layoutInflater = (LayoutInflater) game.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) layoutInflater.inflate(R.layout.end_menu, null);
        textFloor = (TextView)layout.findViewById(R.id.end_floor);
        layout.findViewById(R.id.end_restart).setOnClickListener(new RestartButtonListener());
        layout.findViewById(R.id.end_exit).setOnClickListener(new ExitButtonListener());
    }

    LinearLayout getLayout(int floor){
        textFloor.setText("Floor: " + floor);
        return layout;
    }

    private class RestartButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ((ViewManager)layout.getParent()).removeView(layout);
            game.startNew();
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

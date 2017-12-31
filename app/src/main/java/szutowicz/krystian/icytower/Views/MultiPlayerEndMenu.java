package szutowicz.krystian.icytower.Views;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import szutowicz.krystian.icytower.MainMenuActivity;
import szutowicz.krystian.icytower.R;

public class MultiPlayerEndMenu {

    private Game game;
    private LinearLayout layout;
    private TextView player;
    private TextView foe;

    public MultiPlayerEndMenu(Game game){
        this.game = game;
        LayoutInflater layoutInflater = (LayoutInflater) game.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) layoutInflater.inflate(R.layout.multi_player_end_menu, null);
        player = (TextView) layout.findViewById(R.id.player_floor);
        foe = (TextView) layout.findViewById(R.id.foe_floor);
        layout.findViewById(R.id.multi_exit).setOnClickListener(new MultiPlayerExitButton());
    }

    public LinearLayout getLayout(int playerFloor, boolean isFoeAlive, int foeFloor){
        player.setText("You: " + playerFloor);
        if(isFoeAlive)
            foe.setText("Foe's climbing");
        else
            foe.setText("Foe: " + foeFloor);
        return layout;
    }

    private class MultiPlayerExitButton implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            game.activity.startActivity(new Intent(game.activity, MainMenuActivity.class));
            game.activity.finish();
        }
    }
}

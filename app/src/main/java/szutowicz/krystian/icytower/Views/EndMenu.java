package szutowicz.krystian.icytower.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import szutowicz.krystian.icytower.R;

class EndMenu{

    private LinearLayout layout;
    private TextView textFloor;
    private Button restartButton;
    private Button exitButton;

    public EndMenu(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) layoutInflater.inflate(R.layout.end_menu, null);
        textFloor = (TextView)layout.findViewById(R.id.floor);
        restartButton = (Button)layout.findViewById(R.id.restart);
        exitButton = (Button)layout.findViewById(R.id.exit);
    }

    public LinearLayout getLayout(int floor){
        textFloor.setText("Floor: " + floor);
        return layout;
    }
}

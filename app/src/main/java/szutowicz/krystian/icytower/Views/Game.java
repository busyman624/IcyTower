package szutowicz.krystian.icytower.Views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import szutowicz.krystian.icytower.Bluetooth.Connection;
import szutowicz.krystian.icytower.Bluetooth.Message;
import szutowicz.krystian.icytower.GameObjects.GhostPlayer;
import szutowicz.krystian.icytower.MainMenuActivity;
import szutowicz.krystian.icytower.GameObjects.Background;
import szutowicz.krystian.icytower.GameObjects.Border;
import szutowicz.krystian.icytower.GameObjects.Level;
import szutowicz.krystian.icytower.GameObjects.Player;
import szutowicz.krystian.icytower.GameThread;
import szutowicz.krystian.icytower.R;

public class Game extends SurfaceView implements SurfaceHolder.Callback{

    public Activity activity;
    public GameThread gameThread;
    public Connection connection;
    private SensorManager sensorManager;

    private RelativeLayout gameLayout;
    private SinglePlayerEndMenu singlePlayerEndMenu;
    private MultiPlayerEndMenu multiPlayerEndMenu;
    private PauseMenu pauseMenu;

    private int speed =3;
    private boolean isMultiPlayer;
    private Bitmap[] images;

    private Background background;
    private Player player;
    private GhostPlayer ghostPlayer;
    private ArrayList<Border> leftBorder;
    private ArrayList<Border> rightBorder;
    private ArrayList<Level> levels;

    private boolean paused;

    public Game (Activity activity){
        super(activity);
        this.activity=activity;
        gameLayout=(RelativeLayout)activity.findViewById(R.id.single_player_layout);
        singlePlayerEndMenu =new SinglePlayerEndMenu(this);
        pauseMenu=new PauseMenu(this);
        isMultiPlayer=false;
        init();
    }

    public Game (Activity activity, Connection connection){
        super(activity);
        this.activity=activity;
        this.connection=connection;
        gameLayout=(RelativeLayout)activity.findViewById(R.id.multi_player_layout);
        multiPlayerEndMenu = new MultiPlayerEndMenu(this);
        isMultiPlayer=true;
        init();
    }

    private void init(){
        gameLayout.addView(this);
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        getHolder().addCallback(this);
        setFocusable(true);
        images = new Bitmap[4];
        images[0] = BitmapFactory.decodeResource(getResources(), R.drawable.level);
        images[1] = BitmapFactory.decodeResource(getResources(), R.drawable.border);
        images[2] = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        images[3] = BitmapFactory.decodeResource(getResources(), R.drawable.player);
    }

    void startNew(){
        gameThread =new GameThread(getHolder(), this);
        background =new Background(images[2]);
        if(isMultiPlayer){
            player=new Player(images[3], sensorManager, speed, images[1].getWidth(), connection);
            ghostPlayer = new GhostPlayer(images[3], images[1].getWidth());
            connection.start();
        }
        else
            player= new Player(images[3], sensorManager, speed, images[1].getWidth());
        leftBorder = new ArrayList<>();
        rightBorder = new ArrayList<>();
        levels = new ArrayList<>();
        for(int i = 0; i< MainMenuActivity.displaySize.y/100+1; i++){
            levels.add(new Level(images[0], MainMenuActivity.displaySize.y- MainMenuActivity.displaySize.y/4-i*100, i, images[1].getWidth()));
        }
        for(int i = MainMenuActivity.displaySize.y/images[1].getHeight(); i>-2; i--){
            leftBorder.add(new Border(images[1], 0, i*images[1].getHeight()));
            rightBorder.add(new Border(images[1], MainMenuActivity.displaySize.x - images[1].getWidth(), i * images[1].getHeight()));
        }
        paused=true;
        player.setMaxFloor(0);
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startNew();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry)
        {
            try{
                gameThread.setRunning(false);
                if(isMultiPlayer)
                    connection.cancel();
                gameThread.join();
                retry = false;
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!isMultiPlayer){
                if(!paused && gameLayout.findViewById(R.id.end)==null){
                    gameLayout.addView(pauseMenu.getLayout(), new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    paused=!paused;
                }
                if(gameLayout.findViewById(R.id.pause)==null)
                    paused=!paused;
            } else
                if(!connection.getLastMessage().isValid)
                    paused=!paused;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if(!paused) {
            player.update(speed);
            if(isMultiPlayer)
                ghostPlayer.update(connection.getLastMessage(), player.getY(), player.getTotalY());
            background.update(player.getDy());
            for (int i = 0; i < levels.size(); i++) {
                levels.get(i).update(player.getDy());
                if (levelColission(player, levels.get(i))) {
                    player.setFloorJumpFrame(1);
                    if (player.getMaxFloor() < levels.get(i).getNumber()) {
                        player.setMaxFloor(levels.get(i).getNumber());
                    }
                }
                if (levels.get(i).getY() > MainMenuActivity.displaySize.y + 20) {
                    levels.remove(i);
                    if(levels.get(levels.size() - 1).getNumber() + 1<=1000){
                        levels.add(new Level(images[0],
                                levels.get(levels.size() - 1).getY() - 53 - levels.get(levels.size() - 1).getHeight(),
                                levels.get(levels.size() - 1).getNumber() + 1, images[1].getWidth()));
                    }
                }
            }

            for (int i = 0; i < leftBorder.size(); i++) {
                leftBorder.get(i).update(player.getDy());
                rightBorder.get(i).update(player.getDy());
                if (borderCollistion(player, leftBorder.get(i))
                        || borderCollistion(player, rightBorder.get(i)))
                    player.setWallJumpFrame(1);

                if (leftBorder.get(i).getY() > MainMenuActivity.displaySize.y) {
                    leftBorder.remove(i);
                    rightBorder.remove(i);
                    leftBorder.add(new Border(images[1], 0,
                            leftBorder.get(leftBorder.size() - 1).getY() - leftBorder.get(leftBorder.size() - 1).getHeight()));
                    rightBorder.add(new Border(images[1],
                            MainMenuActivity.displaySize.x - rightBorder.get(rightBorder.size() - 1).getWidth(),
                            rightBorder.get(rightBorder.size() - 1).getY() - rightBorder.get(rightBorder.size() - 1).getHeight()));
                }
            }
            speed=player.getMaxFloor()/100+3;
            if(!keepPlaying()){
                gameThread.setRunning(false);
                if(isMultiPlayer){
                    connection.write(new Message(false, 0, 0, player.getMaxFloor()));
                }
                showEndMenu();
            }
        }
        else
            if(isMultiPlayer && connection.getLastMessage().isValid &&
                    connection.getLastMessage().start)
                paused=!paused;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if(canvas!=null){
            background.draw(canvas);
            for(Border box: leftBorder){
                box.draw(canvas);
            }
            for(Border box: rightBorder){
                box.draw(canvas);
            }
            for(Level level: levels){
                level.draw(canvas);
            }
            if(isMultiPlayer)
                ghostPlayer.draw(canvas);
            player.draw(canvas);
        }
    }

    private void showEndMenu(){
        if(isMultiPlayer)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameLayout.addView(multiPlayerEndMenu.getLayout(player.getMaxFloor(),
                    connection.getLastMessage().start ,ghostPlayer.getMaxFloor()),
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
                }
            });
        else
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameLayout.addView(singlePlayerEndMenu.getLayout(player.getMaxFloor()),
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
    }

    private boolean levelColission(Player player, Level level){
        return Rect.intersects(player.getRect(), level.getRect())
                && player.getY() + player.getHeight() < level.getY() + 20
                && player.getWallJumpFrame() == 0 && player.getFloorJumpFrame() == 0;
    }

    private boolean borderCollistion(Player player, Border border){
        return Rect.intersects(player.getRect(), border.getRect())
                && player.getWallJumpFrame()==0 && player.getCanWallJump();
    }

    private boolean keepPlaying(){
        if(player.getY()> MainMenuActivity.displaySize.y+player.getHeight()
                || player.getMaxFloor() == 1000){
            return false;
        }
        else
            return true;
    }

    public void setPaused(boolean paused){
        this.paused=paused;
    }
}

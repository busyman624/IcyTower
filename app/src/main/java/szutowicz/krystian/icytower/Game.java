package szutowicz.krystian.icytower;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.ArrayList;

class Game extends SurfaceView implements SurfaceHolder.Callback{

    private int speed =3;
    private GameThread gameThread;
    private Background background;
    private Player player;
    private ArrayList<Border> leftBorder;
    private ArrayList<Border> rightBorder;
    private ArrayList<Level> levels;
    private SensorManager sensorManager;

    private int maxFloor;
    private boolean loss;
    private boolean win;
    private boolean restarted=true;

    static Bitmap[] images;

    Game (Context context){
        super(context);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        getHolder().addCallback(this);
        setFocusable(true);
        images = new Bitmap[2];
        images[0] = (BitmapFactory.decodeResource(getResources(), R.drawable.level));
        images[1] = (BitmapFactory.decodeResource(getResources(), R.drawable.border));
    }

    private void init(){
        gameThread =new GameThread(getHolder(), this);
        background =new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        player= new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player), sensorManager, speed);
        leftBorder = new ArrayList<>();
        rightBorder = new ArrayList<>();
        levels = new ArrayList<>();
        for(int i=0; i<GameActivity.displaySize.y/100+1;i++){
            levels.add(new Level(images[0],GameActivity.displaySize.y-GameActivity.displaySize.y/4-i*100, i));
        }
        for(int i=GameActivity.displaySize.y/images[1].getHeight(); i>-2; i--){
            leftBorder.add(new Border(images[1], 0, i*images[1].getHeight()));
            rightBorder.add(new Border(images[1], GameActivity.displaySize.x - images[1].getWidth(), i * images[1].getHeight()));
        }
        win=false;
        loss=false;
        maxFloor =0;
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        init();
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
            if (!player.getAlive()) {
                if(event.getX()<getWidth()/2&&!restarted){
                    init();
                    restarted=true;
                }
                else{
                    player.setAlive(true);
                    restarted=false;
                }
            }
            else{
                player.setAlive(false);
            }
        }
        return super.onTouchEvent(event);
    }

    void update() {
        if(player.getAlive()) {
            player.update(speed);
            background.update(player.getDy());
            for (int i = 0; i < levels.size(); i++) {
                levels.get(i).update(player.getDy());
                if (levelColission(player, levels.get(i))) {
                    player.setFloorJumpFrame(1);
                    if (maxFloor < levels.get(i).getNumber()) {
                        maxFloor = levels.get(i).getNumber();
                    }
                }
                if (levels.get(i).getY() > GameActivity.displaySize.y + 20) {
                    levels.remove(i);
                    if(levels.get(levels.size() - 1).getNumber() + 1<=1000){
                        levels.add(new Level(images[0],
                                levels.get(levels.size() - 1).getY() - 53 - levels.get(levels.size() - 1).getHeight(),
                                levels.get(levels.size() - 1).getNumber() + 1));
                    }

                }
            }

            for (int i = 0; i < leftBorder.size(); i++) {
                leftBorder.get(i).update(player.getDy());
                rightBorder.get(i).update(player.getDy());
                if (borderCollistion(player, leftBorder.get(i))
                        || borderCollistion(player, rightBorder.get(i)))
                    player.setWallJumpFrame(1);

                if (leftBorder.get(i).getY() > GameActivity.displaySize.y) {
                    leftBorder.remove(i);
                    rightBorder.remove(i);
                    leftBorder.add(new Border(images[1], 0,
                            leftBorder.get(leftBorder.size() - 1).getY() - leftBorder.get(leftBorder.size() - 1).getHeight()));
                    rightBorder.add(new Border(images[1],
                            GameActivity.displaySize.x - rightBorder.get(rightBorder.size() - 1).getWidth(),
                            rightBorder.get(rightBorder.size() - 1).getY() - rightBorder.get(rightBorder.size() - 1).getHeight()));
                }
            }
            endGame();
            speed=maxFloor/100+3;
        }
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
            player.draw(canvas);

            if(!player.getAlive()){
                drawMenu(canvas);
            }

            if(win || loss){
                drawEndGame(canvas);
            }
        }
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

    private void endGame(){
        if(player.y>GameActivity.displaySize.y+player.getHeight()){
            player.setAlive(false);
            loss=true;
        }
        if(maxFloor ==1000){
            player.setAlive(false);
            win=true;
        }
    }

    void drawEndGame(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(75);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        if(win){
            canvas.drawText("ZWYCIESTWO!", 250, GameActivity.displaySize.y/2, paint);
        }
        else{
            canvas.drawText("FLOOR: " + maxFloor, 250, GameActivity.displaySize.y/2, paint);
        }
    }

    private void drawMenu(Canvas canvas){
        Bitmap restart=BitmapFactory.decodeResource(getResources(), R.drawable.restart);
        Bitmap resume=BitmapFactory.decodeResource(getResources(), R.drawable.resume);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(75);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        if(restarted){
            canvas.drawText("Touch to play", 200, GameActivity.displaySize.y/2, paint);
        }
        else{
            canvas.drawBitmap(restart, 0, 0, null);
            canvas.drawBitmap(resume, GameActivity.displaySize.x / 2, 0, null);
        }
    }
}

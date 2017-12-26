package szutowicz.krystian.icytower;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorManager;

class Player extends GameObject {

    private Accelerometer accelerometer;
    private boolean alive;
    private boolean canWallJump;
    private int wallJumpFrame;
    private int floorJumpFrame;

    Player(Bitmap image, SensorManager sensorManager, int speed){
        this.image=image;
        width=image.getWidth();
        height=image.getHeight();
        accelerometer = new Accelerometer();
        sensorManager.registerListener(accelerometer, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        x=GameActivity.displaySize.x/2;
        y=GameActivity.displaySize.y*3/4-height-5;
        dy=speed;
        wallJumpFrame =0;
        floorJumpFrame =0;
    }

    void update(int speed){
        int oldY=y;

        x=(int)(x + accelerometer.getData() *3);
        if(x<Game.images[1].getWidth()-1)
            x=Game.images[1].getWidth()-1;
        if(x>GameActivity.displaySize.x-Game.images[1].getWidth()-width+1)
            x=GameActivity.displaySize.x-Game.images[1].getWidth()-width+1;

        if(wallJumpFrame >0){
            floorJumpFrame =0;
            wallJump();
        }
        if(floorJumpFrame >0) {
            canWallJump =true;
            floorJump();
        }
        if(floorJumpFrame ==0 && wallJumpFrame ==0)
        {
            y = y + 12;
        }

        if(y<GameActivity.displaySize.y/5&&oldY>y){
            dy=oldY-y;
            y=oldY;
        }
        else{
            dy=speed;
        }
    }

    void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

    private void wallJump(){

        if (wallJumpFrame < 10) {
            y = y - 26;
            if(x<GameActivity.displaySize.x/2){
                x = x + 15;
            }
            else{
                x=x-15;
            }
        }
        if (wallJumpFrame > 10) {
            wallJumpFrame = 0;
            canWallJump =false;
        } else {
            wallJumpFrame++;
        }
    }

    private void floorJump(){

        if (floorJumpFrame < 10) {
            y = y - 16;
        }
        if (floorJumpFrame > 10) {
            floorJumpFrame = 0;
        } else {
            floorJumpFrame++;
        }
    }

    void setAlive(boolean alive){
        this.alive =alive;
    }

    boolean getAlive(){
        return alive;
    }

    boolean getCanWallJump(){
        return canWallJump;
    }

    void setWallJumpFrame(int wallJumpFrame){
        this.wallJumpFrame = wallJumpFrame;
    }

    int getWallJumpFrame(){
        return wallJumpFrame;
    }

    void setFloorJumpFrame(int floorJumpFrame){
        this.floorJumpFrame=floorJumpFrame;
    }

    int getFloorJumpFrame(){
        return floorJumpFrame;
    }
}

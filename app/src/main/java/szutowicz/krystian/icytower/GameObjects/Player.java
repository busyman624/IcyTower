package szutowicz.krystian.icytower.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import szutowicz.krystian.icytower.Accelerometer;
import szutowicz.krystian.icytower.Bluetooth.Connection;
import szutowicz.krystian.icytower.Bluetooth.Message;
import szutowicz.krystian.icytower.MainMenuActivity;

public class Player extends GameObject {

    private Connection connection;
    private Accelerometer accelerometer;
    private boolean canWallJump;
    private int wallJumpFrame;
    private int floorJumpFrame;
    private int borderWidth;
    private int totalY;
    private int maxFloor;

    public Player(Bitmap image, SensorManager sensorManager, int speed, int borderWidth){
        init(image, sensorManager, speed, borderWidth);
    }

    public Player(Bitmap image, SensorManager sensorManager, int speed, int borderWidth, Connection connection){
        this.connection=connection;
        init(image, sensorManager, speed, borderWidth);
    }

    private void init(Bitmap image, SensorManager sensorManager, int speed, int borderWidth){
        this.image=image;
        width=image.getWidth();
        height=image.getHeight();
        accelerometer = new Accelerometer();
        sensorManager.registerListener(accelerometer, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        x= MainMenuActivity.displaySize.x/2;
        y= MainMenuActivity.displaySize.y*3/4-height-5;
        dy=speed;
        wallJumpFrame =0;
        floorJumpFrame =0;
        totalY=0;
        this.borderWidth=borderWidth;
    }

    public void update(int speed){
        int oldY=y;

        x=(int)(x + accelerometer.getData() * 4);
        if(x< borderWidth-1)
            x=borderWidth-1;
        if(x> MainMenuActivity.displaySize.x-borderWidth-width+1)
            x= MainMenuActivity.displaySize.x-borderWidth-width+1;

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

        if(y< MainMenuActivity.displaySize.y/5&&oldY>y){
            dy=oldY-y;
            y=oldY;
        }
        else{
            dy=speed;
        }

        if(connection!=null){
            float relativeX=(float)(x-borderWidth)/(MainMenuActivity.displaySize.x - 2 * borderWidth);
            totalY=totalY-oldY+y-dy;
            connection.write(new Message(true, relativeX, totalY, maxFloor));
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

    private void wallJump(){

        if (wallJumpFrame < 10) {
            y = y - 26;
            if(x< MainMenuActivity.displaySize.x/2){
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

    public boolean getCanWallJump(){
        return canWallJump;
    }

    public void setWallJumpFrame(int wallJumpFrame){
        this.wallJumpFrame = wallJumpFrame;
    }

    public int getWallJumpFrame(){
        return wallJumpFrame;
    }

    public void setFloorJumpFrame(int floorJumpFrame){
        this.floorJumpFrame=floorJumpFrame;
    }

    public int getFloorJumpFrame(){
        return floorJumpFrame;
    }

    public int getTotalY() { return totalY; }

    public int getMaxFloor() {return maxFloor; }

    public void setMaxFloor(int maxFloor) { this.maxFloor = maxFloor; }
}

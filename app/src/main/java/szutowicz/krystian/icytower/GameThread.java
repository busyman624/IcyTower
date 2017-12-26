package szutowicz.krystian.icytower;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

class GameThread extends Thread{

    private final SurfaceHolder surfaceHolder;
    private Game game;
    private boolean running;
    private Canvas canvas;

    GameThread(SurfaceHolder surfaceHolder, Game game )    {
        super();
        this.game=game;
        this.surfaceHolder=surfaceHolder;
    }

    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        int frameCount =0;
        int FPS = 30;
        long targetTime = 1000/ FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.game.update();
                    this.game.draw(canvas);
                }
            } catch (Exception e) {}
            finally{
                if(canvas!=null)
                {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e){e.printStackTrace();}
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMillis;

            try{
                sleep(waitTime);
            }catch(Exception e){}

            frameCount++;
            if(frameCount == FPS)
            {
                frameCount =0;
            }
        }
    }

    void setRunning(boolean running)
    {
        this.running=running;
    }
}

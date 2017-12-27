package szutowicz.krystian.icytower;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class Accelerometer implements SensorEventListener{
    private float data;

    @Override
    public void onSensorChanged(SensorEvent event) {
        data = event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public float getData(){
        return data;
    }
}

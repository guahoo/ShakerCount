package com.example.shakercount;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

public class ShakeDetector {
    public static final int TRESHHOLD = 8;
    public static final int SHAKESCOUNT = 3;
    public static final int SHAKESPERIOD= 1;

    @NonNull
    public static Observable<?> create(@NonNull Context context){
        return createAccelerationObservable(context)
                .map(sensorEvent -> new Xevent(sensorEvent.timestamp,sensorEvent.values[1]))
                .filter(xevent -> Math.abs(xevent.x)>TRESHHOLD)
                .buffer(2,1)
                .filter(buf -> buf.get(0).x * buf.get(1).x<0)
                .map(buf ->buf.get(1).timeStamp/1000000000f)
                .buffer(SHAKESCOUNT,1)
                .filter(buf->buf.get(SHAKESCOUNT-1)-buf.get(0)<SHAKESPERIOD)
                .throttleFirst(SHAKESPERIOD, TimeUnit.SECONDS);
    }

  @NonNull
    private static Observable<SensorEvent> createAccelerationObservable(@NonNull Context context){
      SensorManager mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
      List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
      if (sensorList==null||sensorList.isEmpty()){
          throw new IllegalStateException("Device has no linear acceleration sensor");
      }
      return SensorEventObservableFactory.createSensorEventObservable(sensorList.get(0),mSensorManager);
  }

  private static class Xevent {
      public final long timeStamp;
      public final float x;

      private Xevent (long timeStamp,float x){
          this.timeStamp = timeStamp;
          this.x = x;
      }
  }
}

package com.example.shakercount;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import io.reactivex.rxjava3.android.MainThreadDisposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;


public class SensorEventObservableFactory {

    public static Observable<SensorEvent> createSensorEventObservable(@NonNull Sensor sensor, @NonNull SensorManager sensorManager) {
        return Observable.create(subscriber -> {
            MainThreadDisposable.verifyMainThread();
            SensorEventListener eventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (subscriber.isDisposed()) {
                        return;
                    }
                    subscriber.onNext(event);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };

            sensorManager.registerListener(eventListener,sensor,SensorManager.SENSOR_DELAY_GAME);


            subscriber.setDisposable(new MainThreadDisposable() {
                @Override
                protected void onDispose() {
                    sensorManager.unregisterListener(eventListener);
                }
            });
        });




    }
}

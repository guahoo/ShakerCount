package com.example.shakercount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;


public class MainActivity extends AppCompatActivity {
private Observable makeShakeObsservable;
private Disposable mShakeSubscription;
private static int eventCount;
private TextView countTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeShakeObsservable = ShakeDetector.create(this);
        countTextView = findViewById(R.id.countTextView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mShakeSubscription=makeShakeObsservable.subscribe((object) -> countUp(eventCount++));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShakeSubscription.dispose();
    }

    private void countUp(int i){
       countTextView.setText(String.valueOf(i));
    }
}

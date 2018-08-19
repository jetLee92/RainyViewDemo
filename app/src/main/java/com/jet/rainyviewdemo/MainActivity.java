package com.jet.rainyviewdemo;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private RainyView rainyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rainyView = findViewById(R.id.view);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainyView.addItemRain();
                init();
            }
        });
    }

    private void init() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(rainyView, "rainyVelocity", 0, 1);
        objectAnimator.setDuration(6000);
        objectAnimator.setStartDelay(300);
        objectAnimator.start();


        ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(rainyView, "rotateAngel", 0, 60);
        objectAnimator2.setDuration(3000);
        objectAnimator2.setStartDelay(300);
        objectAnimator2.start();

    }

}

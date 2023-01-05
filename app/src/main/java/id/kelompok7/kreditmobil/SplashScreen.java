package id.kelompok7.kreditmobil;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    ImageView imgCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splashscreen);
        imgCar = findViewById(R.id.imgSplash);

//        ImageView imageView = findViewById(R.id.imgSplash);
//        imageView.animate().rotation(720f).setDuration(1800).start();
//
//
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);


        final Thread t = new Thread() {
            public void run() {
                int init = 0;

                while(init < 100) {
                    try {
                        sleep(200);
                        init += 5;

                        progressBar.setProgress(init);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            };
        }, 3500L);


        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
                imgCar,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 0.8f)
        );
        animator.setDuration(350);

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.start();
    };
}

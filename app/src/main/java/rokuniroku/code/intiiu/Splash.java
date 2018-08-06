package rokuniroku.code.intiiu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/*
Reference = https://www.youtube.com/watch?v=g-oAWrAvOMo&t=23s
 */

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.splashImage);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_fade);
        imageView.startAnimation(animation);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(intent);
                    finish();
                    super.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.start();
    }
}

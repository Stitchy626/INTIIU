package rokuniroku.code.newintiiu;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
Reference = https://www.youtube.com/watch?v=g-oAWrAvOMo&t=23s
 */

public class Splash extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        ImageView imageView = findViewById(R.id.splashImage);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_fade);
        imageView.startAnimation(animation);



        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    //Silent Login
                    mUser = mAuth.getCurrentUser();
                    if (mUser != null) {
                        startActivity(new Intent(Splash.this, HomePage.class));
                        //the HomeActivity Page not working the one amber push
                    } else {
                        startActivity(new Intent(Splash.this, LoginPageNew.class));
                    }
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

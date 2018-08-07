package rokuniroku.code.intiiu;

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

public class Splash extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener {

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

        //Update Check
        UpdateHelper.with(this)
                .onUpdateCheck(this)
                .check();

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    //Silent Login
                    mUser = mAuth.getCurrentUser();
                    if (mUser != null) {
                        startActivity(new Intent(Splash.this, HomePageActivity.class));
                        //the HomeActivity Page not working the one amber push
                    } else {
                        startActivity(new Intent(Splash.this, LoginPage.class));
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

    //Update Class
    @Override
    public void onUpdateCheckListener(final String urlApp) {

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("A New Version of this App is avaiable")
                .setMessage("Please Update to the lastest version")
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //open google playstore to the app
                        try{
                            //if user has google playstore in their phone
                            startActivity( new Intent( Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + getPackageName())) );
                        } catch (ActivityNotFoundException e){
                            //if user dont have playstore in their phone, open website playstore
                            startActivity( new Intent( Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())) );
                        }

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        finish();
                    }
                }).setCancelable( false ).create();
        alertDialog.show();

    }
}

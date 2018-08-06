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

/*
Reference = https://www.youtube.com/watch?v=g-oAWrAvOMo&t=23s
 */

public class Splash extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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

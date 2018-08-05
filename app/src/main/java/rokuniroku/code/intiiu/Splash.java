package rokuniroku.code.intiiu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        EasySplashScreen config = new EasySplashScreen(Splash.this)
                .withFullScreen()
                .withTargetActivity(MainMenu.class)
                .withSplashTimeOut(2000)
                .withBackgroundColor(Color.parseColor("#ffffff"))
                //.withHeaderText("Header")
                //.withFooterText("INTI IU")
                //.withBeforeLogoText("Before Logo Text")
                .withAfterLogoText("                  INTI \nInternational University")
                .withLogo(R.mipmap.ic_launcher_intiicon_finale_round);

        //config.getHeaderTextView().setTextColor(Color.WHITE);
        //config.getFooterTextView().setTextColor(Color.BLACK);
        //config.getBeforeLogoTextView().setTextColor(Color.WHITE);
        config.getAfterLogoTextView().setTextColor(Color.BLACK);
        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}

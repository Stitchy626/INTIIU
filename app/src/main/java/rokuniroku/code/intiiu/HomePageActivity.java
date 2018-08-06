package rokuniroku.code.intiiu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ImageView imgViewAcademicCalendar,imgView_Announcement,imgView_BusSchedule,imgView_lostnFound,
    imgView_Event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home Page");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imgView_Announcement = (ImageView)findViewById(R.id.imgView_Announcement);
        imgView_BusSchedule = (ImageView)findViewById(R.id.imgView_BusSchedule);
        imgView_Event = (ImageView)findViewById(R.id.imgView_Event);
        imgView_lostnFound = (ImageView)findViewById(R.id.imgView_LostnFound);
        imgViewAcademicCalendar = (ImageView)findViewById(R.id.imgView_AcademicCalender);

        imgViewAcademicCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), AcademicCalender.class);
            startActivity(intent);
            }

        });

        imgView_Announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), INTIAnnPage.class);
                startActivity(intent);
            }

        });

        imgView_BusSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BusSchedule.class);
                startActivity(intent);
            }

        });

        imgView_lostnFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LNFPage.class);
                startActivity(intent);
            }

        });

        imgView_Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventAnnPage.class);
                startActivity(intent);
            }

        });





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_AcademicCalendar) {
            Intent intent = new Intent(getApplicationContext(), AcademicCalender.class);
            startActivity(intent);
        } else if (id == R.id.nav_Announcement) {
            Intent intent = new Intent(getApplicationContext(), INTIAnnPage.class);
            startActivity(intent);

        } else if (id == R.id.nav_EventAnn) {
            Intent intent = new Intent(getApplicationContext(), EventAnnPage.class);
            startActivity(intent);

        } else if (id == R.id.nav_BusSchedule) {
                Intent intent = new Intent(getApplicationContext(), BusSchedule.class);
                startActivity(intent);

        } else if (id == R.id.nav_lostnFound) {
            Intent intent = new Intent(getApplicationContext(), LNFPage.class);
            startActivity(intent);

        } else if (id == R.id.nav_signout) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

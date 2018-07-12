package rokuniroku.code.intiiu;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventAnnPage extends AppCompatActivity {

    private FirebaseDatabase dbDatabase;
    private DatabaseReference rootDatabase, eventAnnRef;
    private Query query;

    private FirebaseStorage dbStorage;
    private StorageReference rootStorage, path;

    private Button buttonToday, buttonUpcoming;
    private ListView listViewAnn;

    private ArrayList<EventAnn> annListToday, annListUpcoming, emptyList;

    private Toolbar myToolbar;

    private SimpleDateFormat dateFormat,timeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_ann_page);


        rootDatabase = dbDatabase.getInstance().getReference();
        eventAnnRef = rootDatabase.child("Announcement").child("EventAnn");
        query = eventAnnRef.orderByChild("status").equalTo("approved");

        rootStorage = dbStorage.getInstance().getReference();
        path = rootStorage.child("Announcement").child("EventAnn");

        buttonToday = (Button) findViewById(R.id.buttonToday);
        buttonUpcoming = (Button) findViewById(R.id.buttonUpcoming);
        listViewAnn = (ListView) findViewById(R.id.listViewAnn);

        annListToday = new ArrayList<>();
        annListUpcoming = new ArrayList<>();
        emptyList = new ArrayList<>();

        myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");//date format
        timeFormat = new SimpleDateFormat("HH:mm");//time format

        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));



        ManageApproveEvent();// load all the events into the array list

        buttonToday.setBackgroundColor(Color.parseColor("#FC8F00"));

        PopulateEventAnn(buttonToday.getText().toString());


        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonToday.setBackgroundColor(Color.parseColor("#FC8F00"));
                buttonUpcoming.setBackgroundResource(android.R.drawable.btn_default);
                PopulateEventAnn(buttonToday.getText().toString());
            }
        });

        buttonUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonUpcoming.setBackgroundColor(Color.parseColor("#FC8F00"));
                buttonToday.setBackgroundResource(android.R.drawable.btn_default);
                PopulateEventAnn(buttonUpcoming.getText().toString());
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_ann_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_request:
                startActivity(new Intent(EventAnnPage.this, EventAnnRequestPage.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void ManageApproveEvent(){

        Calendar calendar = Calendar.getInstance();

        final Date today = calendar.getTime();

        final ArrayList<Date> date = new ArrayList<>();
        final ArrayList<Date> time = new ArrayList<>();



        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    EventAnn announcement = snapshot.getValue(EventAnn.class);


                    //convert string date to Date datatype for easy and accurate comparison
                    try {
                        date.add(dateFormat.parse(dateFormat.format(today)));
                        date.add(dateFormat.parse(announcement.getDateStart()));
                        date.add(dateFormat.parse(announcement.getDateEnd()));

                        time.add(timeFormat.parse(timeFormat.format(today)));
                        time.add(timeFormat.parse(announcement.getTimeStart()));
                        time.add(timeFormat.parse(announcement.getTimeEnd()));
                    } catch (ParseException e) {
                        Log.d("Exception error =", e.toString());
                    }



                    //splitting and managing include deletion
                    if(date.get(0).equals(date.get(1)) || date.get(0).after(date.get(1)) && date.get(0).before(date.get(2))){
                        if(time.get(0).equals(time.get(1)) || time.get(0).after(time.get(1)) && time.get(0).before(time.get(2)))
                            annListToday.add(announcement);

                    }else if(date.get(0).before(date.get(1)) || date.get(0).equals(date.get(1))){
                         if(time.get(0).before(time.get(1)))
                            annListUpcoming.add(announcement);

                    }else if(date.get(0).after(date.get(2)) || date.get(0).equals(date.get(1))){
                         if(time.get(0).after(time.get(2)))
                             DeleteEvent(snapshot.getKey().toString());
                    }
                }


                //sorting for the nearest and newest started event to be on top
                for(int x = 0; x < annListToday.size(); x++){
                    for(int y = 0; y < annListToday.size() - x - 1; y++){

                        if (annListToday.get(y).getDateStart().substring(6, 10).compareTo(annListToday.get(y + 1).getDateStart().substring(6, 10)) < 0) {
                            EventAnn temp = annListToday.get(y);
                            annListToday.set(y, annListToday.get(y + 1));
                            annListToday.set(y + 1, temp);

                        } else if (annListToday.get(y).getDateStart().substring(6, 10).compareTo(annListToday.get(y + 1).getDateStart().substring(6, 10)) == 0) {
                            //MONTH
                            if (annListToday.get(y).getDateStart().substring(3, 5).compareTo(annListToday.get(y + 1).getDateStart().substring(3, 5)) < 0) {
                                EventAnn temp = annListToday.get(y);
                                annListToday.set(y, annListToday.get(y + 1));
                                annListToday.set(y + 1, temp);

                            } else if (annListToday.get(y).getDateStart().substring(3, 5).compareTo(annListToday.get(y + 1).getDateStart().substring(3, 5)) == 0) {
                                //DAY
                                if (annListToday.get(y).getDateStart().substring(0, 2).compareTo(annListToday.get(y + 1).getDateStart().substring(0, 2)) < 0) {
                                    EventAnn temp = annListToday.get(y);
                                    annListToday.set(y, annListToday.get(y + 1));
                                    annListToday.set(y + 1, temp);

                                } else if (annListToday.get(y).getDateStart().substring(0, 2).compareTo(annListToday.get(y + 1).getDateStart().substring(0, 2)) == 0) {
                                    //HOUR
                                    if (annListToday.get(y).getTimeStart().substring(0, 2).compareTo(annListToday.get(y + 1).getTimeStart().substring(0, 2)) < 0) {
                                        EventAnn temp = annListToday.get(y);
                                        annListToday.set(y, annListToday.get(y + 1));
                                        annListToday.set(y + 1, temp);

                                    } else if (annListToday.get(y).getTimeStart().substring(0, 2).compareTo(annListToday.get(y + 1).getTimeStart().substring(0, 2)) == 0) {
                                        //MINUTE
                                        if (annListToday.get(y).getTimeStart().substring(3, 5).compareTo(annListToday.get(y + 1).getTimeStart().substring(3, 5)) < 0) {
                                            EventAnn temp = annListToday.get(y);
                                            annListToday.set(y, annListToday.get(y + 1));
                                            annListToday.set(y + 1, temp);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void PopulateEventAnn(final String category){
        EventAnnAdapter adapter = new EventAnnAdapter(EventAnnPage.this, emptyList);

        if(category.equals(buttonToday.getText().toString()))
            adapter = new EventAnnAdapter(EventAnnPage.this, annListToday);
        else if(category.equals(buttonUpcoming.getText().toString()))
            adapter = new EventAnnAdapter(EventAnnPage.this, annListUpcoming);

        listViewAnn.setAdapter(adapter);
    }


    private void DeleteEvent(String key){

        rootDatabase.child(key).removeValue();
    }

}

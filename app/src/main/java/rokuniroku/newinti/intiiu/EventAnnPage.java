package rokuniroku.newinti.intiiu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private static String userValidation = "@student.newinti.edu.my";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase dbDatabase;
    private DatabaseReference rootDatabaseRef;
    private Query queryDatabaseRef;

    private Toolbar myToolbar;
    private Button buttonToday, buttonUpcoming;
    private ListView listViewAnn;

    private ArrayList<EventAnn> annListToday, annListUpcoming, emptyList;

    private SimpleDateFormat dateFormat,timeFormat, dateFormatGMT08, timeFormatGMT08;

    private boolean bIsToday;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_ann_page);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        email = mUser.getEmail().toString();

        int startPoint = 0;

        for (int x = 0; x < email.length(); x++) {
            if (email.charAt(x) == '@') {
                startPoint = x;
                break;
            }
        }

        email = email.substring(startPoint, email.length());

        rootDatabaseRef = dbDatabase.getInstance().getReference().child("Announcement").child("EventAnn");
        queryDatabaseRef = rootDatabaseRef.orderByChild("status").equalTo("approved");

        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        buttonToday = (Button) findViewById(R.id.buttonToday);
        buttonUpcoming = (Button) findViewById(R.id.buttonUpcoming);
        listViewAnn = (ListView) findViewById(R.id.listViewAnn);

        annListToday = new ArrayList<>();
        annListUpcoming = new ArrayList<>();
        emptyList = new ArrayList<>();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("HH:mm");

        dateFormatGMT08 = new SimpleDateFormat("dd/MM/yyyy");
        timeFormatGMT08 = new SimpleDateFormat("HH:mm");

        dateFormatGMT08.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        timeFormatGMT08.setTimeZone(TimeZone.getTimeZone("GMT+08"));

        bIsToday = true;

        ManageApproveEvent();

        buttonToday.setSelected(true);

        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonToday.setSelected(true);
                buttonUpcoming.setSelected(false);
                bIsToday = true;
                PopulateEventAnn(bIsToday);
            }
        });

        buttonUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonToday.setSelected(false);
                buttonUpcoming.setSelected(true);
                bIsToday = false;
                PopulateEventAnn(bIsToday);
            }
        });

        listViewAnn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(EventAnnPage.this, EventAnnItemPage.class);
                if(bIsToday == true){
                    intent.putExtra("EventAnnouncement", annListToday.get(position));
                }else if(bIsToday == false){
                    intent.putExtra("EventAnnouncement", annListUpcoming.get(position));
                }
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if(email.equals(userValidation)){
        }else
            getMenuInflater().inflate(R.menu.menu_eventann, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_request:
                startActivity(new Intent(EventAnnPage.this, EventAnnRequestPage.class));
                return true;

            case R.id.action_history:
                startActivity(new Intent(EventAnnPage.this, EventAnnRequestStatusPage.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Loads all the event announcement into the respective list
    private void ManageApproveEvent(){

         Calendar calendar = Calendar.getInstance();

         final Date today = calendar.getTime();

        queryDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                annListToday.clear();
                annListUpcoming.clear();
                emptyList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    EventAnn announcement = snapshot.getValue(EventAnn.class);

                    ArrayList<Date> date = new ArrayList<>();
                    ArrayList<Date> time = new ArrayList<>();

                    //convert string date to Date datatype for easy and accurate comparison
                    try {
                        date.add(dateFormat.parse(dateFormatGMT08.format(today)));
                        date.add(dateFormat.parse(announcement.getDateStart()));
                        date.add(dateFormat.parse(announcement.getDateEnd()));

                        time.add(timeFormat.parse(timeFormatGMT08.format(today)));
                        time.add(timeFormat.parse(announcement.getTimeStart()));
                        time.add(timeFormat.parse(announcement.getTimeEnd()));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    //Managing the announcement //Delete approved event after event date is over
                    if(date.get(0).before(date.get(1))){ // before start date
                        annListUpcoming.add(announcement);
                    }else if(date.get(0).after(date.get(2))) { // after end date
                        DeleteEvent(snapshot.getKey().toString());
                    }else{
                        if(date.get(0).equals(date.get(1)) && date.get(0).equals(date.get(2))){ // detect a one day event
                            if(time.get(0).before(time.get(1)))
                                annListUpcoming.add(announcement);
                            else if(time.get(0).equals(time.get(1)) || time.get(0).after(time.get(1)) && time.get(0).before(time.get(2)) || time.get(0).equals(time.get(2)))
                                annListToday.add(announcement);
                            else if(time.get(0).after(time.get(2)))
                                DeleteEvent(snapshot.getKey().toString());
                        }else{ // detect a few days event
                            if(date.get(0).equals(date.get(1))) { // on the start date
                                if (time.get(0).equals(time.get(1)) || time.get(0).after(time.get(1))) {
                                    annListToday.add(announcement);
                                } else
                                    annListUpcoming.add(announcement);
                            }else if(date.get(0).after(date.get(1)) && date.get(0).before(date.get(2))){ // In between the start and the end date
                                annListToday.add(announcement);
                            }else if(date.get(0).equals(date.get(2))){ // on the end date
                                if(time.get(0).before(time.get(2)))
                                    annListToday.add(announcement);
                                else
                                    DeleteEvent(snapshot.getKey().toString());
                            }else
                                emptyList.add(announcement);
                        }
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

                for(int x = 0; x < annListUpcoming.size(); x++){
                    for(int y = 0; y < annListUpcoming.size() - x - 1; y++){

                        if (annListUpcoming.get(y).getDateStart().substring(6, 10).compareTo(annListUpcoming.get(y + 1).getDateStart().substring(6, 10)) < 0) {
                            EventAnn temp = annListUpcoming.get(y);
                            annListUpcoming.set(y, annListUpcoming.get(y + 1));
                            annListUpcoming.set(y + 1, temp);

                        } else if (annListUpcoming.get(y).getDateStart().substring(6, 10).compareTo(annListUpcoming.get(y + 1).getDateStart().substring(6, 10)) == 0) {
                            //MONTH
                            if (annListUpcoming.get(y).getDateStart().substring(3, 5).compareTo(annListUpcoming.get(y + 1).getDateStart().substring(3, 5)) < 0) {
                                EventAnn temp = annListUpcoming.get(y);
                                annListUpcoming.set(y, annListUpcoming.get(y + 1));
                                annListUpcoming.set(y + 1, temp);

                            } else if (annListUpcoming.get(y).getDateStart().substring(3, 5).compareTo(annListUpcoming.get(y + 1).getDateStart().substring(3, 5)) == 0) {
                                //DAY
                                if (annListUpcoming.get(y).getDateStart().substring(0, 2).compareTo(annListUpcoming.get(y + 1).getDateStart().substring(0, 2)) < 0) {
                                    EventAnn temp = annListUpcoming.get(y);
                                    annListUpcoming.set(y, annListUpcoming.get(y + 1));
                                    annListUpcoming.set(y + 1, temp);

                                } else if (annListUpcoming.get(y).getDateStart().substring(0, 2).compareTo(annListUpcoming.get(y + 1).getDateStart().substring(0, 2)) == 0) {
                                    //HOUR
                                    if (annListUpcoming.get(y).getTimeStart().substring(0, 2).compareTo(annListUpcoming.get(y + 1).getTimeStart().substring(0, 2)) < 0) {
                                        EventAnn temp = annListUpcoming.get(y);
                                        annListUpcoming.set(y, annListUpcoming.get(y + 1));
                                        annListUpcoming.set(y + 1, temp);

                                    } else if (annListUpcoming.get(y).getTimeStart().substring(0, 2).compareTo(annListUpcoming.get(y + 1).getTimeStart().substring(0, 2)) == 0) {
                                        //MINUTE
                                        if (annListUpcoming.get(y).getTimeStart().substring(3, 5).compareTo(annListUpcoming.get(y + 1).getTimeStart().substring(3, 5)) < 0) {
                                            EventAnn temp = annListUpcoming.get(y);
                                            annListUpcoming.set(y, annListUpcoming.get(y + 1));
                                            annListUpcoming.set(y + 1, temp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                PopulateEventAnn(bIsToday);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void PopulateEventAnn(boolean bIsToday){
        EventAnnAdapter adapter = new EventAnnAdapter(EventAnnPage.this, emptyList);

        if(bIsToday == true)
            adapter = new EventAnnAdapter(EventAnnPage.this, annListToday);
        else if(bIsToday == false)
            adapter = new EventAnnAdapter(EventAnnPage.this, annListUpcoming);

        listViewAnn.setAdapter(adapter);
    }


    private void DeleteEvent(String key){

        rootDatabaseRef.child(key).removeValue();
    }

}

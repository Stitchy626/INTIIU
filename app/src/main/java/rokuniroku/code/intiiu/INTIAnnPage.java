package rokuniroku.code.intiiu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class INTIAnnPage extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference rootRef, intiAnnRef;
    private Query query;

    private ListView listViewAnn;

    private ArrayList<INTIAnn> annList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intiann_page);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.filter_white));
        setSupportActionBar(myToolbar);

        rootRef = database.getInstance().getReference();
        intiAnnRef = rootRef.child("Announcement").child("INTIAnn");

        listViewAnn = (ListView) findViewById(R.id.listViewAnn);

        annList = new ArrayList<>();


        PopulateINTIAnn();
        //PushINTIAnn();

        listViewAnn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(INTIAnnPage.this, INTIAnnItemPage.class);
                intent.putExtra("INTIAnnouncement", annList.get(position));
                startActivity(intent);
            }
        });

    }

    //Filter Menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.ann_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_All: {
                //Enter Filter HERE


                return true;
            }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }




    private void PopulateINTIAnn() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");//date format
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");//time format

        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

        final String today = dateFormat.format(calendar.getTime());


        query = intiAnnRef.orderByChild("dateUpload").equalTo("");



        intiAnnRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                annList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    INTIAnn announcement = snapshot.getValue(INTIAnn.class);

                    //Delete for expired announcement
                    if(announcement.getCourtDate().equals(today))
                        intiAnnRef.child(snapshot.getKey()).removeValue();
                    else
                        annList.add(announcement);
                }

                //Sort the announcement that latest should be on top
                for(int x = 0; x < annList.size(); x++) {
                    for (int y = 0; y < annList.size() - x - 1; y++) {
                        if(annList.get(y).getDateUpload().substring(6, 10).compareTo(annList.get(y + 1).getDateUpload().substring(6, 10)) < 0){
                            INTIAnn temp = annList.get(y);
                            annList.set(y, annList.get(y + 1));
                            annList.set(y + 1, temp);
                        }else if (annList.get(y).getDateUpload().substring(6, 10).compareTo(annList.get(y + 1).getDateUpload().substring(6, 10)) == 0) {
                            if (annList.get(y).getDateUpload().substring(3, 5).compareTo(annList.get(y + 1).getDateUpload().substring(3, 5)) < 0) {
                                INTIAnn temp = annList.get(y);
                                annList.set(y, annList.get(y + 1));
                                annList.set(y + 1, temp);

                            } else if (annList.get(y).getDateUpload().substring(3, 5).compareTo(annList.get(y + 1).getDateUpload().substring(3, 5)) == 0) {
                                if (annList.get(y).getDateUpload().substring(0, 2).compareTo(annList.get(y + 1).getDateUpload().substring(0, 2)) < 0) {
                                    INTIAnn temp = annList.get(y);
                                    annList.set(y, annList.get(y + 1));
                                    annList.set(y + 1, temp);
                                }
                            }
                        }
                    }
                }

                //Add to list view
                INTIAnnAdapter adapter = new INTIAnnAdapter(INTIAnnPage.this, annList);
                listViewAnn.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*private void PushINTIAnn() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");//date format
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");//time format

        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));


        for (int x = 1; x <= 1; x++) {

            Date today = calendar.getTime();

            calendar.add(Calendar.DAY_OF_YEAR, 1);

            String id = intiAnnRef.push().getKey();

            INTIAnn ann = new INTIAnn(id, "FITMS", "26/06/2019", timeFormat.format(today), "28/06/2018", "This should be the content area", "dunno how leh", "28/06/2018 - 28/06/2018", "4:00 PM - 6:00 PM"); //putting getters in object class causes the app to crash if class field doesn't have private access

            intiAnnRef.child(id).setValue(ann);


        }
    }*/
}

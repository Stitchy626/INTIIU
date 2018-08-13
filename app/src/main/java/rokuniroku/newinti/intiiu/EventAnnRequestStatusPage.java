package rokuniroku.newinti.intiiu;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.TimeZone;

public class EventAnnRequestStatusPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase dbDatabase;
    private DatabaseReference rootDatabaseRef;
    private Query queryDatabaseRef;

    private ListView listViewStatus;

    private ArrayList<EventAnn> annList;

    private SimpleDateFormat dateFormat, dateFormatGMT08;

    private Calendar calendar;

    private String sUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_ann_request_status_page);

        mUser = mAuth.getInstance().getCurrentUser();

        rootDatabaseRef = dbDatabase.getInstance().getReference().child("Announcement").child("EventAnn");
        queryDatabaseRef = rootDatabaseRef.orderByChild("clubEmail").equalTo(mUser.getEmail().toString());

        listViewStatus = (ListView) findViewById(R.id.listViewStatus);

        annList = new ArrayList<EventAnn>();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatGMT08 = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatGMT08.setTimeZone(TimeZone.getTimeZone("GMT+08"));

        calendar = Calendar.getInstance();


        queryDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                annList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    EventAnn announcement = snapshot.getValue(EventAnn.class);

                    //Delete rejected request after 2 weeks
                    if(announcement.getStatus().equals("rejected")){

                        ArrayList<Date> date = new ArrayList<>();

                        try{
                            date.add(dateFormat.parse(dateFormatGMT08.format(calendar.getTime())));
                            date.add(dateFormat.parse(announcement.getDeleteDate()));
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        if(date.get(0).equals(date.get(1)) || date.get(0).after(date.get(1)))
                            DeleteEvent(snapshot.getKey().toString());
                        else
                            annList.add(announcement);
                    }else
                        annList.add(announcement);
                }

                //Sort by uploaded date and time
                for (int x = 0; x < annList.size(); x++) {
                    for (int y = 0; y < annList.size() - x - 1; y++) {
                        //YEAR
                        if (annList.get(y).getDateUpload().substring(6, 10).compareTo(annList.get(y + 1).getDateUpload().substring(6, 10)) < 0) {
                            EventAnn temp = annList.get(y);
                            annList.set(y, annList.get(y + 1));
                            annList.set(y + 1, temp);

                        } else if (annList.get(y).getDateUpload().substring(6, 10).compareTo(annList.get(y + 1).getDateUpload().substring(6, 10)) == 0) {
                            //MONTH
                            if (annList.get(y).getDateUpload().substring(3, 5).compareTo(annList.get(y + 1).getDateUpload().substring(3, 5)) < 0) {
                                EventAnn temp = annList.get(y);
                                annList.set(y, annList.get(y + 1));
                                annList.set(y + 1, temp);

                            } else if (annList.get(y).getDateUpload().substring(3, 5).compareTo(annList.get(y + 1).getDateUpload().substring(3, 5)) == 0) {
                                //DAY
                                if (annList.get(y).getDateUpload().substring(0, 2).compareTo(annList.get(y + 1).getDateUpload().substring(0, 2)) < 0) {
                                    EventAnn temp = annList.get(y);
                                    annList.set(y, annList.get(y + 1));
                                    annList.set(y + 1, temp);

                                } else if (annList.get(y).getDateUpload().substring(0, 2).compareTo(annList.get(y + 1).getDateUpload().substring(0, 2)) == 0) {
                                    //HOUR
                                    if (annList.get(y).getTimeUpload().substring(0, 2).compareTo(annList.get(y + 1).getTimeUpload().substring(0, 2)) < 0) {
                                        EventAnn temp = annList.get(y);
                                        annList.set(y, annList.get(y + 1));
                                        annList.set(y + 1, temp);

                                    } else if (annList.get(y).getTimeUpload().substring(0, 2).compareTo(annList.get(y + 1).getTimeUpload().substring(0, 2)) == 0) {
                                        //MINUTE
                                        if (annList.get(y).getTimeUpload().substring(3, 5).compareTo(annList.get(y + 1).getTimeUpload().substring(3, 5)) < 0) {
                                            EventAnn temp = annList.get(y);
                                            annList.set(y, annList.get(y + 1));
                                            annList.set(y + 1, temp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                EventAnnRequestStatusAdapter adapter = new EventAnnRequestStatusAdapter(EventAnnRequestStatusPage.this, annList);
                listViewStatus.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listViewStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RelativeLayout layoutReason = (RelativeLayout) view.findViewById(R.id.layoutReason);
                TextView textViewReason = (TextView) view.findViewById(R.id.textViewReason);

                if(layoutReason.getVisibility() == View.VISIBLE){
                    if(textViewReason.getVisibility() != View.VISIBLE) {
                        textViewReason.setVisibility(View.VISIBLE);
                    }else{
                        textViewReason.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    private void DeleteEvent(String key){

        rootDatabaseRef.child(key).removeValue();
    }

}

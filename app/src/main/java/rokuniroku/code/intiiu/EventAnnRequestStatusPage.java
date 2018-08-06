package rokuniroku.code.intiiu;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventAnnRequestStatusPage extends AppCompatActivity {

    private FirebaseDatabase dbDatabase;
    private DatabaseReference rootDatabase;
    private Query query;

    private ListView listViewStatus;

    private ArrayList<EventAnn> annList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_ann_request_status_page);

        rootDatabase = dbDatabase.getInstance().getReference().child("Announcement").child("EventAnn");
        query = rootDatabase.orderByChild("club").equalTo("Isaac Club");

        listViewStatus = (ListView) findViewById(R.id.listViewStatus);

        annList = new ArrayList<EventAnn>();


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                annList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    annList.add(snapshot.getValue(EventAnn.class));

                    //Sort uploaded date and time
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
}

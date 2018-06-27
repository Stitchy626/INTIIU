package rokuniroku.code.intiiu;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class INTIAnnPage extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference rootRef, intiAnnRef;

    private ListView listViewAnn;

    private ArrayList<INTIAnn> annList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intiann_page);

        rootRef = database.getInstance().getReference();
        intiAnnRef = rootRef.child("Announcement").child("INTIAnn");

        listViewAnn = (ListView) findViewById(R.id.listViewAnn);

        annList = new ArrayList<>();


        PopulateINTIAnn();
        //PushINTIAnn();

    }

    private void PopulateINTIAnn() {

        intiAnnRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                annList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    INTIAnn announcement = snapshot.getValue(INTIAnn.class);
                    annList.add(announcement);
                }

                INTIAnnAdapter adapter = new INTIAnnAdapter(INTIAnnPage.this, annList);
                listViewAnn.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*PushINTIAnn(){
        for(int x = 1; x < 10; x++) {
            String id = intiAnnRef.push().getKey();

            INTIAnnClass ann = new INTIAnnClass(id, "Tester", "Tester", "Tester", "empty", "empty", "empty", "Tester", "Tester"); //putting getters in object class causes the app to crash if class field doesn't have private access

            intiAnnRef.child(id).setValue(ann);
    }
     */

}

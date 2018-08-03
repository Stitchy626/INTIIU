package rokuniroku.code.intiiu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LNFItemPage extends AppCompatActivity {
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference rootDatabase;
    ArrayList<String> list;
    ArrayAdapter <String> adapter;
    LNFItem item;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnfitem_page);
        item = new LNFItem();
        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.lnf_item,R.id.Date, list);

        //Get Intent from LNFPage activity
        String value = (String) getIntent().getExtras().get("cat");

        //Database
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("LostNFound");
        rootDatabase = database.getInstance().getReference().child("LostNFound");
        query = rootDatabase.orderByChild("category").equalTo(value);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String v = (String) getIntent().getExtras().get("cat");
                if(v.equalsIgnoreCase("Student ID")) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            item = ds.getValue(LNFItem.class);
                            list.add(item.getFoundDate() + "       " + item.getFoundTime() + "       " + item.getDescription().toString());
                    }
                }else{
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        item = ds.getValue(LNFItem.class);
                        list.add(item.getFoundDate() + "       " + item.getFoundTime() + "       " + item.getVenue().toString());
                    }
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

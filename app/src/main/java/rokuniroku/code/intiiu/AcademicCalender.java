package rokuniroku.code.intiiu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AcademicCalender extends AppCompatActivity {

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_academic_calender );

        //the reference to the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child( "Academic Calender" );
        final RecyclerView recyclerView = findViewById( R.id.recyclerView );


        databaseReference.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String fileName = dataSnapshot.getKey(); //  return the fileName
                String url = dataSnapshot.getValue(String.class); // return url for fileName

                ((MyAdapter) recyclerView.getAdapter()).update(fileName,url);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

        recyclerView.setLayoutManager( new LinearLayoutManager( AcademicCalender.this ) );
        MyAdapter myAdapter = new MyAdapter( recyclerView, AcademicCalender.this,new ArrayList<String>(  ), new ArrayList<String>(  ) );
        recyclerView.setAdapter( myAdapter );



    }
}

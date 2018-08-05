package rokuniroku.code.intiiu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LNFPage extends AppCompatActivity {

    private RecyclerView mLNFList;
    private DatabaseReference mDatabase;
    private Query query;
    private ArrayList<String> arrList = new ArrayList<>();
    public String n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnfpage);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Category");
        mDatabase.keepSynced(true);
        mLNFList = (RecyclerView)findViewById(R.id.myrecyclerview);
        mLNFList.setHasFixedSize(true);
        mLNFList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d("Category", Long.toString(dataSnapshot.getChildrenCount()));
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    arrList.add(snapshot.child("name").getValue(String.class));
                }
                Log.d("Test", Long.toString(arrList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<LNF, LNFViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LNF, LNFViewHolder>(LNF.class, R.layout.lnf_row, LNFViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(LNFViewHolder viewHolder, LNF model, final int position) {

                final String catKey = getRef(position).getKey().toString();
                viewHolder.setName(model.getName());
                viewHolder.setCount("Total : " + model.getCount());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        n = (String) arrList.get(position);
                        Toast.makeText(LNFPage.this, n, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent (LNFPage.this, LNFItemPage.class);
                        intent.putExtra("cat", n);
                        startActivity(intent);
                    }
                });
            }
        };

        mLNFList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class LNFViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public LNFViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){

            TextView catName = (TextView) mView.findViewById(R.id.catName);
            catName.setText(name);
        }

        public void setCount(String count){
            TextView catCount = (TextView) mView.findViewById(R.id.catCount);
            catCount.setText(count);
        }

        public void setImage(Context ctx, String image){
            ImageView catImage = (ImageView) mView.findViewById(R.id.catImage);
            Picasso.with(ctx).load(image).into(catImage);
        }
    }
}

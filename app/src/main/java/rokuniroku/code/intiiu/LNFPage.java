package rokuniroku.code.intiiu;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class LNFPage extends AppCompatActivity {


    private RecyclerView mLNFList;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnfpage);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Category");
        mDatabase.keepSynced(true);

        mLNFList = (RecyclerView)findViewById(R.id.myrecyclerview);
        mLNFList.setHasFixedSize(true);
        mLNFList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<LNF, LNFViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LNF, LNFViewHolder>(LNF.class, R.layout.lnf_row, LNFViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(LNFViewHolder viewHolder, LNF model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setCount(model.getCount());
                viewHolder.setImage(getApplicationContext(), model.getImage());
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

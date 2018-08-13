package rokuniroku.newinti.intiiu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventAnnItemPage extends AppCompatActivity {

    private ImageView imageViewBanner;
    private TextView textViewBanner, textViewTitle, textViewClub, textViewDateTime,textViewDate, textViewTime,
            textViewVenue, textViewContent, facebookUrl;

    private FirebaseStorage dbStorage;
    private StorageReference rootStorage;

    private EventAnn annObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_ann_item_page);

        rootStorage = dbStorage.getInstance().getReference();

        imageViewBanner = (ImageView) findViewById(R.id.imageViewBanner);
        textViewBanner = (TextView) findViewById(R.id.textViewBanner);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewClub = (TextView) findViewById(R.id.textViewClub);
        textViewDateTime = (TextView) findViewById(R.id.textViewDateTime);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewVenue = (TextView) findViewById(R.id.textViewVenue);
        textViewContent = (TextView) findViewById(R.id.textViewContent);
        facebookUrl = (TextView) findViewById(R.id.facebookUrl);

        //get the passing object
        annObj = (EventAnn)getIntent().getSerializableExtra("EventAnnouncement");

        //get all the fields
        textViewTitle.setText(annObj.getTitle());
        textViewClub.setText(("by " + annObj.getClub()));

        if(annObj.getDateStart().equals(annObj.getDateEnd())){
            textViewDate.setText("Date: " + annObj.getDateStart());
            textViewTime.setText("Time: " + annObj.getTimeStart() + " - " + annObj.getTimeEnd());
            textViewDate.setVisibility((View.VISIBLE));
            textViewTime.setVisibility((View.VISIBLE));
            textViewDateTime.setVisibility(View.GONE);
        }else{
            textViewDateTime.setText(annObj.getDateStart() + "  " + annObj.getTimeStart() + " - "
                    + annObj.getDateEnd() + "  " + annObj.getTimeEnd());
            textViewDate.setVisibility((View.GONE));
            textViewTime.setVisibility((View.GONE));
            textViewDateTime.setVisibility(View.VISIBLE);
        }
        textViewVenue.setText("Venue: " + annObj.getVenue());
        textViewContent.setText(annObj.getContent());

        //get facebook link
        if(annObj.getUrl().equals("empty"))
            facebookUrl.setVisibility(View.GONE);
        else{
            facebookUrl.setText(annObj.getUrl());
            facebookUrl.setVisibility(View.VISIBLE);
        }

        //get Image
        if(annObj.getBanner().equals("default") == false){
            rootStorage = dbStorage.getInstance().getReference().child("Announcement").child("EventAnn").child(annObj.getBanner());

            Glide.with(EventAnnItemPage.this)
                    .using(new FirebaseImageLoader())
                    .load(rootStorage)
                    .into(imageViewBanner);

            textViewBanner.setVisibility(View.GONE);
        }else {
            textViewBanner.setVisibility(View.VISIBLE);
        }

    }
}

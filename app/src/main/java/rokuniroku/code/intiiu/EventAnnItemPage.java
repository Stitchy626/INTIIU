package rokuniroku.code.intiiu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventAnnItemPage extends AppCompatActivity {

    private ImageView imageViewBanner;
    private TextView textViewTitle, textViewClub, textViewDate, textViewTime, textViewVenue, textViewContent;

    private FirebaseStorage dbStorage;
    private StorageReference rootStorage;

    private EventAnn annObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_ann_item_page);

        rootStorage = dbStorage.getInstance().getReference();

        imageViewBanner = (ImageView) findViewById(R.id.imageViewBanner);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewClub = (TextView) findViewById(R.id.textViewClub);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewVenue = (TextView) findViewById(R.id.textViewVenue);
        textViewContent = (TextView) findViewById(R.id.textViewContent);

        //get the passing object
        annObj = (EventAnn)getIntent().getSerializableExtra("EventAnnouncement");

        //get all the fields
        textViewTitle.setText(annObj.getTitle());
        textViewClub.setText(("by " + annObj.getClub()));
        textViewDate.setText("Date: " + annObj.getDateStart() + " - " + annObj.getDateEnd());
        textViewTime.setText("Time: " + annObj.getTimeStart() + " - " + annObj.getTimeEnd());
        textViewVenue.setText("Venue: " + annObj.getVenue());
        textViewContent.setText(annObj.getContent());

        //get Image
        rootStorage = dbStorage.getInstance().getReference().child("Announcement").child("EventAnn").child(annObj.getBanner());

        Glide.with(EventAnnItemPage.this)
                .using(new FirebaseImageLoader())
                .load(rootStorage)
                .into(imageViewBanner);

    }
}

package rokuniroku.code.intiiu;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class INTIAnnItemPage extends AppCompatActivity {

    private ImageView imageViewBanner;
    private TextView textViewTitle, textViewCat, textViewDate, textViewTime, textViewContent;

    private FirebaseStorage storageFire;
    private StorageReference storageRef;

    private INTIAnn annObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intiann_item_page);

        storageRef = storageFire.getInstance().getReference();

        imageViewBanner = (ImageView) findViewById(R.id.imageViewBanner);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewCat = (TextView) findViewById(R.id.textViewCat);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewContent = (TextView) findViewById(R.id.textViewContent);

        //get the passing object
        annObj = (INTIAnn)getIntent().getSerializableExtra("INTIAnnouncement");

        //get all the fields
        textViewTitle.setText(annObj.getTitle());
        textViewCat.setText(("by " + annObj.getCategory()));
        textViewDate.setText("Date: " + annObj.getDateOccur());
        textViewTime.setText("Time: " + annObj.getTimeOccur());
        textViewContent.setText(annObj.getContent());

        //get Image
        storageRef = storageRef.child("Announcement").child("INTIAnn").child(annObj.getBanner());

        Glide.with(INTIAnnItemPage.this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(imageViewBanner);

    }
}

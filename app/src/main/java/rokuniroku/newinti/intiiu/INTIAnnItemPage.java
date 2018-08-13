package rokuniroku.newinti.intiiu;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class INTIAnnItemPage extends AppCompatActivity {

    RelativeLayout layoutBanner;

    private ImageView imageViewBanner;
    private TextView textViewTitle, textViewCat, textViewDateTime,textViewDate, textViewTime, textViewVenue, textViewContent;

    private FirebaseStorage storageFire;
    private StorageReference storageRef;

    private INTIAnn annObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intiann_item_page);

        storageRef = storageFire.getInstance().getReference();

        layoutBanner = (RelativeLayout) findViewById(R.id.layoutBanner);

        imageViewBanner = (ImageView) findViewById(R.id.imageViewBanner);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewCat = (TextView) findViewById(R.id.textViewCat);
        textViewDateTime = (TextView) findViewById(R.id.textViewDateTime);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewVenue = (TextView) findViewById(R.id.textViewVenue);
        textViewContent = (TextView) findViewById(R.id.textViewContent);

        //get the passing object
        annObj = (INTIAnn)getIntent().getSerializableExtra("INTIAnnouncement");

        //get all the fields
        textViewTitle.setText(annObj.getTitle());
        textViewCat.setText(("by " + annObj.getCategory()));

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

        if(annObj.getBanner().equals("default")){
            if(annObj.getCategory().equals("ACO"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_aco);
            else if(annObj.getCategory().equals("ADCO"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_aro);
            else if(annObj.getCategory().equals("AFM"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_afmco);
            else if(annObj.getCategory().equals("CAE"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_cae);
            else if(annObj.getCategory().equals("COLAL"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_colal);
            else if(annObj.getCategory().equals("CC"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_cc);
            else if(annObj.getCategory().equals("CS"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_cs);
            else if(annObj.getCategory().equals("EC"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_ec);
            else if(annObj.getCategory().equals("FEQS"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_feqs);
            else if(annObj.getCategory().equals("FHLS"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_fhls);
            else if(annObj.getCategory().equals("FITS"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_fits);
            else if(annObj.getCategory().equals("FOBCAL"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_fobcal);
            else if(annObj.getCategory().equals("FinanceScholarship"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_fns);
            else if(annObj.getCategory().equals("INSO"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_it);
            else if(annObj.getCategory().equals("ISS"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_iss);
            else if(annObj.getCategory().equals("Library"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_lib);
            else if(annObj.getCategory().equals("OAR"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_oar);
            else if(annObj.getCategory().equals("ORDC"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_ordc);
            else if(annObj.getCategory().equals("RO"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_ro);
            else if(annObj.getCategory().equals("Safety & Security"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_sns);
            else if(annObj.getCategory().equals("SAO"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_sao);
            else if(annObj.getCategory().equals("TL"))
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_tnl);
            else
                imageViewBanner.setImageResource(R.mipmap.ic_intiann_faculty_proxy);

            imageViewBanner.getLayoutParams().height = 400;
            layoutBanner.setBackgroundResource(0);
        }else {
            storageRef = storageRef.child("Announcement").child("INTIAnn").child(annObj.getBanner());

            Glide.with(INTIAnnItemPage.this)
                    .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .into(imageViewBanner);
        }

    }
}

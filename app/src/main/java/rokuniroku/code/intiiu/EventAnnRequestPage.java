package rokuniroku.code.intiiu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventAnnRequestPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private FirebaseDatabase dbDatabase;
    private FirebaseStorage dbStorage;
    private DatabaseReference rootDatabase;
    private StorageReference rootStorage;


    private EditText editTextTitle, editTextContent;
    private TextView textViewStartDate, textViewEndDate, textViewStartTime, textViewEndTime,
            textViewImageTitle, textViewImageDateTime;

    private Button buttonSetTitle, buttonDPStart,buttonDPEnd, buttonTPStart, buttonTPEnd, buttonUploadImage, buttonSubmit;
    private ImageView imageViewBanner;

    private SimpleDateFormat dateFormat, timeFormat;
    private Date dateStart, dateEnd;

    private Uri uri;

    private String sTimeStart, sTimeEnd, sDS, sDE, sTS, sTE;
    private boolean bIsDateStart, bIsTimeStart;

    final private int GALLERY_INTENT = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_ann_request_page);

        rootDatabase = dbDatabase.getInstance().getReference().child("Announcement").child("EventAnn");
        rootStorage = dbStorage.getInstance().getReference().child("Announcement").child("EventAnn");

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextContent = (EditText) findViewById(R.id.editTextContent);

        textViewStartDate = (TextView) findViewById(R.id.textViewStartDate);
        textViewEndDate = (TextView) findViewById(R.id.textViewEndDate);
        textViewStartTime = (TextView) findViewById(R.id.textViewStartTime);
        textViewEndTime = (TextView) findViewById(R.id.textViewEndTime);
        textViewImageTitle = (TextView) findViewById(R.id.textViewImageTitle);
        textViewImageDateTime = (TextView) findViewById(R.id.textViewImageDateTime);

        buttonSetTitle = (Button) findViewById(R.id.buttonSetTitle);
        buttonDPStart = (Button) findViewById(R.id.buttonDPStart);
        buttonDPEnd = (Button) findViewById(R.id.buttonDPEnd);
        buttonTPStart = (Button) findViewById(R.id.buttonTPStart);
        buttonTPEnd = (Button) findViewById(R.id.buttonTPEnd);
        buttonUploadImage = (Button) findViewById(R.id.buttonUploadImage);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        imageViewBanner = (ImageView) findViewById(R.id.imageViewBanner);


        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("HH:mm");

        sDS = "27/05/2018";
        sDE = "28/06/2018";
        sTS = "16:00";
        sTE = "15:59";


        buttonSetTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewImageTitle.setText(editTextTitle.getText().toString());
                closeKeyboard();
            }
        });


        buttonDPStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                bIsDateStart = true;
                closeKeyboard();
            }
        });
        buttonDPEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewStartDate.getText().toString().equals("")){
                    Toast.makeText(EventAnnRequestPage.this, "Event start date has to be selected first!", Toast.LENGTH_LONG).show();
                }
                else {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(getSupportFragmentManager(), "date picker");
                    bIsDateStart = false;
                }
                closeKeyboard();
            }
        });
        buttonTPStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                bIsTimeStart = true;
                closeKeyboard();
            }
        });

        buttonTPEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewEndDate.getText().toString().equals("") || textViewStartTime.getText().toString().equals("")){
                    Toast.makeText(EventAnnRequestPage.this, "Event end date and start time has to be selected first!", Toast.LENGTH_LONG).show();
                }else {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    bIsTimeStart = false;
                }
                closeKeyboard();
            }
        });

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(intent.ACTION_PICK);
                intent.setType("image/*");//allow only images to be selected
                startActivityForResult(intent, GALLERY_INTENT);
                closeKeyboard();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRequest();
            }
        });
        //PushEventAnn();
    }

    //Hard to control the milliseconds, milliseconds issue TIPs: PARSE
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        Date dateToday = c.getTime();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = dateFormat.format(c.getTime());

        if(bIsDateStart == true){
            dateStart = c.getTime();
            if(dateStart.equals(dateToday) || dateStart.after(dateToday)){
                textViewStartDate.setText(currentDate);
                sDS = currentDate;
                textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);

            }else
                Toast.makeText(EventAnnRequestPage.this, "Announcement cannot be before today's date right", Toast.LENGTH_LONG).show();

        }else if(bIsDateStart == false){
            dateEnd = c.getTime();
            if(dateEnd.equals(dateStart) || dateEnd.after(dateStart)){
                textViewEndDate.setText(currentDate);
                sDE = currentDate;
                textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);
            }else
                Toast.makeText(EventAnnRequestPage.this, "Announcement end date can't be until before the start date right", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);


        String sFullTime = timeFormat.format(c.getTime());

        if(bIsTimeStart == true) {
            sTimeStart = timeFormat.format(c.getTime());
            textViewStartTime.setText(sFullTime);
            sTS = sFullTime;
            textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);
        }
        else if(bIsTimeStart == false) {
            sTimeEnd = timeFormat.format(c.getTime());

            if(textViewStartDate.getText().toString().equals(textViewEndDate.getText().toString())) {
                if (sTimeEnd.compareTo(sTimeStart) > 0) {
                    textViewEndTime.setText(sFullTime);
                    sTE = sFullTime;
                    textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);
                }
                 else
                    Toast.makeText(EventAnnRequestPage.this, "Announcement end time can't be until before the start time right", Toast.LENGTH_LONG).show();
            }else {
                textViewEndTime.setText(sFullTime);
                sTE = sFullTime;
                textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);
            }
        }
    }

    private void submitRequest(){
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();

        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

        String id = rootDatabase.push().getKey();

        EventAnn ann = new EventAnn(editTextTitle.getText().toString(), "Chicken Club",
                dateFormat.format(today), timeFormat.format(today), editTextContent.getText().toString(),
                id, textViewStartDate.getText().toString(), textViewEndDate.getText().toString(),
                textViewStartTime.getText().toString(), textViewEndTime.getText().toString(), "approved");

        rootDatabase.child(id).setValue(ann);

        rootStorage = rootStorage.child(id);
        rootStorage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EventAnnRequestPage.this, "Uploaded", Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            uri = data.getData();

            imageViewBanner.setImageURI(uri);

            //imageViewBanner.setBackgroundColor(Color.parseColor("#80000000"));

            /*try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewBanner.setImageBitmap(bitmap);

            }catch(IOException e){
                Log.d("Error =", e.getMessage().toString());
            }*/

        }

    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /*private void PushEventAnn() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");//date format
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");//time format

        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

        for (int x = 1; x <= 10; x++) {

            Date today = calendar.getTime();

            calendar.add(Calendar.DAY_OF_YEAR, 1);

            String id = eventAnnRef.push().getKey();

            EventAnn ann = new EventAnn(id, "Chicken Club", dateFormat.format(today), timeFormat.format(today), "CONTENT AREA BLA BLA", id, "15/07/2018", "16/07/2018", "17:00", "19:00", "approved");

            eventAnnRef.child(id).setValue(ann);
            //path.child(id).putFile("")


        }
    }*/
}

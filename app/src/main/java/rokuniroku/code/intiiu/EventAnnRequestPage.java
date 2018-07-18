package rokuniroku.code.intiiu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    private Calendar calendar;
    private SimpleDateFormat dateFormat, timeFormat;
    //private SimpleDateFormat dateFormatGMT08, timeFormatGMT08;
    //private Date dateStart, dateEnd, dateToday, timeStart, timeEnd;

    private Uri uri;

    private String sDS, sDE, sTS, sTE;
    private boolean bIsDateStart, bIsTimeStart, bGo;
    private int iImageCount;

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

        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("HH:mm");

        uri = null;

        sDS = "27/05/2018";
        sDE = "28/06/2018";
        sTS = "16:00";
        sTE = "15:59";

        bIsDateStart = true;
        bIsTimeStart = true;

        bGo = false;

        iImageCount = 0;


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

                textViewStartDate.setError(null);

                textViewEndDate.setText("");
                textViewStartTime.setText("");
                textViewEndTime.setText("");

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                bIsDateStart = true;

                closeKeyboard();
            }
        });
        buttonDPEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(textViewStartDate.getText().toString().isEmpty()){
                    textViewStartDate.setError("Error");
                }
                if(textViewStartDate.getText().toString().isEmpty() == false) {
                    textViewEndDate.setError(null);

                    textViewStartTime.setText("");
                    textViewEndTime.setText("");

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

                if(textViewStartDate.getText().toString().isEmpty())
                    textViewStartDate.setError("Error");

                if(textViewEndDate.getText().toString().isEmpty())
                    textViewEndDate.setError("Error");

                if(textViewStartDate.getText().toString().isEmpty() == false && textViewEndDate.getText().toString().isEmpty() == false) {
                    textViewStartTime.setError(null);

                    textViewEndTime.setText("");

                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    bIsTimeStart = true;
                }
                closeKeyboard();
            }
        });

        buttonTPEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(textViewStartDate.getText().toString().isEmpty())
                    textViewStartDate.setError("Error");

                if(textViewEndDate.getText().toString().isEmpty())
                    textViewEndDate.setError("Error");

                if(textViewStartTime.getText().toString().isEmpty())
                    textViewStartTime.setError("Error");

                if (textViewStartDate.getText().toString().isEmpty() == false && textViewEndDate.getText().toString().isEmpty() == false
                        && textViewStartTime.getText().toString().isEmpty() == false) {
                    textViewEndTime.setError(null);

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
    }


    //DONE
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String sSelectedDate = dateFormat.format(c.getTime());

        if(bIsDateStart == true){
            textViewStartDate.setText(sSelectedDate);
            sDS = sSelectedDate;
            textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);

        }else if(bIsDateStart == false){
            textViewEndDate.setText(sSelectedDate);
            sDE = sSelectedDate;
            textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);
        }
    }

    //DONE
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        String sSelectedTime = timeFormat.format(c.getTime());

        if(bIsTimeStart == true) {

            textViewStartTime.setText(sSelectedTime);
            sTS = sSelectedTime;
            textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);

        } else if(bIsTimeStart == false) {

            textViewEndTime.setText(sSelectedTime);
            sTE = sSelectedTime;
            textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            uri = data.getData();
            imageViewBanner.setImageURI(uri);
            iImageCount ++;
        }
    }


    //DONE
    private void submitRequest(){

        if(editTextTitle.getText().toString().isEmpty())
            editTextTitle.setError("An announcement with no title?");

        if(textViewStartDate.getText().toString().isEmpty())
            textViewStartDate.setError("Error");

        if(textViewEndDate.getText().toString().isEmpty())
            textViewEndDate.setError("Error");

        if(textViewStartTime.getText().toString().isEmpty())
            textViewStartTime.setError("Error");

        if(textViewEndTime.getText().toString().isEmpty())
            textViewEndTime.setError("Error");

        if(editTextContent.getText().toString().isEmpty())
            editTextContent.setError("Empty content");

        if(editTextTitle.getText().toString().isEmpty() == false && textViewStartDate.getText().toString().isEmpty() == false
                && textViewEndDate.getText().toString().isEmpty() == false && textViewStartTime.getText().toString().isEmpty() == false
                 && textViewEndTime.getText().toString().isEmpty() == false && editTextContent.getText().toString().isEmpty() == false){

            if(iImageCount == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(EventAnnRequestPage.this);
                builder.setTitle("Image Banner Confirmation");
                builder.setMessage("Do you want to use the default banner image? If no, please select an image");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bGo = true;
                                SubmitReal();
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bGo = false;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }else
                SubmitReal();
        }
    }

    private void SubmitReal(){
        if(bGo == true){

            Calendar c = Calendar.getInstance();
            Date today = c.getTime();

            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

            String id = rootDatabase.push().getKey();
            String banner = id;

            if(iImageCount == 0)
                banner = "default";

            EventAnn ann = new EventAnn(editTextTitle.getText().toString(), "Chicken Club",
                    dateFormat.format(today), timeFormat.format(today), editTextContent.getText().toString(),
                    banner, textViewStartDate.getText().toString(), textViewEndDate.getText().toString(),
                    textViewStartTime.getText().toString(), textViewEndTime.getText().toString(), "approved");

            rootDatabase.child(id).setValue(ann);

            if(banner != "default") {
                rootStorage = rootStorage.child(id);
                rootStorage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EventAnnRequestPage.this, "Uploaded Perfecto", Toast.LENGTH_LONG).show();
                    }
                });
            }else
                Toast.makeText(EventAnnRequestPage.this, "Submitted", Toast.LENGTH_LONG).show();

        }
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

package rokuniroku.code.intiiu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventAnnRequestPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase dbDatabase;
    private FirebaseStorage dbStorage;
    private DatabaseReference rootDatabase, databaseUser;
    private StorageReference rootStorage;

    private LinearLayout layout3, layout4, layout5, layout6;

    private EditText editTextTitle, editTextVenue, editTextContent, editTextFBLink;
    private TextView textViewStartDate, textViewEndDate, textViewStartTime, textViewEndTime,
            textViewImageTitle, textViewImageDateTime;

    private Button buttonSetTitle, buttonDPStart,buttonDPEnd, buttonTPStart, buttonTPEnd,
            buttonUploadImage, buttonLight, buttonDark, buttonSubmit;
    private ImageView imageViewBanner;

    private SimpleDateFormat dateFormat, timeFormat;

    private Uri uri;
    private ProgressDialog progressDialog;

    private String sDS, sDE, sTS, sTE, sUserName;
    private boolean bIsDateStart, bIsTimeStart, bIsLight, bIsPoint;
    private int iImageCount;

    final private int GALLERY_INTENT = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_ann_request_page);

        mUser = mAuth.getInstance().getCurrentUser();
        String email = mUser.getEmail().toString();
        int startZ = 0;

        for (int x = 0; x < email.length(); x++) {
            if (email.charAt(x) == '@') {
                startZ = x;
                break;
            }
        }

        String frontEmail = email.substring(0, startZ).trim();
        rootDatabase = dbDatabase.getInstance().getReference().child("Announcement").child("EventAnn");
        rootStorage = dbStorage.getInstance().getReference().child("Announcement").child("EventAnn");
        databaseUser = dbDatabase.getInstance().getReference().child("Club").child(frontEmail);

        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sUserName = dataSnapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        layout3 = (LinearLayout) findViewById(R.id.layout3);
        layout4 = (LinearLayout) findViewById(R.id.layout4);
        layout5 = (LinearLayout) findViewById(R.id.layout5);
        layout6 = (LinearLayout) findViewById(R.id.layout6);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextVenue = (EditText) findViewById(R.id.editTextVenue);
        editTextContent = (EditText) findViewById(R.id.editTextContent);
        editTextFBLink = (EditText) findViewById(R.id.editTextFBLink);

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
        buttonLight = (Button) findViewById(R.id.buttonLight);
        buttonDark = (Button) findViewById(R.id.buttonDark);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        imageViewBanner = (ImageView) findViewById(R.id.imageViewBanner);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("HH:mm");

        uri = null;
        progressDialog = new ProgressDialog(EventAnnRequestPage.this);

        sDS = "27/05/2018";
        sDE = "28/06/2018";
        sTS = "16:00";
        sTE = "15:59";

        bIsDateStart = true;
        bIsTimeStart = true;
        bIsLight = true;
        bIsPoint = false;

        iImageCount = 0;

        //Start
        buttonLight.setSelected(true);


        buttonSetTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewImageTitle.setText(editTextTitle.getText().toString());
                closeKeyboard();
                Toast.makeText(EventAnnRequestPage.this, "Preview banner updated", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDPStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bIsPoint == true){
                    textViewEndDate.setError(null);
                    textViewStartDate.setError(null);
                }else {
                    textViewStartDate.setError(null);

                    textViewEndDate.setText("");
                    textViewStartTime.setText("");
                    textViewEndTime.setText("");
                }

                bIsDateStart = true;

                DatePickerRun();

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
                    if(bIsPoint == true){
                        textViewEndDate.setError(null);
                        textViewStartDate.setError(null);
                    }else {
                        textViewEndDate.setError(null);

                        textViewStartTime.setText("");
                        textViewEndTime.setText("");
                    }

                    bIsDateStart = false;

                    DatePickerRun();
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
                    if(bIsPoint == true){
                        textViewStartTime.setError(null);
                        textViewEndTime.setError(null);
                    }else {
                        textViewStartTime.setError(null);

                        textViewEndTime.setText("");
                    }

                    bIsTimeStart = true;

                    TimePickerRun();
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
                    if(bIsPoint == true){
                        textViewStartTime.setError(null);
                        textViewEndTime.setError(null);
                    }else {
                        textViewEndTime.setError(null);
                    }

                    bIsTimeStart = false;

                    TimePickerRun();
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

        buttonLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLight.setSelected(true);
                buttonDark.setSelected(false);
                textViewImageTitle.setTextColor(Color.parseColor("#000000"));
                imageViewBanner.setBackgroundResource(R.drawable.default_banner);
                bIsLight = true;
            }
        });

        buttonDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLight.setSelected(false);
                buttonDark.setSelected(true);
                textViewImageTitle.setTextColor(Color.parseColor("#FFFFFF"));
                imageViewBanner.setBackgroundResource(R.drawable.default_banner_dark);
                bIsLight = false;
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitRequest();
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            uri = data.getData();
            imageViewBanner.setImageURI(uri);
            iImageCount ++;
        }
    }

    private void DatePickerRun(){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(EventAnnRequestPage.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String sSelectedDate = dateFormat.format(c.getTime());
                Log.d("Test", sSelectedDate);

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
        },year,month,day);

        datePicker.show();
    }

    private void TimePickerRun(){

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(EventAnnRequestPage.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);

                String sSelectedTime = timeFormat.format(c.getTime());
                Log.d("Test", sSelectedTime);

                if (bIsTimeStart == true) {

                    textViewStartTime.setText(sSelectedTime);
                    sTS = sSelectedTime;
                    textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);

                } else if (bIsTimeStart == false) {

                    textViewEndTime.setText(sSelectedTime);
                    sTE = sSelectedTime;
                    textViewImageDateTime.setText(sDS + "  " + sTS + " - " + sDE + "  " + sTE);
                }
            }
        },hour, minute, true);

        timePicker.show();
    }

    //Validation and erro checking done here
    private void SubmitRequest(){

        Boolean bValid = true;
        String sErrorMessage = null;
        Date dateStart = null, dateEnd = null, timeStart = null, timeEnd = null;

        if(editTextTitle.getText().toString().isEmpty())
            editTextTitle.setError("Announcement title has to be included");

        if(editTextVenue.getText().toString().isEmpty())
            editTextVenue.setError("Venue has to be included");

        if(textViewStartDate.getText().toString().isEmpty()) {
            layout3.requestFocus();
            textViewStartDate.setError("No Date");
        }

        if(textViewEndDate.getText().toString().isEmpty()) {
            layout4.requestFocus();
            textViewEndDate.setError("No Date");
        }

        if(textViewStartTime.getText().toString().isEmpty()) {
            layout5.requestFocus();
            textViewStartTime.setError("No Time");
        }

        if(textViewEndTime.getText().toString().isEmpty()) {
            layout6.requestFocus();
            textViewEndTime.setError("No Time");
        }

        if(editTextContent.getText().toString().isEmpty())
            editTextContent.setError("Empty content");

        if(editTextTitle.getText().toString().isEmpty() == false && editTextVenue.getText().toString().isEmpty() == false && textViewStartDate.getText().toString().isEmpty() == false
                && textViewEndDate.getText().toString().isEmpty() == false && textViewStartTime.getText().toString().isEmpty() == false
                && textViewEndTime.getText().toString().isEmpty() == false && editTextContent.getText().toString().isEmpty() == false) {

            bIsPoint = true;

            try {
                dateStart = dateFormat.parse(textViewStartDate.getText().toString());
                dateEnd = dateFormat.parse(textViewEndDate.getText().toString());
                timeStart = timeFormat.parse(textViewStartTime.getText().toString());
                timeEnd = timeFormat.parse(textViewEndTime.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dateStart.after(dateEnd)) {
                textViewStartDate.requestFocus();
                textViewStartDate.setError("Start date pass end date");
                textViewEndDate.setError("Error");
                sErrorMessage = "Invalid Date Selected";
                Toast.makeText(EventAnnRequestPage.this, sErrorMessage, Toast.LENGTH_LONG).show();
                bValid = false;
            }
            if (dateStart.equals(dateEnd)) {
                if (timeStart.after(timeEnd)) {
                    textViewStartTime.requestFocus();
                    textViewStartTime.setError("Start time pass end time");
                    textViewEndTime.setError("Error");
                    sErrorMessage = "Invalid Time Selected";
                    Toast.makeText(EventAnnRequestPage.this, sErrorMessage, Toast.LENGTH_LONG).show();
                    bValid = false;
                }
            }

            if (bValid == true) {
                final AlertDialog.Builder builderSubmit = new AlertDialog.Builder(EventAnnRequestPage.this);
                builderSubmit.setTitle("Submission Comfirmation");
                builderSubmit.setMessage("Are you sure you want to submit?");
                builderSubmit.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SubmitReal();
                            }
                        });
                builderSubmit.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


                if (iImageCount == 0) {
                    AlertDialog.Builder builderBanner = new AlertDialog.Builder(EventAnnRequestPage.this);
                    builderBanner.setTitle("Image Banner Confirmation");
                    builderBanner.setMessage("Do you want to use the default banner image? If no, please select an image");
                    builderBanner.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    builderSubmit.create().show();
                                }
                            });
                    builderBanner.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    builderBanner.create().show();
                } else
                    builderSubmit.create().show();
            }
        }
    }

    //All error checking, validation and comfirmation is done in SubmitRequest function, this function only push data to database.
    private void SubmitReal(){

        progressDialog.setMessage("Submitting ....");
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                Date today = c.getTime();

                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

                String id = rootDatabase.push().getKey();
                String banner = id;
                String background = "light";
                String FB = "empty";

                if(iImageCount == 0)
                    banner = "default";

                if(bIsLight == false)
                    background = "dark";

                if(editTextFBLink.getText().toString().trim().isEmpty() == false)
                    FB = editTextFBLink.getText().toString();

                EventAnn ann = new EventAnn(editTextTitle.getText().toString(), editTextVenue.getText().toString(), sUserName,//getDisplayName() cant be use becos its not a real email
                        dateFormat.format(today), timeFormat.format(today), "empty", editTextContent.getText().toString(),
                        FB, banner, background, textViewStartDate.getText().toString(), textViewEndDate.getText().toString(),
                        textViewStartTime.getText().toString(), textViewEndTime.getText().toString(), "empty","pending");// status using approve for testing purposes

                rootDatabase.child(id).setValue(ann);

                if(banner != "default") {
                    rootStorage = rootStorage.child(id);
                    rootStorage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            handler.sendEmptyMessage(0);
                        }
                    });
                }else {
                    handler.sendEmptyMessage(0);
                }

            }
        }).start();
    }

    //A handler to handle the thread when finish
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            Toast.makeText(EventAnnRequestPage.this, "Request submitted", Toast.LENGTH_LONG).show();
        }
    };

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

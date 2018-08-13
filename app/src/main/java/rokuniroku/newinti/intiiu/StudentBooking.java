package rokuniroku.newinti.intiiu;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class StudentBooking extends AppCompatActivity {

    private Spinner spinnerFacilityType, spinnerFacility, spinnerStartDate, spinnerStartTime;
    private Button buttonOneHour, buttonTwoHour, buttonCheck;

    private FirebaseDatabase database;
    private DatabaseReference rootRef, studentBookRef;

    //Class Variable
    private boolean bOneHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_booking);

        rootRef = database.getInstance().getReference();
        studentBookRef = rootRef.child("Booking");

        spinnerFacilityType = (Spinner) findViewById(R.id.spinnerFacilityType);
        spinnerFacility = (Spinner) findViewById(R.id.spinnerFacility);
        spinnerStartDate = (Spinner) findViewById(R.id.spinnerStartDate);
        spinnerStartTime = (Spinner) findViewById(R.id.spinnerStartTime);

        buttonOneHour = (Button) findViewById(R.id.buttonOneHour);
        buttonTwoHour = (Button) findViewById(R.id.buttonTwoHour);
        buttonCheck = (Button) findViewById(R.id.buttonCheck);

        bOneHour = true;

        PopulateFacilityType();
        PopulateDate();
        CheckDuration();
        PopulateTime();

        spinnerFacilityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PopulateFacility();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAvailability();
            }
        });
    }

    private void PopulateFacilityType(){

        String[] list = getResources().getStringArray(R.array.facility_type);// can use list also for dynamic adding like PopulateFacility
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinnerFacilityType.setAdapter(adapter);
    }

    private void PopulateFacility(){

        String sFacilityType = spinnerFacilityType.getSelectedItem().toString();

        List<String> list = new ArrayList<>();

        if(sFacilityType.equals("Rooms"))
            list = Arrays.asList(getResources().getStringArray(R.array.facility_rooms));
        else if(sFacilityType.equals("Sports"))
            list = Arrays.asList(getResources().getStringArray(R.array.facility_sports));
        else if(sFacilityType.equals("Equipments"))
            list = Arrays.asList(getResources().getStringArray(R.array.facility_equipments));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);

        spinnerFacility.setAdapter(adapter);
    }

    private void PopulateDate(){

        ArrayList<String> list = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();//date instance
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");//date format
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

        Date today = calendar.getTime();//today date

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();// tomorrow date

        // add to spinner
        list.add(dateFormat.format(today));
        list.add(dateFormat.format(tomorrow));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);

        spinnerStartDate.setAdapter(adapter);
    }

    private void CheckDuration(){
        buttonOneHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bOneHour = true;
                buttonOneHour.setBackgroundColor(Color.parseColor("#FF9900"));
                buttonTwoHour.setBackgroundResource(android.R.drawable.btn_default);
                PopulateTime();
            }
        });

        buttonTwoHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bOneHour = false;
                buttonOneHour.setBackgroundResource(android.R.drawable.btn_default);
                buttonTwoHour.setBackgroundColor(Color.parseColor("#FF9900"));
                PopulateTime();
            }
        });


    }

    private void PopulateTime(){

        List<String> list = new ArrayList<>();

        if(bOneHour == true)
            list = Arrays.asList(getResources().getStringArray(R.array.normal_booking_start_time_one_hour));
        else if(bOneHour == false)
            list = Arrays.asList(getResources().getStringArray(R.array.normal_booking_start_time_two_hour));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);

        spinnerStartTime.setAdapter(adapter);
    }

    private void CheckAvailability(){
        //check and jump to another page

        String facility = spinnerFacility.getSelectedItem().toString();

        String sChild1, sChild2;

        if(facility.equals("Badminton Court")){
            sChild1 = "MPH Booking";
            sChild2 = "Sport Hall Booking";
        }
        else if(facility.equals("Basketball Court")){
            sChild1 = "ACA";
            //sChi
        }

        Query query1 = studentBookRef.orderByChild("status").equalTo("In Progress");
        Query query2 = studentBookRef.orderByChild("status").equalTo("Approved");
        Query query3 = studentBookRef.orderByChild("status").equalTo("Pending");





    }

}

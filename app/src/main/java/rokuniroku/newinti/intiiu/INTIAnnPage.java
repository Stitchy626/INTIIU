package rokuniroku.newinti.intiiu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class INTIAnnPage extends AppCompatActivity {

    private FirebaseDatabase dbDatabase;
    private DatabaseReference rootDatabase;
    private Query query;

    private TextView textViewCategory;
    private ListView listViewAnn;

    private ArrayList<INTIAnn> annList;

    private SimpleDateFormat dateFormat, dateFormatGMT08;

    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intiann_page);

        myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_intiann_filter));
        setSupportActionBar(myToolbar);

        rootDatabase = dbDatabase.getInstance().getReference().child("Announcement").child("INTIAnn");

        textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        listViewAnn = (ListView) findViewById(R.id.listViewAnn);

        annList = new ArrayList<>();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatGMT08 = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatGMT08.setTimeZone(TimeZone.getTimeZone("GMT+08"));

        PopulateINTIAnn(textViewCategory.getText().toString());
        //PushINTIAnn();

        listViewAnn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(INTIAnnPage.this, INTIAnnItemPage.class);
                intent.putExtra("INTIAnnouncement", annList.get(position));
                startActivity(intent);
            }
        });

    }


    //Filter Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_intiann, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_All:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_ACO:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_ADCO:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_AFM:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_CAE:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_COLAL:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_CC:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_CS:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_EC:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_FEQS:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_FHLS:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_FITS:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_FOBCAL:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_FinanceScholarship:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_INSO:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_ISS:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_Library:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_OAR:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_ORDC:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_RO:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_SafetySecurity:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_SAO:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            case R.id.action_TL:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                textViewCategory.setText(item.getTitle());
                PopulateINTIAnn(item.getTitle().toString());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void PopulateINTIAnn(String category) {

        Calendar calendar = Calendar.getInstance();

        final Date today = calendar.getTime();

        if (category.equals("ALL")) {
            query = rootDatabase;
        } else
            query = rootDatabase.orderByChild("category").equalTo(category);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                annList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    INTIAnn announcement = snapshot.getValue(INTIAnn.class);

                    ArrayList<Date> date = new ArrayList<>();

                    try {
                        date.add(dateFormat.parse(dateFormatGMT08.format(today)));
                        date.add(dateFormat.parse(announcement.getDeleteDate()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (date.get(0).equals(date.get(1)) || date.get(0).after(date.get(1))) {
                        DeleteAnn(snapshot.getKey().toString());
                    } else {
                        annList.add(announcement);
                    }

                }

                //Sort the announcement that latest should be on top
                for (int x = 0; x < annList.size(); x++) {
                    for (int y = 0; y < annList.size() - x - 1; y++) {
                        //YEAR
                        if (annList.get(y).getDateUpload().substring(6, 10).compareTo(annList.get(y + 1).getDateUpload().substring(6, 10)) < 0) {
                            INTIAnn temp = annList.get(y);
                            annList.set(y, annList.get(y + 1));
                            annList.set(y + 1, temp);

                        } else if (annList.get(y).getDateUpload().substring(6, 10).compareTo(annList.get(y + 1).getDateUpload().substring(6, 10)) == 0) {
                            //MONTH
                            if (annList.get(y).getDateUpload().substring(3, 5).compareTo(annList.get(y + 1).getDateUpload().substring(3, 5)) < 0) {
                                INTIAnn temp = annList.get(y);
                                annList.set(y, annList.get(y + 1));
                                annList.set(y + 1, temp);

                            } else if (annList.get(y).getDateUpload().substring(3, 5).compareTo(annList.get(y + 1).getDateUpload().substring(3, 5)) == 0) {
                                //DAY
                                if (annList.get(y).getDateUpload().substring(0, 2).compareTo(annList.get(y + 1).getDateUpload().substring(0, 2)) < 0) {
                                    INTIAnn temp = annList.get(y);
                                    annList.set(y, annList.get(y + 1));
                                    annList.set(y + 1, temp);

                                } else if (annList.get(y).getDateUpload().substring(0, 2).compareTo(annList.get(y + 1).getDateUpload().substring(0, 2)) == 0) {
                                    //HOUR
                                    if (annList.get(y).getTimeUpload().substring(0, 2).compareTo(annList.get(y + 1).getTimeUpload().substring(0, 2)) < 0) {
                                        INTIAnn temp = annList.get(y);
                                        annList.set(y, annList.get(y + 1));
                                        annList.set(y + 1, temp);

                                    } else if (annList.get(y).getTimeUpload().substring(0, 2).compareTo(annList.get(y + 1).getTimeUpload().substring(0, 2)) == 0) {
                                        //MINUTE
                                        if (annList.get(y).getTimeUpload().substring(3, 5).compareTo(annList.get(y + 1).getTimeUpload().substring(3, 5)) < 0) {
                                            INTIAnn temp = annList.get(y);
                                            annList.set(y, annList.get(y + 1));
                                            annList.set(y + 1, temp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                //Add to list view
                INTIAnnAdapter adapter = new INTIAnnAdapter(INTIAnnPage.this, annList);
                listViewAnn.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DeleteAnn(String key) {

        rootDatabase.child(key).removeValue();
    }

    private void PushINTIAnn() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");//date format
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");//time format

        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

        for (int x = 1; x <= 2; x++) {

            Date today = calendar.getTime();
            String id = rootDatabase.push().getKey();

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.add(Calendar.MINUTE, 1);

            INTIAnn ann = new INTIAnn("Notice Fron Security Office", "INTI", "Safety & Security", "08/07/2018", timeFormat.format(today), "23/08/2018",
                    "Dear Students,\n" +
                            "\n" +
                            "Please be informed that you will be denied access to enter IIU premise if you failed to provide your original Student I.D. This is for Security and Safety purpose.\n" +
                            "\n" +
                            "This standard procedure which already exist, will be strictly followed by the security and safety department. Students are given 1 day time period to get their replacement student I.D if they have lost it.\n" +
                            "\n" +
                            "This procedure will take place effective 3rd of August 2018 and providing student I.D via mobile is unacceptable and not valid.", id,
                    "18/08/2018","18/08/2018", "00:00","23:59");

            rootDatabase.child(id).setValue(ann);
        }
    }
}

/**/




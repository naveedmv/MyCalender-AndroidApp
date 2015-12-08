package com.navii.mycalendar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Navii on 11/29/2015.
 */
public class Export extends AppCompatActivity {

    Button bCancel;
    Button bExport;

    TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        tinydb = new TinyDB(getApplicationContext());
        tinydb.getListObject("allevents", MyEvent.class);

        // list view chosen calendar's events
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        final ListView lvExport = (ListView) findViewById(R.id.lvExport);
        lvExport.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        tvTitle.setText("Select events to export :");

        final Spanned[] MycalendarEventList = new Spanned[tinydb.getListObject("allevents", MyEvent.class).size()];
        int i = 0;
        for (MyEvent my_e : tinydb.getListObject("allevents", MyEvent.class)) {

            MycalendarEventList[i] = Html.fromHtml("<b>" + my_e.getDescription());
            i++;
        }

        ArrayAdapter<Spanned> eventsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, MycalendarEventList);
        lvExport.setAdapter(eventsArrayAdapter);

        bCancel = (Button) findViewById(R.id.bCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bExport = (Button) findViewById(R.id.bExport);
        bExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SparseBooleanArray checked = lvExport.getCheckedItemPositions();
                for (int i = 0; i < tinydb.getListObject("allevents", MyEvent.class).size(); i++) {
                    if (checked.get(i)) {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try {
                            Date newstartdate;
                            Date newenddate;
                            newstartdate = df.parse(tinydb.getListObject("allevents", MyEvent.class).get(i).getFrom_date() + " " + tinydb.getListObject("allevents", MyEvent.class).get(i).getStart_time());
                            GregorianCalendar mycalendar = new GregorianCalendar();
                            mycalendar.setTime(newstartdate);
                            GregorianCalendar myendcalendar = new GregorianCalendar();
                            newenddate = df.parse(tinydb.getListObject("allevents", MyEvent.class).get(i).getTo_date() + " " + tinydb.getListObject("allevents", MyEvent.class).get(i).getEnd_time());
                            myendcalendar.setTime(newenddate);
                            Intent intent = new Intent(Intent.ACTION_INSERT)


                                    .setData(CalendarContract.Events.CONTENT_URI)
                                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, mycalendar.getTimeInMillis())
                                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, myendcalendar.getTimeInMillis())
                                    .putExtra(CalendarContract.Events.TITLE, tinydb.getListObject("allevents", MyEvent.class).get(i).getDescription())
                                    .putExtra(CalendarContract.Events.EVENT_LOCATION, tinydb.getListObject("allevents", MyEvent.class).get(i).getLocation());
                            startActivity(intent);

                        } catch (ParseException e) {

                        }

                    }
                }
                Toast.makeText(Export.this, "Events Exported!", Toast.LENGTH_SHORT).show();
                //Intent ImportIntent = new Intent(Export.this, LoggedIn.class);
                //ImportIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//close previous open activities
                //startActivity(ImportIntent);
                finish();
            }
        });
    }
}

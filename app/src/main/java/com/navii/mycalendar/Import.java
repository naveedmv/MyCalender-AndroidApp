package com.navii.mycalendar;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;


/**
 * Created by Navii on 11/28/2015.
 */
public class Import extends AppCompatActivity{

    Button bCancel;
    Button bImport;

    private static final String TAG = "MyCalendar50";
    String exitsStatus = "";
    public String eventAddMessage = "";

    SharedPreferences userprefid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        userprefid = this.getSharedPreferences("UserPrefId", Context.MODE_PRIVATE);

        // calendar providers
        final CalendarProvider calendarProvider = new CalendarProvider(this);
        final List<me.everything.providers.android.calendar.Calendar> calendars = calendarProvider.getCalendars().getList();

        // list view chosen calendar's events
        final Intent intent = getIntent();
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        final ListView lvImport = (ListView) findViewById(R.id.lvImport);
        lvImport.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        final DateFormat formatter_time = new SimpleDateFormat("HH:mm");
        final DateFormat formatter_date = new SimpleDateFormat("yyyy-MM-dd");

        bCancel = (Button) findViewById(R.id.bCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ImportIntent = new Intent(Import.this, LoggedIn.class);
                ImportIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ImportIntent);
                finish();
            }
        });

        bImport = (Button) findViewById(R.id.bImport);
        bImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SparseBooleanArray checked = lvImport.getCheckedItemPositions();
                for (int i = 0; i < calendarProvider.getEvents(intent.getLongExtra("Calendar_ID", 0)).getList().size(); i++) {
                    if (checked.get(i)) {
                        String ce_description = calendarProvider.getEvents(intent.getLongExtra("Calendar_ID", 0)).getList().get(i).title;
                        String ce_location = calendarProvider.getEvents(intent.getLongExtra("Calendar_ID", 0)).getList().get(i).eventLocation;
                        Date ce_dtstart = new Date(calendarProvider.getEvents(intent.getLongExtra("Calendar_ID", 0)).getList().get(i).dTStart);
                        String ce_fromdate = formatter_date.format(ce_dtstart);
                        String ce_starttime = formatter_time.format(ce_dtstart);
                        Date ce_dtend = new Date(calendarProvider.getEvents(intent.getLongExtra("Calendar_ID", 0)).getList().get(i).dTend);
                        String ce_todate = formatter_date.format(ce_dtend);
                        String ce_endtime = formatter_time.format(ce_dtend);

                        // Instantiate the RequestQueue.
                        RequestQueue queue = Volley.newRequestQueue(Import.this);

                        String url = "http://130.233.42.143:8080/addevent";

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("from_date", ce_fromdate);
                        params.put("to_date", ce_todate);
                        params.put("start_time", ce_starttime);
                        params.put("end_time", ce_endtime);
                        params.put("location", ce_location);
                        params.put("description", ce_description);
                        params.put("user", userprefid.getString("user_id", null));

                        // Request a string response from the provided URL.
                        //JsonObjectRequest actually accepts JSONObject as body.
                        JsonObjectRequest JSONRequest = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try{
                                            Log.i(TAG, response.toString());
                                            exitsStatus = response.getString("exists");
                                            eventAddMessage = response.getString("message");

                                            if (exitsStatus == "false"){
                                                Toast.makeText(Import.this, eventAddMessage, Toast.LENGTH_LONG).show();
                                            }
                                            else if(exitsStatus == "true"){
                                                Toast.makeText(Import.this, eventAddMessage, Toast.LENGTH_LONG).show();
                                            }

                                        }catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                            }
                        });

                        // Add the request to the RequestQueue.
                        queue.add(JSONRequest);
                    }
                }
                //Toast.makeText(Import.this, eventAddMessage+":"+j+'\n'+eventExistMessage+":"+k , Toast.LENGTH_LONG).show();
                Intent ImportIntent = new Intent(Import.this, LoggedIn.class);
                ImportIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//close previous open activities
                startActivity(ImportIntent);
                finish();
                }
        });

        // calendar already picked
        if (intent.hasExtra("Calendar_Name") && intent.hasExtra("Calendar_ID")) {
            tvTitle.setText("Select events to import from : " + intent.getStringExtra("Calendar_Name"));

            bCancel.setVisibility(View.VISIBLE);
            bImport.setVisibility(View.VISIBLE);

            final Spanned[] calendarEventList = new Spanned[calendarProvider.getEvents(intent.getLongExtra("Calendar_ID", 0)).getList().size()];
            int i = 0;
            for (me.everything.providers.android.calendar.Event cal_e : calendarProvider.getEvents(intent.getLongExtra("Calendar_ID", 0)).getList()) {

                calendarEventList[i] = Html.fromHtml("<b>" + cal_e.title);
                i++;
            }

            ArrayAdapter<Spanned> eventsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, calendarEventList);
            lvImport.setAdapter(eventsArrayAdapter);

        } else    // calendar not picked
        {
            tvTitle.setText("Choose Calendar:");

            bCancel.setVisibility(View.INVISIBLE);
            bImport.setVisibility(View.INVISIBLE);

            String[] calendarList = new String[calendars.size()];
            int i = 0;
            for (me.everything.providers.android.calendar.Calendar c : calendars) {
                calendarList[i] = c.displayName;
                i++;
            }

            ArrayAdapter<String> calendarArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, calendarList);
            lvImport.setAdapter(calendarArrayAdapter);

            // Pick a calendar and show events
            lvImport.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos,
                                        long id) {

                    String calendar_name = calendars.get(pos).displayName;
                    Long calendar_ID = calendars.get(pos).id;

                    Intent i = new Intent(Import.this, Import.class);
                    i.putExtra("Calendar_Name", calendar_name);
                    i.putExtra("Calendar_ID", calendar_ID);
                    startActivity(i);
                }
            });
        }

    }


}

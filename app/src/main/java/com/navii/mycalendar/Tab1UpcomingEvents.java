package com.navii.mycalendar;

/**
 * Created by Navii on 11/19/2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tab1UpcomingEvents extends Fragment {

    private static final String TAG = "MyCalendar50";
    private ArrayList<MyEvent> usereventslist = new ArrayList<>();

    ListView eventsListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> items;

    SharedPreferences userprefid;
    SharedPreferences userpref;
    //SharedPreferences allevents;

    TinyDB tinydb;

    String UpdateMessage = "";
    String DeleteMessage = "";

    EditText etFromDate, etToDate, etStartTime, etEndTime, etLocation, etDescription;

    private FragmentActivity myContext;
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab1_upcomingevents, container, false);

        userprefid = this.getActivity().getSharedPreferences("UserPrefId", Context.MODE_PRIVATE);
        userpref = this.getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);

        //allevents = this.getActivity().getSharedPreferences("AllEvents", Context.MODE_PRIVATE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String url = "http://130.233.42.143:8080/eventlist?user=" + userprefid.getString("user_id", null);

        JsonArrayRequest JSONARequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parse the JSON array to get description of each event
                        for(int i=0;i<response.length();i++){
                            try {
                                Log.i(TAG, response.toString());
                                //Toast.makeText(getActivity().getBaseContext(), response.toString(), Toast.LENGTH_LONG).show();
                                JSONObject jsonObject=response.getJSONObject(i);

                                MyEvent myE = new MyEvent(
                                        jsonObject.getString("_id"),
                                        jsonObject.getString("from_date"),
                                        jsonObject.getString("to_date"),
                                        jsonObject.getString("start_time"),
                                        jsonObject.getString("end_time"),
                                        jsonObject.getString("location"),
                                        jsonObject.getString("description")
                                        );
                                myE.set_id(jsonObject.getString("_id"));
                                myE.setFrom_date(jsonObject.getString("from_date"));
                                myE.setTo_date(jsonObject.getString("to_date"));
                                myE.setStart_time(jsonObject.getString("start_time"));
                                myE.setEnd_time(jsonObject.getString("end_time"));
                                myE.setLocation(jsonObject.getString("location"));
                                myE.setDescription(jsonObject.getString("description"));
                                usereventslist.add(myE);

                                //just testing if description can be extracted from JSON array and set it on the list view
                                items.add(i,jsonObject.getString("from_date")+" : "+jsonObject.getString("description"));// get it from the database
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();

                        tinydb = new TinyDB(getContext());
                        tinydb.putListObject("allevents", usereventslist);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(JSONARequest);

        FragmentManager fragManager = myContext.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction t = fragManager.beginTransaction();
        t.commit();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventsListView =(ListView)getActivity().findViewById(android.R.id.list);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,items);
        eventsListView.setAdapter(adapter);


        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                //String item = ((TextView) view).getText().toString();

                //Toast.makeText(getActivity().getApplicationContext(), item, Toast.LENGTH_LONG).show();

                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.event_details, null);

                etFromDate = (EditText) promptsView.findViewById(R.id.etFromDate);
                etToDate = (EditText) promptsView.findViewById(R.id.etToDate);
                etStartTime = (EditText) promptsView.findViewById(R.id.etStartTime);
                etEndTime = (EditText) promptsView.findViewById(R.id.etEndTime);
                etLocation = (EditText) promptsView.findViewById(R.id.etLocation);
                etDescription = (EditText) promptsView.findViewById(R.id.etDescription);

                final String e_id = usereventslist.get(position).get_id();
                etFromDate.setText(usereventslist.get(position).getFrom_date());
                etToDate.setText(usereventslist.get(position).getTo_date());
                etStartTime.setText(usereventslist.get(position).getStart_time());
                etEndTime.setText(usereventslist.get(position).getEnd_time());
                etLocation.setText(usereventslist.get(position).getLocation());
                etDescription.setText(usereventslist.get(position).getDescription());

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("Update",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Instantiate the RequestQueue.
                                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                                        final String From_date = etFromDate.getText().toString();
                                        final String To_date = etToDate.getText().toString();
                                        final String Start_time = etStartTime.getText().toString();
                                        final String End_time = etEndTime.getText().toString();
                                        final String Location = etLocation.getText().toString();
                                        final String Description = etDescription.getText().toString();

                                        if (From_date != null && !From_date.equals("") && To_date != null && !To_date.equals("")
                                                && Start_time != null && !Start_time.equals("") && End_time != null && !End_time.equals("")
                                                && Location != null && !Location.equals("") && Description != null && !Description.equals("")) {

                                            String url = "http://130.233.42.143:8080/updateevent/"+e_id;

                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("from_date", From_date);
                                            params.put("to_date", To_date);
                                            params.put("start_time", Start_time);
                                            params.put("end_time", End_time);
                                            params.put("location", Location);
                                            params.put("description", Description);
                                            params.put("user", userprefid.getString("user_id", null));


                                            // Request a JsonObject response from the provided URL.
                                            //JsonObjectRequest actually accepts JSONObject as body.
                                            JsonObjectRequest JSONRequest = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(params),
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            try {
                                                                Log.i(TAG, response.toString());
                                                                UpdateMessage = response.getString("message");

                                                                Toast.makeText(getActivity().getBaseContext(), UpdateMessage, Toast.LENGTH_LONG).show();

                                                            } catch (JSONException e) {
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

                                        Intent LoggedInIntent = new Intent(getActivity().getBaseContext(),LoggedIn.class);
                                        getActivity().finish();
                                        startActivity(LoggedInIntent);

                                        dialog.dismiss();

                                    }
                                })
                        .setNeutralButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Instantiate the RequestQueue.
                                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());


                                        String url = "http://130.233.42.143:8080/deleteevent/"+e_id;

                                        // Request a JsonObject response from the provided URL.
                                        JsonObjectRequest JSONRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            Log.i(TAG, response.toString());
                                                            DeleteMessage = response.getString("message");

                                                            Toast.makeText(getActivity().getBaseContext(), DeleteMessage, Toast.LENGTH_LONG).show();

                                                        } catch (JSONException e) {
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

                                        Intent LoggedInIntent = new Intent(getActivity().getBaseContext(),LoggedIn.class);
                                        getActivity().finish();
                                        startActivity(LoggedInIntent);

                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton("Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

    }
}

package com.navii.mycalendar;

/**
 * Created by Navii on 11/19/2015.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Tab3AddEvent extends Fragment {

    EditText etFromDate, etToDate, etStartTime, etEndTime, etLocation, etDescription;
    Button bAddEvent;

    private static final String TAG = "MyCalendar50";
    String exitsStatus = "";
    String eventAddMessage = "";

    SharedPreferences userprefid;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab3_addevent,container,false);

        etFromDate = (EditText) v.findViewById(R.id.etFromDate);
        etToDate = (EditText) v.findViewById(R.id.etToDate);
        etStartTime = (EditText) v.findViewById(R.id.etStartTime);
        etEndTime = (EditText) v.findViewById(R.id.etEndTime);
        etLocation = (EditText) v.findViewById(R.id.etLocation);
        etDescription = (EditText) v.findViewById(R.id.etDescription);

        userprefid = this.getActivity().getSharedPreferences("UserPrefId", Context.MODE_PRIVATE);

        bAddEvent = (Button) v.findViewById(R.id.bAddEvent);

        bAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        && Location!= null && !Location.equals("") && Description != null && !Description.equals("")){

                    String url = "http://130.233.42.143:8080/addevent";

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("from_date", From_date);
                    params.put("to_date", To_date);
                    params.put("start_time", Start_time);
                    params.put("end_time", End_time);
                    params.put("location", Location);
                    params.put("description", Description);
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
                                            Toast.makeText(getActivity().getBaseContext(), eventAddMessage, Toast.LENGTH_LONG).show();
                                        }
                                        else if(exitsStatus == "true"){
                                            Toast.makeText(getActivity().getBaseContext(), eventAddMessage, Toast.LENGTH_LONG).show();
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

                    etFromDate.setText("");
                    etToDate.setText("");
                    etStartTime.setText("");
                    etEndTime.setText("");
                    etLocation.setText("");
                    etDescription.setText("");


                }
                else{
                    Toast.makeText(getActivity().getBaseContext(), "All Fields Mandatory!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }
}

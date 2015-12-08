package com.navii.mycalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaring Your View and Variables

    Button bLogin;
    TextView SignUplink;
    EditText etUsername, etPassword;

    private static final String TAG = "MyCalendar50";
    String successStatus= "";
    String invalidUserMessage= "";
    String userId= "";
    JSONObject user;

    SharedPreferences userprefid;
    SharedPreferences userpref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bLogin = (Button) findViewById(R.id.bLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        SignUplink = (TextView) findViewById(R.id.tvSignUpLink);

        userprefid = getSharedPreferences("UserPrefId", Context.MODE_PRIVATE);
        userprefid.edit().clear().commit();
        userpref = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        userprefid.edit().clear().commit();
        TinyDB tinydb = new TinyDB(this.getBaseContext());
        tinydb.clear();

        bLogin.setOnClickListener(this);
        SignUplink.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:

                CookieManager manager = new CookieManager();
                CookieHandler.setDefault(manager);

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(this);

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                if (username != null && !username.equals("") && password != null && !password.equals("")) {
                    String url = "http://130.233.42.143:8080/login";

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("password", password);

                    // Request a string response from the provided URL.
                    JsonObjectRequest JSONRequest = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try{
                                        Log.i(TAG,response.toString());
                                        successStatus = response.getString("success");

                                        if (successStatus == "true"){
                                            userId =response.getJSONObject("user").getString("_id");
                                            SharedPreferences.Editor editor = userprefid.edit();
                                            editor.putString("user_id", userId);
                                            editor.commit();

                                            user =response.getJSONObject("user");
                                            SharedPreferences.Editor editor2 = userpref.edit();
                                            editor2.putString("user", user.toString());
                                            editor2.commit();

                                            Toast.makeText(MainActivity.this, "Welcome "+ username, Toast.LENGTH_LONG).show();
                                            Intent LoggedInIntent = new Intent(MainActivity.this,LoggedIn.class);
                                            startActivity(LoggedInIntent);
                                            finish();
                                        }
                                        else if(successStatus == "false"){
                                            invalidUserMessage = response.getString("message");
                                            Toast.makeText(MainActivity.this, invalidUserMessage, Toast.LENGTH_LONG).show();
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
                else{
                    Toast.makeText(this, "Enter Username and Password!", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.tvSignUpLink:
                Intent SignUpIntent = new Intent(this,SignUp.class);
                this.finish();
                startActivity(SignUpIntent);
                break;
        }
    }

    //to kill the App
    /*@Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/
}
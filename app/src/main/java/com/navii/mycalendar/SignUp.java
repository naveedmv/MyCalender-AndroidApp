package com.navii.mycalendar;

/**
 * Created by Navii on 11/20/2015.
 */
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity implements View.OnClickListener{
    EditText etUsername, etPassword;
    Button bSignUp;
    TextView SignUpCancellink;

    private static final String TAG = "MyCalendar50";
    String successStatus= "";
    String signupErrorMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bSignUp = (Button) findViewById(R.id.bSignUp);
        SignUpCancellink = (TextView) findViewById(R.id.tvSignUpCancel);

        bSignUp.setOnClickListener(this);
        SignUpCancellink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSignUp:
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(this);

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                if (username != null && !username.equals("") && password != null && !password.equals("")) {
                    String url = "http://130.233.42.143:8080/signup";

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("password", password);

                    // Request a string response from the provided URL.
                    JsonObjectRequest JSONRequest = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try{
                                        Log.i(TAG, response.toString());
                                        successStatus = response.getString("success");
                                        signupErrorMessage = response.getString("message");

                                        if (successStatus == "true"){
                                            Toast.makeText(SignUp.this, "Welcome " + username, Toast.LENGTH_LONG).show();
                                            Intent LoggedInIntent = new Intent(SignUp.this,LoggedIn.class);
                                            startActivity(LoggedInIntent);
                                        }
                                        else if(successStatus == "false"){
                                            Toast.makeText(SignUp.this, signupErrorMessage, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(this, "Enter a Username and Password!", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.tvSignUpCancel:
                Intent SignUpCancelIntent = new Intent(this,MainActivity.class);
                startActivity(SignUpCancelIntent);
                this.finish();
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}

package com.example.covid_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    TextView canada,ontario;
    private RequestQueue mQ,m_ontario;

    private static String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canada = findViewById(R.id.Canada);
        ontario = findViewById(R.id.Ontario);

        mQ = Volley.newRequestQueue(this);
        m_ontario = Volley.newRequestQueue(this);
        jsonparser();
        jsonparser_ontario();





    }

    private void jsonparser_ontario(){
        String url = "https://api.covid19tracker.ca/provinces";



        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try {
                    for(int i =0; i< response.length(); i++){
                        JSONObject data = response.getJSONObject(i);

                        ontario.append(" Provice : " + data.getString("name") + " ");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        });


        m_ontario.add(request);

    }

    private void jsonparser(){
        String url = "https://api.covid19tracker.ca/summary";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for(int i =0; i< jsonArray.length(); i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                String total_cases = data.getString("total_cases");
                                String total_hosp = data.getString("total_hospitalizations");
                                String total_critical = data.getString("total_criticals");
                                String  total_recovered = data.getString("total_recoveries");

                                canada.append("Total cases " + total_cases +"\n");
                                canada.append(" Total Hospitilized " + total_hosp +"\n");
                                canada.append("Critical Condition " + total_critical +"\n");
                                canada.append(" Recovered " + total_recovered +"\n");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

            mQ.add(request);

    }
}

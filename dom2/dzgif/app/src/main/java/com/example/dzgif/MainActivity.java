package com.example.dzgif;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    RecyclerView rView;
    ArrayList<DataModel> dataModelArrayList = new ArrayList<>();

    public static final String API_KEY = "GKKSdcAtrgVwo79F6a7fV5eUtJpEM34Y";
    public static final String BASE_URL = "https://api.giphy.com/v1/gifs/trending?api_keys=";

    String url = BASE_URL+API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rView = findViewById(R.id.recyclerView);
        rView.setLayoutManager(new GridLayoutManager(this,2));
        rView.setHasFixedSize(true);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray dataArray = response.getJSONArray("data");

                    for(int i=0;i<dataArray.length();i++) {
                        JSONObject obj = dataArray.getJSONObject(i);

                        JSONObject obj1 = obj.getJSONObject("images");
                        JSONObject obj2 = obj1.getJSONObject("downsised_medium");
                        String sourceUrl = obj2.getString("url");

                        dataModelArrayList.add(new DataModel(sourceUrl));

                    }
                    DataAdapter  dataAdapter = new DataAdapter(MainActivity.this, dataModelArrayList);
                    rView.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();



                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(objectRequest );

    }
}

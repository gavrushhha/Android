package com.example.dzgif

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var rView: RecyclerView
    private val dataModelArrayList = ArrayList<DataModel>()
    private lateinit var dataAdapter: DataAdapter

    companion object {
        const val API_KEY = "GKKSdcAtrgVwo79F6a7fV5eUtJpEM34Y"
        const val BASE_URL = "https://api.giphy.com/v1/gifs/trending?api_key="
    }

    private val url = "$BASE_URL$API_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rView = findViewById(R.id.recyclerView)
        rView.layoutManager = GridLayoutManager(this, 2)
        rView.setHasFixedSize(true)

        val objectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                try {
                    val dataArray = response.getJSONArray("data")

                    for (i in 0 until dataArray.length()) {
                        val obj = dataArray.getJSONObject(i)
                        val obj1 = obj.getJSONObject("images")
                        val obj2 = obj1.getJSONObject("original")
                        val sourceUrl = obj2.getString("url")

                        dataModelArrayList.add(DataModel(sourceUrl))
                    }

                    dataAdapter = DataAdapter(this, dataModelArrayList)
                    rView.adapter = dataAdapter
                    dataAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        MySingleton.getInstance(this).addToRequestQueue(objectRequest)
    }
}

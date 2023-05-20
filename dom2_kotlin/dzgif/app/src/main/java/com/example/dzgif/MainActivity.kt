package com.example.dzgif

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
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
    private lateinit var loadMoreButton: Button  // Add this line
    private val dataModelArrayList = ArrayList<DataModel>()
    private lateinit var dataAdapter: DataAdapter

    companion object {
        const val API_KEY = "GKKSdcAtrgVwo79F6a7fV5eUtJpEM34Y"
        const val BASE_URL = "https://api.giphy.com/v1/gifs/trending?api_key="
        const val LIMIT = 20  // Number of items to load per page
    }

    private var offset = 0  // Starting offset

    private val url: String
        get() = "$BASE_URL$API_KEY&limit=$LIMIT&offset=$offset"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rView = findViewById(R.id.recyclerView)
        loadMoreButton = findViewById(R.id.loadMoreButton)  // Add this line

        rView.layoutManager = GridLayoutManager(this, 2)
        rView.setHasFixedSize(true)

        loadMoreButton.setOnClickListener {
            loadMoreGifs()
        }

        fetchGifs()
    }

    private fun fetchGifs() {
        offset = 0  // Reset offset for initial fetch

        val objectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                try {
                    val dataArray = response.getJSONArray("data")

                    dataModelArrayList.clear()  // Clear existing data before adding new items

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

                    // Check if there are more items available
                    val totalItemCount = response.getInt("total_count")
                    if (totalItemCount > offset + LIMIT) {
                        loadMoreButton.visibility = View.VISIBLE
                    } else {
                        loadMoreButton.visibility = View.GONE
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        MySingleton.getInstance(this).addToRequestQueue(objectRequest)
    }

    private fun loadMoreGifs() {
        offset += LIMIT  // Increment offset for next page

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

                    dataAdapter.notifyDataSetChanged()

                    // Check if there are more items available
                    val totalItemCount = response.getInt("total_count")
                    if (totalItemCount > offset + LIMIT) {
                        loadMoreButton.visibility = View.VISIBLE
                    } else {
                        loadMoreButton.visibility = View.GONE
                    }

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


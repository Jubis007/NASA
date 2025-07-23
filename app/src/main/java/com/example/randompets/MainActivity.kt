package com.example.randompets

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.randompets.databinding.ActivityMainBinding
import okhttp3.Headers
import java.security.MessageDigest

// This helper function stays outside the class
fun String.toMd5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

class MainActivity : AppCompatActivity() {
    // Properties are now correctly inside the class
    private lateinit var binding: ActivityMainBinding
    private lateinit var items: MutableList<NasaItem>
    private lateinit var rvCharacters: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize variables using the binding object
        items = mutableListOf()
        rvCharacters = binding.petList // Use binding, NOT findViewById. Assumes your RecyclerView ID is pet_list

        fetchNasaData()
    }

    private fun fetchNasaData() {
        val apiKey = "kSUNo6I06hgraGFSaDx0BCbQw0glzjLiywnrUfS1" // Your NASA key
        val client = AsyncHttpClient()
        // We ask for 20 random pictures from the APOD API
        val url = "https://api.nasa.gov/planetary/apod?api_key=$apiKey&count=20"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("NASA", "Response successful!")
                items.clear() // Clear the list for new data

                val resultsArray = json.jsonArray

                for (i in 0 until resultsArray.length()) {
                    val itemObject = resultsArray.getJSONObject(i)

                    // The APOD API sometimes returns videos, so we check the media type
                    if (itemObject.getString("media_type") == "image") {
                        items.add(
                            NasaItem(
                                title = itemObject.getString("title"),
                                explanation = itemObject.getString("explanation"),
                                url = itemObject.getString("url")
                            )
                        )
                    }
                }

                val adapter = NasaItemAdapter(items)
                rvCharacters.adapter = adapter
                rvCharacters.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.d("NASA Error", errorResponse)
            }
        })
    }
}
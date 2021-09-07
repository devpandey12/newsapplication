package com.example.newsfresh

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter= NewsListAdapter( this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData(){
        val url =
            "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                val newsJSONArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJSONArray.length()){
                    val newsJasonObject=newsJSONArray.getJSONObject(i)
                    val news = News(
                        newsJasonObject.getString("title"),
                        newsJasonObject.getString("author"),
                        newsJasonObject.getString("url"),
                        newsJasonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener{
            })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }





    override fun onItemClicked(item: News) {

        val builder= CustomTabsIntent.Builder();
        val customTabsIntent= builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));

    }
}

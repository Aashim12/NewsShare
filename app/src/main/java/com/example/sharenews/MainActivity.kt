package com.example.sharenews

import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.request.Request
import com.example.sharenews.MySingleton.MySingleton.Companion.getInstance
import com.example.sharenews.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NewsItemclicked {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
private lateinit var mAdapter: Newsadapter // to make a member function we add 'm' front of it
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         recyclerview.layoutManager = LinearLayoutManager(this)
        fetchdata()
     mAdapter = Newsadapter(this)
        recyclerview.adapter = mAdapter
    }

private fun fetchdata(){
val url = "https://saurav.tech/NewsAPI/top-headlines/category/business/in.json"
  val jsonObjectRequest=JsonObjectRequest(com.android.volley.Request.Method.GET
,url,null, Response.Listener {
                             val newsJSONArray=it.getJSONArray("articles")
                               val newsarray = ArrayList<News>()  // News type from data class
                                for (i in 0 until newsJSONArray.length()){
                                    val newsJSONObject=newsJSONArray.getJSONObject(i) // to get all data requried for each article
                                 val news = News(
                                     newsJSONObject.getString("title"),
                                       newsJSONObject.getString("author"),
                                       newsJSONObject.getString("url"),
                                     newsJSONObject.getString("urlToImage") // all data added in data class
                                 )
                                    newsarray.add(news)
                                }
          mAdapter.updateNews(newsarray)  // when we got newsarray we called update news fucntion to pass newsarray into adapter

      },
      Response.ErrorListener {

      }


  )

// Access the RequestQueue through your singleton class.
    MySingleton.MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


}



    override fun onItemclicked(item: News) {

        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))

    }


}

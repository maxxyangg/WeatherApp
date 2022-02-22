package com.example.weatherapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ForecastActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var api: Api



    lateinit var adapterData : List<DayForecast>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        println("creating forecast")

        adapterData = mutableListOf()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = MyAdapter(adapterData)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            )
        ) // https://stackoverflow.com/questions/31242812/how-can-a-divider-line-be-added-in-an-android-recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pro.openweathermap.org/data/2.5/forecast/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)

    }

    override fun onResume(){
        super.onResume()
        println("grabbing data")
        val call: Call<Forecast> = api.getForcast("55038")
        call.enqueue(object : Callback<Forecast>{
            override fun onResponse(
                call: Call<Forecast>,
                response: Response<Forecast>
            ) {
                println("successfully got it")
                val forecastConditions = response.body()
                forecastConditions?.let {
                    sendAdapter(it)
                }

            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                println("Fail: did not get conditions")
                println(t.message)

            }
        })
    }

    fun sendAdapter(conditions: Forecast){
        adapterData = conditions.list as MutableList<DayForecast>
        recyclerView.adapter = MyAdapter(adapterData)
    }


}
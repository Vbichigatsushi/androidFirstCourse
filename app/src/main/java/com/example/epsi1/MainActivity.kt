package com.example.epsi1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.ekn.gruzer.gaugelibrary.FullGauge
import com.ekn.gruzer.gaugelibrary.Range
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    private lateinit var temperatureTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var airQualityTextView : TextView
    private lateinit var airQualitygauge: CustomArcGauge
    private val handler = Handler(Looper.getMainLooper())
    private val updateIntervalTemperature = 9000L
    private val updateIntervalHumidity = 7000L
    private val updateIntervalAirQuality = 6000L

    private val updateTemperatureRunnable = object : Runnable {
        override fun run() {
            temperatureTextView.apply {
                text = GetDatas().getTemperature().toString()
                textSize = 20F
            }
            handler.postDelayed(this, updateIntervalTemperature)
        }
    }

    private val updateHumidityRunnable = object : Runnable {
        override fun run() {
            humidityTextView.apply {
                text = GetDatas().getHumidity().toString()
                textSize = 20F
            }
            handler.postDelayed(this, updateIntervalHumidity)
        }
    }

    private val updateAirQualityRunnable = object : Runnable {
        override fun run() {
            val range = Range()
            range.color = Color.parseColor("#00b20b")
            range.from = 0.0
            range.to = 15.0

            val range2 = Range()
            range2.color = Color.parseColor("#ffde21")
            range2.from = 15.0
            range2.to = 24.5

            val range3 = Range()
            range3.color = Color.parseColor("#ce0000")
            range3.from = 24.5
            range3.to = 30.0

            //add color ranges to gauge
            airQualitygauge.addRange(range)
            airQualitygauge.addRange(range2)
            airQualitygauge.addRange(range3)
            airQualitygauge.minValue = 0.0
            airQualitygauge.maxValue = 30.0

            val values = GetDatas().getAirQuality().split(";")
            val totalIndex = values[0].toDouble()
            airQualitygauge.value = totalIndex
            airQualityTextView.apply {
                text = values[1]
                textSize = 12.5F
            }
            handler.postDelayed(this, updateIntervalAirQuality)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.title = "Dom'Hub"

        val temperatureCard = findViewById<MaterialCardView>(R.id.cardTemperature)
        temperatureTextView = temperatureCard.getChildAt(0) as TextView
        // Lancer la mise à jour périodique
        handler.post(updateTemperatureRunnable)

        val humidityCard = findViewById<MaterialCardView>(R.id.cardHumidity)
        humidityTextView = humidityCard.getChildAt(0) as TextView
        // Lancer la mise à jour périodique
        handler.post(updateHumidityRunnable)

        airQualitygauge = findViewById<CustomArcGauge>(R.id.arcGauge)
        val airQualityCard = findViewById<MaterialCardView>(R.id.cardAirQuality)
        airQualityTextView = airQualityCard.getChildAt(1) as TextView
        // Lancer la mise à jour périodique
        handler.post(updateAirQualityRunnable)

//        val i = Intent(this, secondPage::class.java)
//
//        val tv_hello_world = findViewById<TextView>(R.id.tv_hello_world)
//        tv_hello_world.setOnClickListener {
//            tv_hello_world.apply {
//                text = "Nouveau texte"
//                setBackgroundColor(Color.RED)
//            }
//            startActivity(i)
//        }
//        Toast.makeText(this, "Erreur", Toast.LENGTH_LONG).show()

    }
}
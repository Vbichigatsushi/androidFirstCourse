package com.example.epsi1.activity

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.epsi1.Adapter
import com.example.epsi1.CustomArcGauge
import com.example.epsi1.GetDatas
import com.example.epsi1.R
import com.example.epsi1.UserDataApplication
import com.example.epsi1.database.AppDatabase
import com.example.epsi1.model.InventoryItemDao
import com.example.epsi1.model.T_InventoryItem
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var temperatureTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var airQualityTextView : TextView
    private lateinit var airQualitygauge: CustomArcGauge
    private lateinit var pingTextView: TextView
    private lateinit var downloadTextView: TextView
    private lateinit var uploadTextView : TextView
    private val handler = Handler(Looper.getMainLooper())
    private val updateIntervalTemperature = 9000L
    private val updateIntervalHumidity = 7000L
    private val updateIntervalAirQuality = 6000L
    private val updateIntervalConnectionState = 15000L

    private lateinit var itemDao: InventoryItemDao
    private val updateTemperatureRunnable = object : Runnable {
        override fun run() {
            temperatureTextView.apply {
                text = GetDatas(this@MainActivity).getTemperature().toString()
                textSize = 20F
            }
            handler.postDelayed(this, updateIntervalTemperature)
        }
    }

    private val updateHumidityRunnable = object : Runnable {
        override fun run() {
            humidityTextView.apply {
                text = GetDatas(this@MainActivity).getHumidity().toString()
                textSize = 20F
            }
            handler.postDelayed(this, updateIntervalHumidity)
        }
    }

    private val updateAirQualityRunnable = object : Runnable {
        override fun run() {
            GetDatas(this@MainActivity).getAirQuality(airQualitygauge, airQualityTextView)
            handler.postDelayed(this, updateIntervalAirQuality)
        }
    }

    private val updateConnectionStateRunnable = object : Runnable {
        override fun run() {
            GetDatas(this@MainActivity).performSpeedTest(pingTextView, downloadTextView, uploadTextView)
            handler.postDelayed(this, updateIntervalConnectionState)
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
        supportActionBar?.title = "🏠 Dom'Hub"

        itemDao = (applicationContext as UserDataApplication).database.InventoryItemDao()
        val temperatureCard = findViewById<MaterialCardView>(R.id.cardTemperature)
        temperatureTextView = temperatureCard.getChildAt(0) as TextView
        // Lancer la mise à jour périodique de la température
        handler.post(updateTemperatureRunnable)

        val humidityCard = findViewById<MaterialCardView>(R.id.cardHumidity)
        humidityTextView = humidityCard.getChildAt(0) as TextView
        // Lancer la mise à jour périodique de l'humidité
        handler.post(updateHumidityRunnable)

        airQualitygauge = findViewById<CustomArcGauge>(R.id.arcGauge)
        val airQualityCard = findViewById<MaterialCardView>(R.id.cardAirQuality)
        airQualityTextView = airQualityCard.getChildAt(1) as TextView
        // Lancer la mise à jour périodique de la qualité de l'air
        handler.post(updateAirQualityRunnable)

        val blindCard = findViewById<MaterialCardView>(R.id.cardBlind).getChildAt(0) as LinearLayout

        for (i in 0..2){
            val blindTextView = blindCard.getChildAt(i) as TextView

            val colorFrom = ContextCompat.getColor(this, R.color.second_violet)
            val colorTo = ContextCompat.getColor(this, R.color.purple_500)


            val colorAnimation = ValueAnimator.ofArgb(colorFrom, colorTo, colorFrom)
            colorAnimation.duration = 1000 // Durée en millisecondes
            colorAnimation.repeatCount = 5

            colorAnimation.addUpdateListener { animator ->
                blindTextView.setBackgroundColor(animator.animatedValue as Int)
            }

            blindTextView.setOnClickListener {
                Toast.makeText(this, "action volet effectué", Toast.LENGTH_LONG).show()
                colorAnimation.start()
            }
        }

        val connectionStateCard = findViewById<MaterialCardView>(R.id.cardConnectionState).getChildAt(0) as LinearLayout
        pingTextView = connectionStateCard.getChildAt(0) as TextView
        downloadTextView = connectionStateCard.getChildAt(1) as TextView
        uploadTextView = connectionStateCard.getChildAt(2) as TextView
        // Lancer la mise à jour périodique du speedtest
        handler.post(updateConnectionStateRunnable)

        //changement de taille de l'icone de serveur
        val serverStateCard = findViewById<MaterialCardView>(R.id.cardServerState).getChildAt(0) as TextView
        val drawable = ContextCompat.getDrawable(this, R.drawable.server_icon)
        drawable?.setBounds(40, 0, serverStateCard.lineHeight+40, serverStateCard.lineHeight) // Ajuste la taille au texte
        serverStateCard.setCompoundDrawables(drawable, null, null, null) // Applique l'icône à gauche

        val lampsCard = findViewById<MaterialCardView>(R.id.cardLamps).getChildAt(0) as LinearLayout

        for (i in 0..2){
            val lampTextView = lampsCard.getChildAt(i) as TextView

            val colorFrom = ContextCompat.getColor(this, R.color.second_violet)
            val colorTo = ContextCompat.getColor(this, R.color.purple_500)


            val colorAnimation = ValueAnimator.ofArgb(colorFrom, colorTo, colorFrom)
            colorAnimation.duration = 250 // Durée en millisecondes
            colorAnimation.repeatMode = ValueAnimator.REVERSE

            colorAnimation.addUpdateListener { animator ->
                lampTextView.setBackgroundColor(animator.animatedValue as Int)
            }

            lampTextView.setOnClickListener {
                Toast.makeText(this, "action lampe effectué", Toast.LENGTH_LONG).show()
                colorAnimation.start()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //mise a jour des données de l'inventaire
        val inventoryCard = findViewById<MaterialCardView>(R.id.cardInventory).getChildAt(0) as LinearLayout
        val listView = inventoryCard.getChildAt(1) as ListView

        CoroutineScope(Dispatchers.IO).launch {
            val dbvalues = itemDao.selectLines()
            withContext(Dispatchers.Main){
                val listAdapter = Adapter(this@MainActivity, dbvalues)
                listView.adapter = listAdapter
            }

        }

        val i = Intent(this, InventoryDetails::class.java)
        inventoryCard.setOnClickListener {
            startActivity(i)
        }
    }
}
package com.example.epsi1

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.widget.TextView
import com.ekn.gruzer.gaugelibrary.Range
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress
import kotlin.math.roundToInt
import kotlin.random.Random


class GetDatas(private val ctx: Context) {

    fun getTemperature(): String {
        val min = 17.5
        val max = 25.9
        val temperature = min + (Math.random() * (max - min))
        return String.format("%.1f °C", temperature)
    }

    fun getHumidity(): String {
        val min = 40
        val max = 60
        val humidity = min + (Math.random() * (max - min))
        return String.format("💧%.1f%%", humidity)
    }

    fun getAirQuality(airQualitygauge: CustomArcGauge, airQualityTextView: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            val hcho = Random.nextDouble(0.1, 0.5) // Faux HCHO (mg/m³)
            val tvoc = Random.nextDouble(0.5, 3.0) // Faux TVOC (mg/m³)
            val pm25 = Random.nextDouble(0.005, 0.05) // Faux PM2.5 (µg/m³)
            val pm10 = Random.nextDouble(0.01, 0.1) // Faux PM10 (µg/m³)

            // Calcul d'un index global (formule arbitraire)
            val totalIndex = ((hcho * 12) + (tvoc * 4) + (pm25 * 1000) + (pm10 * 300)) / 4 //max 24.5

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

            withContext(Dispatchers.Main) {
                //add color ranges to gauge
                airQualitygauge.addRange(range)
                airQualitygauge.addRange(range2)
                airQualitygauge.addRange(range3)
                airQualitygauge.minValue = 0.0
                airQualitygauge.maxValue = 30.0


                airQualitygauge.value = (totalIndex * 100.0).roundToInt() / 100.0
                airQualityTextView.apply {
                    text = String.format("hcho : %.2f   tvoc : %.2f\npm25 : %.2f   pm10 : %.2f", hcho, tvoc, pm25, pm10)
                    textSize = 11F
                }
            }
        }
    }

     fun performSpeedTest(pingTextView: TextView, downloadTextView: TextView, uploadTextView: TextView) {
         CoroutineScope(Dispatchers.IO).launch {
             val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

             // Network Capabilities of Active Network
             val nc = cm.getNetworkCapabilities(cm.activeNetwork) ?: return@launch

             // DownSpeed in MBPS
             val downSpeed = (nc.linkDownstreamBandwidthKbps)/1024

             // UpSpeed  in MBPS
             val upSpeed = (nc.linkUpstreamBandwidthKbps)/1024

             val host = "www.google.com"
             val timeout = 10000
             val beforeTime = System.currentTimeMillis()
             InetAddress.getByName(host).isReachable(timeout)
             val afterTime = System.currentTimeMillis()
             val latency = afterTime - beforeTime

             withContext(Dispatchers.Main){
                 pingTextView.apply {
                     text = "⏱️\n\n$latency ms"
                     textSize = 11F
                 }
                 downloadTextView.apply {
                     text = "⬇️\n\n$downSpeed Mb/s"
                     textSize = 11F
                 }
                 uploadTextView.apply {
                     text = "⬆️\n\n$upSpeed Mb/s"
                     textSize = 11F
                 }
             }
         }
    }
}


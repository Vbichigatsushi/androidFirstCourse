package com.example.epsi1

import kotlin.math.round
import kotlin.random.Random

class GetDatas {
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

    fun getAirQuality(): String {
        val hcho = Random.nextDouble(0.1, 0.5) // Faux HCHO (mg/m³)
        val tvoc = Random.nextDouble(0.5, 3.0) // Faux TVOC (mg/m³)
        val pm25 = Random.nextDouble(0.005, 0.05) // Faux PM2.5 (µg/m³)
        val pm10 = Random.nextDouble(0.01, 0.1) // Faux PM10 (µg/m³)

        // Calcul d'un index global (formule arbitraire)
        val totalIndex = ((hcho * 12) + (tvoc * 4) + (pm25 * 1000) + (pm10 * 300)) / 4 //max 24.5

        return String.format("%.1f;hcho : %.2f   tvoc : %.2f\npm25 : %.2f   pm10 : %.2f", totalIndex, hcho, tvoc, pm25, pm10)
    }
}


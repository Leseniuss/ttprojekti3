package com.example.ttprojekti3

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

    private var lastTimeStamp = 0L
    private var deltaTime = 0L


    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    override fun analyze(image: ImageProxy) {
        val currentTimeStamp = System.currentTimeMillis()
        val intervalInMilliSeconds = TimeUnit.MILLISECONDS.toMillis(5000)
        deltaTime = currentTimeStamp - lastTimeStamp

        if (deltaTime >= intervalInMilliSeconds) {
            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()
           // Log.d("PERKELE", "Average luminosityyyyyy: $luma")

            lastTimeStamp = currentTimeStamp


            listener(luma)
        }

        image.close()
    }


}
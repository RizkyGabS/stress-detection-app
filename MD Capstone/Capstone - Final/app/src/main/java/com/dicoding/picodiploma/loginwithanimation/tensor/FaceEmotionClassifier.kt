package com.dicoding.picodiploma.loginwithanimation.tensor

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class FaceEmotionClassifier(context: Context) {

    private val interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context))
    }


    private fun loadModelFile(context: Context): ByteBuffer {
        val inputStream: InputStream = context.assets.open("model.tflite")
        val fileBytes = ByteArray(inputStream.available())
        inputStream.read(fileBytes)
        inputStream.close()

        val buffer = ByteBuffer.allocateDirect(fileBytes.size)
        buffer.order(ByteOrder.nativeOrder())
        buffer.put(fileBytes)
        buffer.rewind()

        return buffer
    }

    fun classifyEmotion(bitmap: Bitmap): String {
        val inputImageBuffer = convertBitmapToByteBuffer(bitmap)
        val outputScores = Array(1) { FloatArray(NUM_CLASSES) }
        interpreter.run(inputImageBuffer, outputScores)
        return processOutputScores(outputScores)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val inputShape = interpreter.getInputTensor(0).shape()
        val inputSize = inputShape[1] * inputShape[2] * inputShape[3]
        val inputImageBuffer = ByteBuffer.allocateDirect(inputSize * 4)
        inputImageBuffer.order(ByteOrder.nativeOrder())
        inputImageBuffer.rewind()

        val pixels = IntArray(inputShape[1] * inputShape[2])
        bitmap.getPixels(pixels, 0, inputShape[1], 0, 0, inputShape[1], inputShape[2])

        for (pixelValue in pixels) {
            val normalizedPixelValue = (pixelValue and 0xFF) / 255.0f
            inputImageBuffer.putFloat(normalizedPixelValue)
        }

        return inputImageBuffer
    }

    private fun processOutputScores(outputScores: Array<FloatArray>): String {
        // OutputScores biasanya berupa probabilitas untuk setiap kelas
        // Pilih indeks dengan probabilitas tertinggi sebagai kelas yang diprediksi

        val maxIndex = outputScores[0].indices.maxByOrNull { outputScores[0][it] } ?: -1

        // Gantilah dengan label yang sesuai dengan indeks kelas pada model Anda
        val emotionLabel = when (maxIndex) {
            0 -> "Angry"
            1 -> "Disgusted"
            2 -> "Fearful"
            3 -> "Happy"
            4 -> "Neutral"
            5 -> "Sad"
            6 -> "Suprised"
            else -> "Unknown"
        }

        return emotionLabel
    }

    companion object {
        private const val NUM_CLASSES = 7 // Sesuaikan dengan jumlah kelas pada model Anda
    }
}
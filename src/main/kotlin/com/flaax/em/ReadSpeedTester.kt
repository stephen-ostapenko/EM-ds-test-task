package com.flaax.em

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun testReadSpeed(files: List<File>, fileSize: Int, dataToRead: ByteArray): Double {
    var time = 0L
    files.shuffled(Random).forEach { file ->
        val inputStream = FileInputStream(file)
        val bufInputStream = BufferedInputStream(inputStream)

        val currentTime = measureTimeMillis {
            val data = bufInputStream.readNBytes(fileSize)
            if (!data.contentEquals(dataToRead)) {
                throw RuntimeException("Read data doesn't equal to written data")
            }
        }
        time += currentTime

        bufInputStream.close()
        inputStream.close()
    }
    return fileSize.toLong() * files.size.toLong() * 1000L / time.toDouble()
}
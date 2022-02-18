package com.flaax.em

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random
import kotlin.system.measureTimeMillis

// function writes randomly generated data into all temporary files from the list
// and measures time taken for writing
fun testWriteSpeed(files: List<File>, dataToWrite: ByteArray): Double {
    var time = 0L
    files.shuffled(Random).forEach { file ->
        val outputStream = FileOutputStream(file)
        val bufOutputStream = BufferedOutputStream(outputStream)

        val currentTime = measureTimeMillis {
            bufOutputStream.write(dataToWrite)
            bufOutputStream.flush()
            outputStream.channel.force(true)
        }
        time += currentTime

        bufOutputStream.close()
        outputStream.close()
    }
    return dataToWrite.size.toLong() * files.size.toLong() * 1000L / time.toDouble()
}
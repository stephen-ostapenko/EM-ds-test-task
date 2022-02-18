package com.flaax.em

import java.io.*
import java.lang.IllegalArgumentException
import java.lang.Integer.min
import java.lang.NumberFormatException
import java.lang.RuntimeException
import kotlin.io.path.Path
import kotlin.io.path.createTempFile
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.system.measureTimeMillis

val NUMBERS_OF_FILES = arrayOf(1, 2, 3, 4, 5, 10, 20, 50, 100)

fun testWriteSpeed(files: List<File>, fileSize: Int, dataToWrite: ByteArray): Double {
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
    return fileSize.toLong() * files.size.toLong() * 1000L / time.toDouble()
}

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

fun singleTestReadWriteSpeed(directory: String, fileSize: Int, numberOfFiles: Int): Pair<Double, Double> {
    val files = (1..numberOfFiles).map { createTempFile(Path(directory), it.toString()).toFile() }
    val testData = Random.nextBytes(fileSize)

    val writeSpeed = testWriteSpeed(files, fileSize, testData)
    val readSpeed = testReadSpeed(files, fileSize, testData)

    files.forEach { file ->
        file.delete()
    }

    return Pair(readSpeed, writeSpeed)
}

fun testReadWriteSpeed(directory: String, testDataSizeInBytes: Int, numberOfTries: Int) {
    for (numberOfFiles in NUMBERS_OF_FILES) {
        val fileSize = testDataSizeInBytes / numberOfFiles
        if (fileSize == 0) {
            continue
        }

        val (readSpeedResults, writeSpeedResults) = (1..numberOfTries).map { singleTestReadWriteSpeed(directory, fileSize, numberOfFiles) }.unzip()
        val readSpeed = readSpeedResults.average()
        val writeSpeed = writeSpeedResults.average()

        println("Test data with total size of ${(testDataSizeInBytes.toDouble() / 1024 / 1024).roundToInt()} MB was split to $numberOfFiles file(s)")
        println("Read speed is ${(readSpeed / 1024 / 1024).roundToInt()} MB/s and write speed is ${(writeSpeed / 1024 / 1024).roundToInt()} MB/s")
        println()
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please specify the path to folder with R/W access")
        return
    }

    try {
        val testDataSizeInBytes = if (args.size >= 2) {
            args[1].toInt()
        } else {
            128
        } * 1024 * 1024
        if (testDataSizeInBytes < 1024) {
            throw IllegalArgumentException("Test data size must be at least 1MB")
        }

        val numberOfTries = if (args.size >= 3) {
            args[2].toInt()
        } else {
            5
        }
        if (numberOfTries <= 0) {
            throw IllegalArgumentException("Number of tries must be positive")
        }

        testReadWriteSpeed(args[0], testDataSizeInBytes, numberOfTries)

    } catch (e: NumberFormatException) {
        println("Test data size and number of tries must be positive integer")
    } catch (e: IllegalArgumentException) {
        println("Illegal argument!")
        println(e.message)
    }
}
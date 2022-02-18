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

const val MAX_FILES_NUMBER = 1000
const val MAX_FILE_SIZE_IN_BYTES = 1024 * 1024 * 1024
val SIZES_OF_SMALL_FILES = arrayOf(3, 4).map { it * 1024 }

fun testWriteSpeed(files: List<File>, fileSize: Int, dataToWrite: ByteArray): Double {
    val time = measureTimeMillis {
        files.forEach { file ->
            val outputStream = FileOutputStream(file)
            val bufOutputStream = BufferedOutputStream(outputStream)

            bufOutputStream.write(dataToWrite)
            bufOutputStream.flush()
            outputStream.channel.force(true)

            bufOutputStream.close()
            outputStream.close()
        }
    }
    return fileSize.toLong() * files.size.toLong() * 1000L / time.toDouble()
}

fun testReadSpeed(files: List<File>, fileSize: Int, dataToRead: ByteArray): Double {
    val time = measureTimeMillis {
        files.forEach { file ->
            val inputStream = FileInputStream(file)
            val bufInputStream = BufferedInputStream(inputStream)

            val data = bufInputStream.readNBytes(fileSize)
            if (!data.contentEquals(dataToRead)) {
                throw RuntimeException("Read data doesn't equal to written data")
            }
        }
    }
    return fileSize.toLong() * files.size.toLong() * 1000L / time.toDouble()
}

fun singleTestReadWriteSpeedOnSmallFiles(directory: String, fileSize: Int, numberOfFiles: Int): Pair<Double, Double> {
    val files = (1..numberOfFiles).map { createTempFile(Path(directory), it.toString()).toFile() }
    val testData = Random.nextBytes(fileSize)

    val writeSpeed = testWriteSpeed(files, fileSize, testData)
    val readSpeed = testReadSpeed(files, fileSize, testData)

    files.forEach { file ->
        file.delete()
    }

    return Pair(readSpeed, writeSpeed)
}

fun testReadWriteSpeedOnSmallFiles(directory: String, testDataSizeInBytes: Int, numberOfTries: Int) {
    for (fileSize in SIZES_OF_SMALL_FILES) {
        val numberOfFiles = min(testDataSizeInBytes / fileSize, MAX_FILES_NUMBER)
        if (numberOfFiles == 0) {
            continue
        }
        repeat(numberOfTries) {
            val result = singleTestReadWriteSpeedOnSmallFiles(directory, fileSize, numberOfFiles)
            println("${result.first.roundToInt()} ${result.second.roundToInt()}")
        }
    }
}

fun testRWSpeed(directory: String, testDataSizeInBytes: Int) {

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
            512
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

        testReadWriteSpeedOnSmallFiles(args[0], testDataSizeInBytes, 1)

    } catch (e: NumberFormatException) {
        println("Test data size and number of tries must be positive integer")
    } catch (e: IllegalArgumentException) {
        println("Illegal argument!")
        println(e.message)
    }
}
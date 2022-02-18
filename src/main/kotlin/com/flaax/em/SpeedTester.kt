package com.flaax.em

import kotlin.io.path.Path
import kotlin.random.Random

// function performs single Write/Read test
fun singleTestReadWriteSpeed(directory: String, fileSize: Int, numberOfFiles: Int): Pair<Double, Double> {
    // creating temporary files
    val files = (1..numberOfFiles)
        .map { kotlin.io.path.createTempFile(Path(directory), it.toString()).toFile() }
    // generating random data to write
    val testData = Random.nextBytes(fileSize)

    val writeSpeed = testWriteSpeed(files, testData)
    val readSpeed = testReadSpeed(files, testData)

    // deleting temporary files
    files.forEach { file ->
        file.delete()
    }

    return Pair(readSpeed, writeSpeed)
}

// numbers of files to perform different measures
// in the first measure all data is written to one file
// in the second measure all data is split into two files
// in the third measure all data is split into three files
// ...
// in the last measure all data is split into 100 files
val NUMBERS_OF_FILES = arrayOf(1, 2, 3, 4, 5, 10, 20, 50, 100)

// class to store one result of speed test
data class ResultOfSpeedTest(val numberOfFiles: Int, val readSpeed: Double, val writeSpeed: Double)

// function performs different tests and returns a list with all results
fun testReadWriteSpeed(directory: String, testDataSizeInBytes: Int, numberOfTries: Int): List<ResultOfSpeedTest> {
    val results = mutableListOf<ResultOfSpeedTest>()
    for (numberOfFiles in NUMBERS_OF_FILES) {
        val fileSize = testDataSizeInBytes / numberOfFiles
        if (fileSize == 0) {
            continue
        }

        val (readSpeedResults, writeSpeedResults) = (1..numberOfTries)
            .map { singleTestReadWriteSpeed(directory, fileSize, numberOfFiles) }
            .unzip()
        val readSpeed = readSpeedResults.average()
        val writeSpeed = writeSpeedResults.average()

        results.add(ResultOfSpeedTest(numberOfFiles, readSpeed, writeSpeed))
    }

    return results.toList()
}
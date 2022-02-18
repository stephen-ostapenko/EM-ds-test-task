package com.flaax.em

import kotlin.io.path.Path
import kotlin.random.Random

fun singleTestReadWriteSpeed(directory: String, fileSize: Int, numberOfFiles: Int): Pair<Double, Double> {
    val files = (1..numberOfFiles)
        .map { kotlin.io.path.createTempFile(Path(directory), it.toString()).toFile() }
    val testData = Random.nextBytes(fileSize)

    val writeSpeed = testWriteSpeed(files, fileSize, testData)
    val readSpeed = testReadSpeed(files, fileSize, testData)

    files.forEach { file ->
        file.delete()
    }

    return Pair(readSpeed, writeSpeed)
}

val NUMBERS_OF_FILES = arrayOf(1, 2, 3, 4, 5, 10, 20, 50, 100)

data class ResultOfSpeedTest(val numberOfFiles: Int, val readSpeed: Double, val writeSpeed: Double)

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
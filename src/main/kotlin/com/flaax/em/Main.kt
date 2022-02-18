package com.flaax.em

import java.lang.Exception
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.math.roundToInt

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please specify the path to folder with Read/Write access")
        return
    }

    try {
        val directory = args[0]
        if (!Files.isDirectory(Path(directory))) {
            throw IllegalArgumentException("Path '$directory' doesn't exist or is not a folder")
        }
        if (!Files.isReadable(Path(directory)) || !Files.isWritable(Path(directory))) {
            throw IllegalArgumentException("'$directory' has to be readable and writable")
        }

        val testDataSizeInBytes = if (args.size >= 2) {
            val sz = args[1].toInt()
            if (sz <= 0 || sz > 1024) {
                throw IllegalArgumentException("Test data size must be at least 1 MB and at most 1024 MB")
            }
            sz // value from args
        } else {
            128 // default value
        } * 1024 * 1024

        val numberOfTries = if (args.size >= 3) {
            val num = args[2].toInt()
            if (num <= 0) {
                throw IllegalArgumentException("Number of tries must be positive")
            }
            num // value from args
        } else {
            5 // default value
        }

        val results = testReadWriteSpeed(directory, testDataSizeInBytes, numberOfTries)
        results.forEach {
            println("Test data with total size of " +
                    "${(testDataSizeInBytes.toDouble() / 1024 / 1024).roundToInt()} MB " +
                    "was split into ${it.numberOfFiles} file(s)")
            println("Read speed is ${(it.readSpeed / 1024 / 1024).roundToInt()} MB/s " +
                    "and write speed is ${(it.writeSpeed / 1024 / 1024).roundToInt()} MB/s")
            println()
        }

    } catch (e: NumberFormatException) {
        println("Illegal argument!")
        println("Test data size and number of tries must be positive integer")
    } catch (e: IllegalArgumentException) {
        println("Illegal argument!")
        println(e.message)
    } catch (e: Exception) {
        println("Fatal error!!!")
        println(e)
    }
}
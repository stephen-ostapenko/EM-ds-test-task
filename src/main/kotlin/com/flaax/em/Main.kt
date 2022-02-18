package com.flaax.em

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
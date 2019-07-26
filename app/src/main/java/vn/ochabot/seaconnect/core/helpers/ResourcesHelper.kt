package vn.ochabot.seaconnect.core.helpers

import vn.ochabot.seaconnect.core.App

/**
 * @author linhtruong
 */
object ResourcesHelper {
    fun getAsset(fileName: String): String {
        val reader = (App.appContext.assets.open(fileName)
                ?: throw RuntimeException("Cannot open file: $fileName"))
                .bufferedReader()

        val lines = reader.useLines { sequence: Sequence<String> ->
            sequence.reduce { a, b -> "$a\n$b" }
        }

        return lines
    }
}
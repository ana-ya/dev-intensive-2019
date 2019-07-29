package ru.skillbranch.devintensive.utils

import java.io.*
import kotlin.collections.HashMap

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {

        val parts: List<String>? = fullName?.split(' ')

        val firstName = checkName(parts?.getOrNull(0))
        val lastName = checkName(parts?.getOrNull(1))

        return firstName to lastName
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val clearedFirstName = firstName?.trim()
        val clearedLastName = lastName?.trim()

        return when {
            !clearedFirstName.isNullOrEmpty() && !clearedLastName.isNullOrEmpty() ->
                "${clearedFirstName.first().toUpperCase()}${clearedLastName.first().toUpperCase()}"
            clearedFirstName.isNullOrEmpty() && !clearedLastName.isNullOrEmpty() ->
                "${clearedLastName.first().toUpperCase()}"
            !clearedFirstName.isNullOrEmpty() && clearedLastName.isNullOrEmpty() ->
                "${clearedFirstName.first().toUpperCase()}"
            else -> null
        }
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val transliterationList: HashMap<String, String> = getTransliterationList()
        val parts = payload.split(" ")
        val firstName = convertFromCyrillicToLatin(parts[0], transliterationList)
        val lastName = convertFromCyrillicToLatin(parts[1], transliterationList)

        return "$firstName$divider$lastName"
    }

    private fun convertFromCyrillicToLatin(word: String, transliterationList: HashMap<String, String>): String {
        var convertedWord = ""

        for (item in word) {
            if (!isLatinAlphabet(item)) {
                val isCapitalized = item.isUpperCase()
                val latin = transliterationList[item.toString().toLowerCase()]

                convertedWord += "${if (isCapitalized) latin?.capitalize() else latin}"
            } else {
                convertedWord += item
            }
        }

        return convertedWord
    }

    private fun isLatinAlphabet(c: Char): Boolean {
        return when (c) {
            in 'a'..'z', in 'A'..'Z' -> true
            else -> false
        }
    }

    private fun getTransliterationList(): HashMap<String, String> {
        val file = File("src/html/transliteration.html")
        val lines: List<String> = file.readLines()
        val map = HashMap<String, String>()
        for (line in lines) {
            val cleanedLine = line.replace("[,]|[ ]|[\"]".toRegex(), "")
            val parts = cleanedLine.split(":").toTypedArray()

            if (parts.size >= 2) {
                val key = parts[0]
                val value = parts[1]
                map[key] = value
            }
        }

        return map
    }

    private fun checkName(name: String?): String? {
        return when (name.isNullOrEmpty()) {
            true -> null
            else -> name
        }
    }
}
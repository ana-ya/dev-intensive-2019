package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {

        val parts: List<String>? = fullName?.split(' ')

        val firstName = checkName(parts?.getOrNull(0))
        val lastName = checkName(parts?.getOrNull(1))

        return firstName to lastName
    }

    private fun checkName(name: String?): String? {
        return when (name.isNullOrEmpty()) {
            true -> null
            else -> name
        }
    }
}
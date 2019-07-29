package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))

    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {

    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time

    return this
}

fun Date.humanizeDiff(): String {
    val currentDate = Date()
    val diff = currentDate.time - this.time
    val absVal = diff.absoluteValue

    return when {
        absVal / SECOND in 0..1 -> "только что"
        absVal / SECOND in 1..45 -> "${if (diff > 0) "несколько секунд назад" else "через несколько секунд"}"
        absVal / SECOND in 45..75 -> "${if (diff > 0) "минуту назад" else "через минуту"}"
        absVal / SECOND in 75..45 * MINUTE / SECOND -> "${if (diff < 0) "через " else ""}${TimeUnits.MINUTE.plural(
            convertFromMs(
                absVal,
                TimeUnits.MINUTE
            )
        )}${if (diff > 0) " назад" else ""}"
        absVal / MINUTE in 45..75 -> "${if (diff > 0) "час назад" else "через час"}"
        absVal / MINUTE in 75..22 * HOUR / MINUTE -> return "${if (diff < 0) "через " else ""}${TimeUnits.HOUR.plural(
            convertFromMs(
                absVal,
                TimeUnits.HOUR
            )
        )}${if (diff > 0) " назад" else ""}"
        absVal / HOUR in 22..26 -> "${if (diff > 0) "день назад" else "через день"}"
        absVal / HOUR in 26..360 * DAY / HOUR -> return "${if (diff < 0) "через " else ""}${TimeUnits.DAY.plural(
            convertFromMs(
                absVal,
                TimeUnits.DAY
            )
        )}${if (diff > 0) " назад" else ""}"
        absVal / DAY > 360 -> "${if (diff > 0) "более года назад" else "более чем через год"}"
        else -> "Ошибка"
    }
}

fun TimeUnits.plural(number: Int): String {
    val rem: Int = if (number > 20) number % 10 else number % 20

    return when (this) {
        TimeUnits.SECOND -> {
            when (rem) {
                0, in 5..20 -> "$number секунд"
                1 -> "$number секунда"
                in 2..4 -> "$number секунды"
                else -> ""
            }
        }
        TimeUnits.MINUTE -> {
            when (rem) {
                0, in 5..20 -> "$number минут"
                1 -> "$number минута"
                in 2..4 -> "$number минуты"
                else -> ""
            }
        }
        TimeUnits.HOUR -> {
            when (rem) {
                0, in 5..20 -> "$number часов"
                1 -> return "$number час"
                in 2..4 -> "$number часа"
                else -> ""
            }
        }
        TimeUnits.DAY -> {
            when (rem) {
                0, in 5..20 -> "$number дней"
                1 -> return "$number день"
                in 2..4 -> "$number дня"
                else -> ""
            }
        }
    }
}

private fun convertFromMs(number: Long, type: TimeUnits): Int {
    return when (type) {
        TimeUnits.SECOND -> (number * 1.0 / SECOND).roundToInt()
        TimeUnits.MINUTE -> (number * 1.0 / MINUTE).roundToInt()
        TimeUnits.HOUR -> (number * 1.0 / HOUR).roundToInt()
        TimeUnits.DAY -> (number * 1.0 / DAY).roundToInt()
    }
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}
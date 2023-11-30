package mir.oslav.mockup.processor.recognition

import mir.oslav.mockup.processor.MockupProcessor
import mir.oslav.mockup.processor.data.InputOptions
import mir.oslav.mockup.processor.data.ResolvedProperty
import mir.oslav.mockup.processor.generation.isInt
import mir.oslav.mockup.processor.generation.isLong
import mir.oslav.mockup.processor.generation.isString
import mir.oslav.mockup.processor.recognition.DateTimeRecognizer.Companion.recognizableNames
import org.joda.time.DateTimeZone
import org.joda.time.IllegalFieldValueException
import org.joda.time.MutableDateTime
import org.joda.time.format.DateTimeFormat
import java.util.Calendar
import kotlin.random.Random


/**
 * Recognizer for date and time, using [recognizableNames]. Can generate Int, Long and String values. <br>
 * You can set custom dateTime format like this:
 * <code><br>
 * ksp {<br>
 *    arg(k = "mockup-date-format", v = "yyyy-MM-dd")<br>
 * }</code>
 * @since 1.1.0
 * @author Miroslav Hýbler <br>
 * created on 16.11.2023
 */
class DateTimeRecognizer constructor() : BaseRecognizer() {

    companion object {

        /**
         * Default format used when mockup-date-format is not set
         * @since 1.1.0
         */
        const val defaultFormat: String = "yyyy-MM-dd HH:mm:ss.ZZ"


        /**
         * @since 1.0.0
         */
        private val recognizableNames: List<String> = listOf(
            "date", "date_time", "dateTime",
            "created_at", "createdAt",
            "updated_at", "updatedAt",
            "deleted_at", "deletedAt",
            "date_of_birth", "dateOfBirth"
        )
    }


    /**
     * @since 1.1.0
     */
    private val calendar = Calendar.getInstance()


    /**
     * @since 1.1.0
     */
    private val outTempDateTime: MutableDateTime = MutableDateTime()


    /**
     * Tries to recognize if [property] is contextually a dateTime based on it's name
     * @return True when [recognizableNames] contains [ResolvedProperty.name] meaning that property is
     * contextually a dateTime.
     * @since 1.1.0
     */
    override fun recognize(property: ResolvedProperty): Boolean {
        return recognizableNames.contains(element = property.name)
    }


    /**
     * Generates code value for recognized [property]. Can generate [Int], [Long] and [String] values,
     * other types are not supported.
     * @since 1.1.0
     */
    override fun generateCodeValueForProperty(property: ResolvedProperty): String {
        val type = property.type
        val code = when {
            type.isLong -> "${System.currentTimeMillis()}"
            type.isInt -> "${System.currentTimeMillis() / 1000L}"
            type.isString -> generateStringDate()
            else -> throw IllegalStateException(
                "Unable to generate dateTime for type" +
                        " ${type.declaration.simpleName} (${type.declaration.qualifiedName})"
            )
        }

        return code
    }


    /**
     * Generates random date and formats it by [InputOptions.defaultDateFormat]
     * @return Code for string dateTime property, e.g. 22-05-2023
     * @since 1.1.0
     */
    private fun generateStringDate(): String {
        val options = MockupProcessor.inputOptions
            ?: throw IllegalStateException(
                "Unable to generate date, MockupProcessor.inputOptions are null!!"
            )
        val format = options.defaultDateFormat

        val year = Random.nextInt(from = 1900, until = 2100)
        val month = Random.nextInt(from = 1, until = 12)

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val day = Random.nextInt(from = 1, until = maxDay)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        val hour = Random.nextInt(from = 0, until = 23)
        val minute = Random.nextInt(from = 0, until = 59)
        val second = Random.nextInt(from = 0, until = 59)
        val millis = Random.nextInt(from = 0, until = 999)
        val zone = Random.nextInt(from = -12, until = 14)

        outTempDateTime.year = year
        outTempDateTime.monthOfYear = month
        try {
            outTempDateTime.dayOfMonth = day
        } catch (ignored: IllegalFieldValueException) {
            outTempDateTime.dayOfMonth = 1
        }
        outTempDateTime.hourOfDay = hour
        outTempDateTime.minuteOfHour = minute
        outTempDateTime.secondOfMinute = second
        outTempDateTime.millisOfSecond = millis
        outTempDateTime.zone = DateTimeZone.forOffsetHours(zone)

        val formatter = DateTimeFormat.forPattern(format)
        val stringDate = outTempDateTime.toString(formatter)
        return "\"$stringDate\""
    }


}
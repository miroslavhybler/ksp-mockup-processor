package mir.oslav.mockup.processor.data


/**
 * Holds input options from gradle. If they are not inputted, they are replaced with default properties.<br>
 * You can set custom input properties like this:
 * <code><br>
 * ksp {<br>
 *    arg(k = "mockup-date-format", v = "yyyy-MM-dd")<br>
 * }</code>
 * @since 1.1.0
 * @author Miroslav Hýbler <br>
 * created on 16.11.2023
 */
data class InputOptions constructor(
    val defaultDateFormat: String
) {
}
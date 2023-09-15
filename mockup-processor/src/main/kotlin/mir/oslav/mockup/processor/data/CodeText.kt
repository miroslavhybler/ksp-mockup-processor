package mir.oslav.mockup.processor.data


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
data class CodeText constructor(
    val lines: List<Line>
) {


    /**
     * @since 1.0.0
     */
    data class Line constructor(val lineText: String)
}
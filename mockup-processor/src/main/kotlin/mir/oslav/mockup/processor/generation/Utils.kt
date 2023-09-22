package mir.oslav.mockup.processor.generation

import java.util.Locale


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 21.09.2023
 */
fun String.decapitalized(): String {
    return replaceFirstChar { it.lowercase(Locale.getDefault()) }
}
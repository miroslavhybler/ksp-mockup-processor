package mir.oslav.mockup.processor.data

import androidx.annotation.IntRange
import mir.oslav.mockup.annotations.Mockup


/**
 * Holds extracted data from [Mockup] annotation.
 * @param count Number of items generated, keep it low for better performance and less memory consumption.
 * @param enableNullValues True if you want to allow random null values is nullable properties, false otherwise.
 * @param name Name of mockup data provider property in generated Mockup.kt object. If empty, class name
 * is used instead.
 * @see Mockup
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 16.09.2023
 */
data class MockupAnnotationData constructor(
    @IntRange(from = 1, to = 256)
    val count: Int,
    val enableNullValues: Boolean,
    val name: String,
) {
}
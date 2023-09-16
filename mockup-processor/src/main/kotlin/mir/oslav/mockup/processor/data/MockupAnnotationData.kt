package mir.oslav.mockup.processor.data

import androidx.annotation.IntRange


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 16.09.2023
 */
data class MockupAnnotationData constructor(
    @IntRange(from = 1, to = 256)
    val count: Int,
    val enableNullValues: Boolean,
    val name: String,
    val isDataClass: Boolean
) {
}
package mir.oslav.mockup.processor.generation

import mir.oslav.mockup.processor.data.CodeText
import java.io.OutputStream


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
open class FileCodeWriter constructor(
    protected val outputStream: OutputStream
) {

    /**
     * @since 1.0.0
     */
    fun writePackage(suffix: String = "") {
        outputStream += "package mir.oslav.mockup$suffix"
    }
}
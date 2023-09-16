package mir.oslav.mockup.processor.generation

import mir.oslav.mockup.processor.MockupConstants
import java.io.OutputStream


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
open class MockupObjectWriter constructor(
    protected val outputStream: OutputStream
) {


    fun generateContent() {
        outputStream += MockupConstants.generatedFileHeader
        outputStream += "\n\n"
        outputStream += "package mir.oslav.mockup"
        outputStream += "\n\n"
        outputStream += "/**\n"
        outputStream += " * All generated data are accessed via extension properties on Mockup object.\n"
        outputStream += " * @since 1.0.0\n"
        outputStream += " */\n"

        outputStream += "public object Mockup {}"
    }
}
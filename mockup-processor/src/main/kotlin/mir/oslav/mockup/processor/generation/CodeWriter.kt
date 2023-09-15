package mir.oslav.mockup.processor.generation

import mir.oslav.mockup.processor.MockupConstants
import java.io.OutputStream


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class CodeWriter constructor(
    outputStream: OutputStream
) : FileCodeWriter(
    outputStream = outputStream
) {


    fun writeFileHeaders() {
        outputStream += "${MockupConstants.generatedFileHeader}\n"
        outputStream += "package mir.oslav.mockup\n"
        outputStream += "\n"
    }


    fun writeCode(code: String) {
        outputStream += code
    }


}
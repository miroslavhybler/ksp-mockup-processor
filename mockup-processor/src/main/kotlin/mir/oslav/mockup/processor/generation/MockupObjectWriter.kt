package mir.oslav.mockup.processor.generation

import mir.oslav.mockup.processor.MockupConstants
import mir.oslav.mockup.processor.data.CodeText
import java.io.OutputStream


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class MockupObjectWriter constructor(
    outputStream: OutputStream
) : FileCodeWriter(
    outputStream = outputStream
) {


    fun generateContent() {
        outputStream += MockupConstants.generatedFileHeader
        outputStream += "\n\n"

        writePackage()

        outputStream += "\n\n"


        outputStream += "/**\n" +
                " * \"All generated data are accessed via extension properties on Mockup object.\n" +
                " */\n"


        outputStream += "public object Mockup {}"
    }
}
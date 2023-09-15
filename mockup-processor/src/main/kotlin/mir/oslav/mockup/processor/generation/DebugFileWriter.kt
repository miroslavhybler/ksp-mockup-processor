package mir.oslav.mockup.processor.generation

import java.io.OutputStream


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class DebugFileWriter constructor(
    outputStream: OutputStream
) : FileCodeWriter(
    outputStream = outputStream
) {

    fun write(code: String) {
        outputStream += code
    }

}
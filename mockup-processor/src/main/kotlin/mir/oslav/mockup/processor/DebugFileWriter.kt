package mir.oslav.mockup.processor

import java.io.OutputStream


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class DebugFileWriter constructor(
    private val outputStream: OutputStream
) {


    fun writeCoommented(code: String) {
        outputStream += code
    }

}
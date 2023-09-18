package mir.oslav.mockup.processor.generation

import mir.oslav.mockup.processor.MockupConstants
import java.io.OutputStream


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 16.09.2023
 */
class AbstractMockupDataProviderGenerator constructor(
    private val outputStream: OutputStream
) {

    /**
     * @since 1.0.0
     */
    fun generateContent() {

        //File header
        outputStream += MockupConstants.generatedFileHeader
        outputStream += "\n\n\n"
        outputStream += "package mir.oslav.mockup"
        outputStream += "\n\n\n"


        //Javadoc
        outputStream += "/**\n"
        outputStream += " * Defines the mockup data provider class\n"
        outputStream += " * @param values Generated mockup data, must be not empty\n"
        outputStream += " * @since 1.0.O\n"
        outputStream += " */\n"

        //Code
        outputStream += "public abstract class MockupDataProvider<T : Any> constructor(\n"
        outputStream += "\t private val values: List<T> = emptyList()\n"
        outputStream += ") {\n"
        outputStream += "\n"
        outputStream += "\tval singe: T get() = values.random()\n"
        outputStream += "\n"
        outputStream += "\tval list: List<T> get() = values\n"
        outputStream += "}"
    }

}
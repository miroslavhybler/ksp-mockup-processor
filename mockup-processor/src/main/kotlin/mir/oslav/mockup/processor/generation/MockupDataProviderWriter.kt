package mir.oslav.mockup.processor.generation

import mir.oslav.mockup.processor.MockupConstants
import mir.oslav.mockup.processor.data.MockupClass
import java.io.OutputStream


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 16.09.2023
 */
class MockupDataProviderWriter constructor(

) {

    fun generateContent(
        outputStream: OutputStream,
        clazz: MockupClass,
        generatedValuesContent: String
    ) {
        val name = clazz.name
        val declaration = clazz.type.declaration
        val type = declaration.simpleName.getShortName()
        val providerClassName = "${name}MockupProvider"

        outputStream += MockupConstants.generatedFileHeader
        outputStream += "\n\n"
        outputStream += "package mir.oslav.mockup.providers"
        outputStream += "\n\n"
        outputStream += "import mir.oslav.mockup.MockupDataProvider\n"
        outputStream += "import ${declaration.packageName.asString()}.${declaration.simpleName.getShortName()}\n"
        outputStream += "\n"

        outputStream += "/**\n"
        outputStream += " * Holds the generated mockup data for $name class. Single item can be accessed by [${providerClassName}.singe] \n"
        outputStream += " * and multiple items with [${providerClassName}.list].\n"
        outputStream += " * @since 1.0.0\n"
        outputStream += " */\n"


        outputStream += "internal class $providerClassName constructor(): MockupDataProvider<$type>(\n"
        outputStream += "\tvalues = $generatedValuesContent\n"
        outputStream += ") {\n"
        outputStream += "}"
    }

}
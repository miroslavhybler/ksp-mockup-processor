package mir.oslav.mockup.processor.generation

import mir.oslav.mockup.processor.MockupConstants
import mir.oslav.mockup.processor.data.MockupType
import java.io.OutputStream


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 16.09.2023
 */
class MockupDataProviderGenerator constructor(

) {

    fun generateContent(
        outputStream: OutputStream,
        clazz: MockupType.MockUpped,
        generatedValuesContent: String
    ): String {
        val name = clazz.name
        val declaration = clazz.type.declaration
        val type = declaration.simpleName.getShortName()
        val providerClassName = "${name}MockupProvider"
        val writtenImports = ArrayList<String>()

        //Header, package name and import of base class
        outputStream += MockupConstants.generatedFileHeader
        outputStream += "\n\n"
        outputStream += "package mir.oslav.mockup.providers"
        outputStream += "\n\n"
        outputStream += "import mir.oslav.mockup.MockupDataProvider\n"

        //Used types imports
        clazz.imports.sortedDescending().forEach { qualifiedName ->
            if (!writtenImports.contains(qualifiedName)) {
                outputStream += "import $qualifiedName\n"
                writtenImports.add(qualifiedName)
            }
        }

        //Javadoc
        outputStream += "\n"
        outputStream += "/**\n"
        outputStream += " * Holds the generated mockup data for $name class.\n"
        outputStream += " * Single item can be accessed by [${providerClassName}.singe] \n"
        outputStream += " * Multiple items with [${providerClassName}.list].\n"
        outputStream += " * @since 1.0.0\n"
        outputStream += " */\n"


        //Class definition
        outputStream += "public class $providerClassName internal constructor(): MockupDataProvider<$type>(\n"
        outputStream += "\tvalues = $generatedValuesContent\n"
        outputStream += ") {\n"
        outputStream += "}"

        return providerClassName
    }

}
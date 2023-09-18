package mir.oslav.mockup.processor.generation

import mir.oslav.mockup.processor.MockupConstants
import mir.oslav.mockup.processor.data.MockupObjectMember
import java.io.OutputStream
import java.util.Locale


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
open class MockupObjectGenerator constructor(
    protected val outputStream: OutputStream
) {


    fun generateContent(providers: List<MockupObjectMember>) {

        //Header
        outputStream += MockupConstants.generatedFileHeader
        outputStream += "\n\n"
        outputStream += "package mir.oslav.mockup"
        outputStream += "\n\n"

        //Imports
        providers.forEach { provider ->
            val import = "import ${provider.providerClassPackage}.${provider.providerClassName}"
            outputStream += "$import\n"
        }

        outputStream += "\n"

        //Mockup object documentation
        outputStream += "/**\n"
        outputStream += " * All generated data are accessed via public properties on Mockup object.\n"
        outputStream += " * @since 1.0.0\n"
        outputStream += " */\n"

        outputStream += "public object Mockup {\n"

        //Mockup data providers as public values of object
        providers.forEach { provider ->
            outputStream += "\n"

            //Item documentation
            outputStream += "\t/**\n"
            outputStream += "\t * Provides generated data for ${provider.itemClassName}\n"
            outputStream += "\t */\n"
            val valName = provider.itemClassName.replaceFirstChar { it.lowercase(Locale.ROOT) }
            val valDeclaration = "public val $valName"
            val valType = ": ${provider.providerClassName}"
            val constructorCall = "${provider.providerClassName}()"

            outputStream += "\t$valDeclaration$valType = $constructorCall\n"
            outputStream += "\n"
        }


        outputStream += "}"
    }
}
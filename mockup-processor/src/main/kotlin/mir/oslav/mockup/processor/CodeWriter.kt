package mir.oslav.mockup.processor

import java.io.OutputStream


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class CodeWriter constructor(private val outputStream: OutputStream) {



    fun writeFileHeaders() {
        outputStream += "${MockupConstants.generatedFileHeader}\n"
        outputStream += "package mir.oslav.mockup\n"
        outputStream += "\n"
    }

    fun writeImports(imports: List<String>) {
        imports.forEach { import ->
            outputStream += "import $import\n"
        }
        outputStream += "\n"
    }



    fun writeCode(code: String) {
        outputStream += code
    }


}
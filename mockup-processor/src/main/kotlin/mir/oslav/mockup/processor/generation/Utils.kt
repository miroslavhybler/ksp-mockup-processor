package mir.oslav.mockup.processor.generation

import java.io.OutputStream


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
operator fun OutputStream.plusAssign(other: String) {
    this.write(other.toByteArray())
}

operator fun DebugFileWriter.plusAssign(other:String) {
    this.write(code= other)
}
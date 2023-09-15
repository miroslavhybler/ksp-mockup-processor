package mir.oslav.mockup.processor

import java.io.OutputStream


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
operator fun OutputStream.plusAssign(other: String) {
    this.write(other.toByteArray())
}
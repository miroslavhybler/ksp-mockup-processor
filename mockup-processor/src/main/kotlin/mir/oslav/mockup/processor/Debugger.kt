@file:Suppress("ConstPropertyName")

package mir.oslav.mockup.processor

import java.io.OutputStream


/**
 * Debugger for creating longs into generated code. Should be always dissabled for release.
 * @since 1.1.3
 * @author Miroslav Hýbler <br>
 * created on 02.01.2024
 */
object Debugger {


    /**
     * OutputStream used to write logs
     * @since 1.1.3
     */
    private var outputStream: OutputStream? = null


    /**
     * True if writing logs into generated file through [outputStream] is enabled, false otherwise. This
     * property is never changing in code, it must be set constantly.
     * @since 1.1.3
     */
    const val isDebugEnabled: Boolean = true

    fun setOutputStream(outputStream: OutputStream) {
        if (isDebugEnabled) {
            this.outputStream = outputStream
        }
    }

    fun write(text: String) {
        if (isDebugEnabled) {

            outputStream?.write("//     $text\n".toByteArray())
        }
    }

    fun close() {
        outputStream?.close()
    }
}
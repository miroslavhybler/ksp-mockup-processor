package mir.oslav.mockup.processor.generation

import com.google.devtools.ksp.symbol.KSType
import java.io.OutputStream


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
operator fun OutputStream.plusAssign(other: String) {
    this.write(other.toByteArray())
}


/**
 * @since 1.0.0
 */
val KSType.isShort: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.Short"


/**
 * @since 1.0.0
 */
val KSType.isInt: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.Int"


/**
 * @since 1.0.0
 */
val KSType.isLong: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.Long"


/**
 * @since 1.0.0
 */
val KSType.isFloat: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.Float"


/**
 * @since 1.0.0
 */
val KSType.isDouble: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.Double"


/**
 * @since 1.0.0
 */
val KSType.isList: Boolean
    get() = declaration.qualifiedName?.asString() == " kotlin.collections.List"

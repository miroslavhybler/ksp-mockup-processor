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
val KSType.isBoolean: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.Boolean"


/**
 * @since 1.0.0
 */
val KSType.isString: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.String"


/**
 * @since 1.0.0
 */
val KSType.isList: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.collections.List"


/**
 * @since 1.0.0
 */
val KSType.isArray: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.Array"


/**
 * @since 1.0.0
 */
val KSType.isShortArray: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.ShortArray"




/**
 * @since 1.0.0
 */
val KSType.isIntArray: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.IntArray"



/**
 * @since 1.0.0
 */
val KSType.isLongArray: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.LongArray"


/**
 * @since 1.0.0
 */
val KSType.isFloatArray: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.FloatArray"


/**
 * @since 1.0.0
 */
val KSType.isDoubleArray: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.DoubleArray"


/**
 * @since 1.0.0
 */
val KSType.isCharArray: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.CharArray"


/**
 * @since 1.0.0
 */
val KSType.isByteArray: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.ByteArray"


/**
 * @since 1.0.0
 */
val KSType.isBooleanArray: Boolean
    get() = declaration.qualifiedName?.asString() == "kotlin.BooleanArray"
package mir.oslav.mockup.processor.generation

import com.google.devtools.ksp.symbol.KSAnnotation


/**
 * @author Miroslav Hýbler <br>
 * created on 20.05.2024
 * @since 1.1.6
 */
val KSAnnotation.isIntRange: Boolean
    get() = this.shortName.asString() == "IntRange"


/**
 * @since 1.1.6
 */
val KSAnnotation.isFloatRange: Boolean
    get() = this.shortName.asString() == "FloatRange"


/**
 * @since 1.1.6
 */
val KSAnnotation.isIntDef: Boolean
    get() = this.shortName.asString() == "IntDef"


/**
 * @since 1.2.2
 */
val KSAnnotation.isStringDef: Boolean
    get() = this.shortName.asString() == "StringDef"


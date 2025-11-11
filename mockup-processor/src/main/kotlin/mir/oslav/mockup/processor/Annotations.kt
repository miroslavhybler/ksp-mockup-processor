@file:Suppress("RedundantVisibilityModifier")

package mir.oslav.mockup.processor

import com.google.devtools.ksp.symbol.KSAnnotation


/**
 * @author Miroslav Hýbler <br>
 * created on 08.11.2025
 * @since 1.2.2
 */
public object Annotations {


    /**
     * Util function to procces androidx range based annotations (like [androidx.annotation.IntRange], [androidx.annotation.FloatRange])
     * @since 1.2.2
     */
    public fun <T : Comparable<T>> processRangeAnnotation(
        annotation: KSAnnotation,
        min: T,
        max: T,
    ): Pair<T, T> {
        val fromArgument = annotation.arguments.find(predicate = { argument ->
            argument.name?.asString() == "from"
        })?.value as? T
        val toArgument = annotation.arguments.find(predicate = { argument ->
            argument.name?.asString() == "to"
        })?.value as? T

        val usedMin = fromArgument ?: min
        val usedMax = toArgument ?: max

        return usedMin to usedMax
    }


    /**
     * Util function to procces androidx def based annotations (like [androidx.annotation.IntDef])
     * @since 1.2.2
     */
    public fun <T : Any> proccessDefAnnotation(
        annotation: KSAnnotation,
    ): List<T> {
        val values = annotation.arguments.find(predicate = { argument ->
            argument.name?.asString() == "value"
        })?.value as? List<T>


        return values ?: emptyList()
    }
}
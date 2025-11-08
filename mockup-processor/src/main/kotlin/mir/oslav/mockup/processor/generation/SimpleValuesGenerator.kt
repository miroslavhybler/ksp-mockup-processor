package mir.oslav.mockup.processor.generation

import mir.oslav.mockup.processor.data.MockupType
import mir.oslav.mockup.processor.data.ResolvedProperty
import mir.oslav.mockup.processor.data.WrongTypeException
import mir.oslav.mockup.processor.data.loremIpsumWords
import kotlin.random.Random


/**
 * TODO - handle properties with lazy declaration (do not generate value)
 * @author Miroslav Hýbler <br>
 * created on 17.05.2024
 * @since 1.1.6
 */
class SimpleValuesGenerator constructor() {

    /**
     * Generates random code for value of [property], e.g. `id = 123`
     * @param property Single property of class
     * @return Generated code
     * @throws WrongTypeException
     * @since 1.1.6
     */
    fun generate(
        property: MockupType.Simple,
        resolvedProperty: ResolvedProperty,
    ): String {
        val type = property.type
        return when {
            //Simple types and string
            type.isShort -> {
                "${
                    Random.nextInt(
                        from = Short.MIN_VALUE.toInt(),
                        until = Short.MAX_VALUE.toInt(),
                    )
                }"
            }

            type.isInt -> {
                generateIntegerValue(
                    property = property,
                    resolvedProperty = resolvedProperty,
                )
            }

            type.isFloat -> {
                generateFloatValue(
                    property = property,
                    resolvedProperty = resolvedProperty,
                )
            }

            type.isLong -> "${Random.nextInt()}"
            type.isDouble -> "${Random.nextDouble()}"
            type.isBoolean -> "${Random.nextBoolean()}"
            type.isString -> "\"${
                loremIpsumWords(
                    wordCount = Random.nextInt(
                        from = 2,
                        until = 60
                    )
                )
            }\""

            else -> throw WrongTypeException(expectedType = "Simple", givenType = "$type")
        }
    }


    /**
     * ### Refactored in 1.2.2
     * [MockupType.Simple.Source.IntNumber] was introduced to handle values and annotations based limitations.
     * @param property
     * @param resolvedProperty
     * @return Generated code consisting of string holding generated int value
     * @since 1.1.6
     */
    private fun generateIntegerValue(
        property: MockupType.Simple,
        resolvedProperty: ResolvedProperty
    ): String {

        val source = property.source as? MockupType.Simple.Source.IntNumber
            ?: throw WrongTypeException(
                expectedType = "IntNumber",
                givenType = "${property.source}"
            )


        return when (source) {
            is MockupType.Simple.Source.IntNumber.Range -> {
                val intValue = Random.nextInt(from = source.from, until = source.to)
                "$intValue"
            }

            is MockupType.Simple.Source.IntNumber.Def -> {
                val intValue = source.values.random()
                "$intValue"
            }

            is MockupType.Simple.Source.IntNumber.Random -> {
                val intValue = Random.nextInt()
                "$intValue"
            }
        }
    }


    /**
     * @param property
     * @param resolvedProperty
     * @return Generated code consisting of string holding generated float value
     * @since 1.1.6
     */
    private fun generateFloatValue(
        property: MockupType.Simple,
        resolvedProperty: ResolvedProperty
    ): String {
        val annotations = if (resolvedProperty.isInPrimaryConstructorProperty) {
            resolvedProperty.primaryConstructorDeclaration?.annotations ?: emptySequence()
        } else property.property.annotations

        var from = 0f
        var to = 5000f
        for (annotation in annotations) {
            if (annotation.isFloatRange) {
                val fromArgument = annotation.arguments.find(predicate = { argument ->
                    argument.name?.asString() == "from"
                })?.value as? Double
                val toArgument = annotation.arguments.find(predicate = { argument ->
                    argument.name?.asString() == "to"
                })?.value as? Double

                if (fromArgument != null) {
                    from = fromArgument.toFloat()
                }
                if (toArgument != null) {
                    to = toArgument.toFloat()
                }
                break
            }
        }

        val floatValue = Random.nextFloat()
            .coerceIn(minimumValue = from, maximumValue = to)
        return "${floatValue}f"
    }
}
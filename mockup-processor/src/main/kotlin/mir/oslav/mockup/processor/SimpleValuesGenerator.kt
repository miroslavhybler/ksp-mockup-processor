package mir.oslav.mockup.processor

import mir.oslav.mockup.processor.data.MockupType
import mir.oslav.mockup.processor.data.ResolvedProperty
import mir.oslav.mockup.processor.data.WrongTypeException
import mir.oslav.mockup.processor.data.loremIpsumWords
import mir.oslav.mockup.processor.generation.isBoolean
import mir.oslav.mockup.processor.generation.isDouble
import mir.oslav.mockup.processor.generation.isFloat
import mir.oslav.mockup.processor.generation.isInt
import mir.oslav.mockup.processor.generation.isLong
import mir.oslav.mockup.processor.generation.isShort
import mir.oslav.mockup.processor.generation.isString
import kotlin.random.Random


/**
 * @author Miroslav Hýbler <br>
 * created on 17.05.2024
 * @since 1.1.6
 */
class SimpleValuesGenerator constructor() {


    /**
     * Generates random code for value of [property], e.g. <code>id = 123</code>
     * @param property Single property of class
     * @return Generated code
     * @throws WrongTypeException
     * @since 1.1.6
     */
    //TODO must be all properties and primary constructor parameters
    fun generate(
        property: MockupType.Simple,
        resolvedProperty: ResolvedProperty,
    ): String {
        val type = property.type
        return when {
            //Simple types and string
            type.isShort -> "${Random.nextInt(from = 0, until = Short.MAX_VALUE.toInt())}"
            type.isInt -> generateIntegerValue(
                property = property,
                resolvedProperty = resolvedProperty
            )

            type.isLong -> "${Random.nextInt()}"
            type.isFloat -> "${Random.nextFloat()}f"
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


    private fun generateIntegerValue(
        property: MockupType.Simple,
        resolvedProperty: ResolvedProperty
    ): String {
        val annotations = if (resolvedProperty.isInPrimaryConstructorProperty) {
            resolvedProperty.primaryConstructorDeclaration?.annotations ?: emptySequence()
        } else property.property.annotations


        Debugger.write(text = "${property.name}: ${annotations.joinToString { it.shortName.asString() }}")

        var from = 0
        var to = 5000
        for (annotation in annotations) {
            if (annotation.shortName.asString() == "IntRange") {
                Debugger.write(text = "${annotation.arguments.joinToString { it.name?.asString() ?: "null" }}")
                Debugger.write(text = "${annotation.arguments.joinToString { it.value?.toString() ?: "null" }}")


                val fromArgument = annotation.arguments.find { argument ->
                    argument.name?.asString() == "from"
                }?.value as? Long
                val toArgument = annotation.arguments.find { argument ->
                    argument.name?.asString() == "to"
                }?.value as? Long

                Debugger.write(text = "from: $fromArgument to: $toArgument")


                if (fromArgument != null) {
                    from = fromArgument.toInt()
                }
                if (toArgument != null) {
                    to = toArgument.toInt()
                }

            }
        }

        return "${Random.nextInt(from = from, until = to)}"
    }
}
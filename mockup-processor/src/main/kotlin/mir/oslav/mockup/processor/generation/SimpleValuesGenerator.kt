package mir.oslav.mockup.processor.generation

import mir.oslav.mockup.processor.Debugger
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
            type.isShort -> "${Random.nextInt(from = 0, until = Short.MAX_VALUE.toInt())}"
            type.isInt -> {
                generateIntegerValue(property = property, resolvedProperty = resolvedProperty)
            }

            type.isFloat -> {
                generateFloatValue(property = property, resolvedProperty = resolvedProperty)
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
     * @param property
     * @param resolvedProperty
     * @return Generated code consisting of string holding generated int value
     * @since 1.1.6
     */
    private fun generateIntegerValue(
        property: MockupType.Simple,
        resolvedProperty: ResolvedProperty
    ): String {
        val annotations = if (resolvedProperty.isInPrimaryConstructorProperty) {
            resolvedProperty.primaryConstructorDeclaration?.annotations ?: emptySequence()
        } else property.property.annotations

        var from = 0
        var to = 5000
        var values: Array<Int> = emptyArray()
        for (annotation in annotations) {
            Debugger.write(text = "annotation $annotation")
            if (annotation.isIntRange) {
                val fromArgument = annotation.arguments.find { argument ->
                    argument.name?.asString() == "from"
                }?.value as? Long
                val toArgument = annotation.arguments.find { argument ->
                    argument.name?.asString() == "to"
                }?.value as? Long

                if (fromArgument != null) {
                    from = fromArgument.toInt()
                }
                if (toArgument != null) {
                    to = toArgument.toInt()
                }
                break
            } else if (annotation.isIntDef) {
                //TODO solve how to find annotation's annotations for def annotations
                annotation.arguments.forEach {
                    Debugger.write(text = "argument: $it value: ${it.value}")
                }

                val valuesArgument = annotation.arguments.find { argument ->
                    argument.name?.asString() == "value"
                }
                val valuesArray = valuesArgument?.value as? Array<Int>
                if (valuesArray != null) {
                    values = valuesArray
                }
                Debugger.write(text = "ARGUMENT FOUND!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                Debugger.write(text = "argument: $valuesArgument value: ${valuesArgument?.value}")
                break
            }
        }

        if (values.isNotEmpty()) {
            return "${values.random()}"
        }
        return "${Random.nextInt(from = from, until = to)}"
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
                val fromArgument = annotation.arguments.find { argument ->
                    argument.name?.asString() == "from"
                }?.value as? Double
                val toArgument = annotation.arguments.find { argument ->
                    argument.name?.asString() == "to"
                }?.value as? Double

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
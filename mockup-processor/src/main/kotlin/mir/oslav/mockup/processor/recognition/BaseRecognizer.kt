package mir.oslav.mockup.processor.recognition

import mir.oslav.mockup.processor.data.ResolvedProperty


/**
 * @since 1.1.O
 * @author Miroslav Hýbler <br>
 * created on 20.10.2023
 */
abstract class BaseRecognizer constructor() {


    /**
     * Tries to recognize property
     * @return True when [property] was recognized by recognizer
     * @since 1.1.0
     */
    abstract fun recognize(
        property: ResolvedProperty
    ): Boolean


    /**
     * Generates code value for given [property] wich was recognized before by [recognize]
     * @param property Property which was recognized before
     * @since 1.1.0
     */
    abstract fun generateCodeValueForProperty(
        property: ResolvedProperty,
    ): String
}
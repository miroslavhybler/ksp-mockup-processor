package mir.oslav.mockup.processor.recognition

import mir.oslav.mockup.processor.data.ResolvedProperty


/**
 * Recognizer is a component that is trying to recognize contextual meaning of code property and
 * generate appropriate value. E.g. if property would be called "username", generated value will be
 * "John Doe".
 * @since 1.1.O
 * @author Miroslav Hýbler <br>
 * created on 20.10.2023
 */
abstract class BaseRecognizer constructor() {


    /**
     * Tries to recognize [property]
     * @param property Class property to be recognized
     * @param containingClassName Name of class which contains [property]
     * @return True when [property] was recognized by recognizer
     * @since 1.1.0
     */
    @Deprecated(
        message = "Refactor in 1.2.0, recognition and code generation in two steps meains more code and twice as time. " +
                "Put recognition and generation in one step."
    )
    abstract fun recognize(
        property: ResolvedProperty,
        containingClassName: String
    ): Boolean


    /**
     * Generates code value for given [property] which was recognized before by [recognize]
     * @param property Property which was recognized before
     * @since 1.1.0
     */
    @Deprecated(
        message = "Refactor in 1.2.0, recognition and code generation in two steps meains more code and twice as time. " +
                "Put recognition and generation in one step."
    )
    abstract fun generateCodeValueForProperty(
        property: ResolvedProperty,
    ): String
}
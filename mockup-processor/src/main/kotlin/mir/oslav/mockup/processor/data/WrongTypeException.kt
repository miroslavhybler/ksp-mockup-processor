package mir.oslav.mockup.processor.data


/**
 * Exception to be thrown when some part of the processor would receive type that is not expected.
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 22.09.2023
 */
class WrongTypeException constructor(
    expectedType: String,
    givenType: String
) : IllegalStateException(
    "Expected $expectedType but got $givenType"
) {
}
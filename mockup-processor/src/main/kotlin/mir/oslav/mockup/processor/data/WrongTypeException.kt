package mir.oslav.mockup.processor.data

import com.google.devtools.ksp.symbol.KSType
import java.lang.IllegalStateException


/**
 * TODO message
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
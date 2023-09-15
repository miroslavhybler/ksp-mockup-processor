package mir.oslav.mockup.processor.data

import com.google.devtools.ksp.symbol.KSType


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
data class MockupClassMember constructor(
    val name: String,
    val type: KSType,
    val isNullable: Boolean
) {



}
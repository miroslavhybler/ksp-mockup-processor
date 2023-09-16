package mir.oslav.mockup.processor.data

import com.google.devtools.ksp.symbol.KSType


/**
 * @param members
 * @param imports
 * @param name
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
data class MockupClass constructor(
    val members: List<MockupClassMember>,
    val imports: List<String>,
    val name: String,
    val type: KSType
) {

}
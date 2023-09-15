package mir.oslav.mockup.processor

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
data class Parameter constructor(
    val name: String,
    val type: KSTypeReference,
    val packageName: String,
    val clazzName: String,
    val isNullable: Boolean
) {



}
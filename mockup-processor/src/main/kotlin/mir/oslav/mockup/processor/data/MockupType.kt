package mir.oslav.mockup.processor.data

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import mir.oslav.mockup.annotations.Mockup
import mir.oslav.mockup.processor.generation.isSimpleType


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 21.09.2023
 */
sealed class MockupType<out D : KSDeclaration> private constructor(
    open val name: String,
    open val type: KSType,
    open val declaration: D
) {


    val packageName: String get() = declaration.packageName.asString()


    /**
     * Represents a simple data type (etc. [Int], [String], ...), [KSType.isSimpleType] must always
     * be true, otherwise type was not recognized correctly.
     * @see KSType.isSimpleType
     * @since 1.0.0
     */
    data class Simple constructor(
        override val name: String,
        override val type: KSType,
        override val declaration: KSDeclaration
    ) : MockupType<KSDeclaration>(
        name = name,
        type = type,
        declaration = declaration
    ) {
    }


    /**
     * Representing type for classes annotated with @[Mockup] annotation.
     * @since1.0.0
     */
    data class Mocked constructor(
        override val name: String,
        override val type: KSType,
        override val declaration: KSClassDeclaration,
        val data: MockupAnnotationData,
        val imports: List<String>,
        val properties: List<Property>
    ) : MockupType<KSClassDeclaration>(
        name = name,
        type = type,
        declaration = declaration
    ) {

        val isDataClass: Boolean get() = data.isDataClass
    }


    /**
     * Represents generic collection type
     * @since 1.0.0
     */
    data class Collection constructor(
        override val name: String,
        override val type: KSType,
        override val declaration: KSClassDeclaration,
        val elementType: MockupType<*>,
        val imports: List<String>,
    ) : MockupType<KSClassDeclaration>(
        name = name,
        type = type,
        declaration = declaration
    )


    /**
     * TODO docs
     * Represents property type
     * @since 1.0.0
     */
    data class Property constructor(
        override val name: String,
        override val type: KSType,
        override val declaration: KSDeclaration,
        val imports: List<String>,
        val resolvedType: MockupType<*>,
        val isMutable: Boolean

    ): MockupType<KSDeclaration>(
        name=name,
        type=type,
        declaration= declaration
    )

}
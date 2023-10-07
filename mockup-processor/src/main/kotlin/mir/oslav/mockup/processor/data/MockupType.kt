package mir.oslav.mockup.processor.data

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import mir.oslav.mockup.annotations.Mockup


/**
 * Wrapper class around [KSType] containing additional data
 * @param name Name of type class or property name based on context
 * @param type Resolved [KSType]
 * @param declaration
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 21.09.2023
 */
sealed class MockupType<out D : KSDeclaration> private constructor(
    open val name: String,
    open val type: KSType,
    open val declaration: D
) {


    /**
     * Package name of the [declaration]
     * @since 1.0.0
     */
    val packageName: String get() = declaration.packageName.asString()


    /**
     * Represents a simple data type (etc. [Int], [String], ...), [KSType.isSimpleType] must always
     * be true, otherwise type was not recognized correctly and [WrongTypeException] would be thrown
     * elsewhere.
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
    )

    /**
     * Representing type for classes annotated with @[Mockup] annotation.
     * @since 1.0.0
     */
    data class MockUpped constructor(
        override val name: String,
        override val type: KSType,
        override val declaration: KSClassDeclaration,
        val data: MockupAnnotationData,
        val imports: List<String>,
        val properties: List<ResolvedProperty>
    ) : MockupType<KSClassDeclaration>(
        name = name,
        type = type,
        declaration = declaration
    ) {

    }


    /**
     * Represents generic collection type,  [KSType.isGenericCollectionType] must be true for [type]
     * @param elementType Resolved [MockupType] of elements,
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
    ) {

    }

    /**
     * Represents a array with known type (e.g. intArray, floatArray, ...).
     * @see KSType.isFixedArrayType
     * @since 1.0.0
     */
    data class FixedTypeArray constructor(
        override val name: String,
        override val type: KSType,
        override val declaration: KSDeclaration,
    ) : MockupType<KSDeclaration>(
        name = name,
        type = type,
        declaration = declaration
    )
}
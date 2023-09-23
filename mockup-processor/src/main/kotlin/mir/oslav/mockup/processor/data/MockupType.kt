package mir.oslav.mockup.processor.data

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import mir.oslav.mockup.annotations.Mockup
import mir.oslav.mockup.processor.generation.isFixedArrayType
import mir.oslav.mockup.processor.generation.isGenericCollectionType
import mir.oslav.mockup.processor.generation.isSimpleType


/**
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 21.09.2023
 */
//TODO Fixed arrays
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
    data class MockUpped constructor(
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
        override val declaration: KSClassDeclaration,
    ) : MockupType<KSClassDeclaration>(
        name = name,
        type = type,
        declaration = declaration
    )


    /**
     * Representing class property
     * @param name Name of the property from class (e.g. id, name, ...), name of the property's type
     * is in [resolvedType]
     * @param type Raw [KSType] of property
     * @param declaration
     * @param imports
     * @param resolvedType
     * @param isMutable True when property is mutable (declared with var keyword in source). False
     * when property is not mutable (declared with "val" keyword in source)
     * Represents property type
     * @since 1.0.0
     */
    //TODO isPrimaryConstructorProperty
    data class Property constructor(
        override val name: String,
        override val type: KSType,
        override val declaration: KSDeclaration,
        val imports: List<String>,
        val resolvedType: MockupType<*>,
        val isMutable: Boolean,
        val isPrimaryConstructorProperty: Boolean

    ) : MockupType<KSDeclaration>(
        name = name,
        type = type,
        declaration = declaration
    )

}
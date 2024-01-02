package mir.oslav.mockup.processor.data

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType

/**
 * Representing class property
 * @param name Name of the property from class (e.g. id, name, ...), name of the property's type
 * is in [resolvedType]
 * @param type Raw [KSType] of property
 * @param declaration Original property declaration.
 * @param imports All imports that are needed by this property.
 * @param resolvedType Wrapped type of this property
 * @param isMutable True when property is mutable (declared with var keyword in source). False
 * when property is not mutable (declared with "val" keyword in source)
 * @param isInPrimaryConstructorProperty True when property is declared in primary constructor
 * @param containingClassDeclaration Name of the class where is this property declared and used
 * of the class.
 * @since 1.0.0
 */
data class ResolvedProperty constructor(
    val name: String,
    val type: KSType,
    val declaration: KSDeclaration,
    val imports: List<String>,
    val resolvedType: MockupType<*>,
    val isMutable: Boolean,
    val isInPrimaryConstructorProperty: Boolean,
    val containingClassDeclaration: KSClassDeclaration
) {

    /**
     * True when this property is <b>not</b> declared in primary constructor, false otherwise.
     * @see isInPrimaryConstructorProperty
     * @since 1.0.0
     */
    val isNotInPrimaryConstructorProperty: Boolean
        get() = !isInPrimaryConstructorProperty


    val containingClassName: String
        get() = containingClassDeclaration.simpleName.asString()
}
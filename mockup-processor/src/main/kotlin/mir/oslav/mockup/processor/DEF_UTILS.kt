package mir.oslav.mockup.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import kotlin.reflect.KClass


/**
 * @author Miroslav Hýbler <br>
 * created on 25.10.2025
 * @since 1.2.2
 */
fun KSPropertyDeclaration.isAnnotatedWithOrHasAnnotation(
    target: KClass<*>,
): Boolean {
    if (this.isAnnotatedWith(target = target)) {
        return true
    }

    val isSomeAnnotationAnnotated = annotations.any(predicate = { annotation ->
        return annotation.isAnnotatedWith(target = target)
    })
    return isSomeAnnotationAnnotated
}


/**
 * @since 1.2.2
 */
fun KSAnnotated.isAnnotatedWith(target: KClass<*>): Boolean {
    return annotations.any(predicate = { annotation ->
        return annotation.isAnnotatedWith(target = target)
    })
}


/**
 * @since 1.2.2
 */
fun KSAnnotation.isAnnotatedWith(
    target: KClass<*>,
): Boolean {
    val annotationType = annotationType.resolve()
    val annotationDeclaration = annotationType.declaration

    // Check if THIS annotation (like @Type) is annotated with @IntDef
    return annotationDeclaration.annotations.any(
        predicate = { metaAnnotation ->
            val qualifiedName = metaAnnotation.annotationType.resolve()
                .declaration.qualifiedName?.asString()
            qualifiedName == target.qualifiedName
        }
    )
}

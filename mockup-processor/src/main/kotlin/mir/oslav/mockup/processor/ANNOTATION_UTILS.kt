package mir.oslav.mockup.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import kotlin.reflect.KClass


/**
 * TODO docs on functions
 * @author Miroslav Hýbler <br>
 * created on 25.10.2025
 * @since 1.2.2
 */
fun KSDeclaration.findAnnotationOrAnnotatedAnnotation(
    target: KClass<*>,
): KSAnnotation? {
    if (this.isAnnotatedWith(target = target)) {
        return findAnnotation(target = target)
    }

    val targetAnnotation = this.annotations.find(predicate = { annotation ->
        annotation.isAnnotatedWith(target = target)
    })
    return targetAnnotation
}


/**
 * @since 1.2.2
 */
fun KSAnnotated.isAnnotatedWith(target: KClass<*>): Boolean {
    return this.annotations.any(predicate = { annotation ->
        return annotation.isAnnotatedWith(target = target)
    })
}


/**
 * @since 1.2.2
 */
fun KSAnnotated.findAnnotation(
    target: KClass<*>,
): KSAnnotation? {
    return this.annotations.find(predicate = { annotation ->
        annotation.isAnnotatedWith(target = target)
    })
}


/**
 * @since 1.2.2
 */
fun KSAnnotation.isAnnotatedWith(
    target: KClass<*>,
): Boolean {
    val annotationType = this.annotationType.resolve()
    val annotationDeclaration = annotationType.declaration

    return annotationDeclaration.annotations.any(
        predicate = { metaAnnotation ->
            val qualifiedName = metaAnnotation.annotationType.resolve()
                .declaration.qualifiedName?.asString()
            qualifiedName == target.qualifiedName
        }
    )
}

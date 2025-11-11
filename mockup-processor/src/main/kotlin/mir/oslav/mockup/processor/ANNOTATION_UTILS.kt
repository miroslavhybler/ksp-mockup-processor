package mir.oslav.mockup.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import kotlin.reflect.KClass


/**
 * TODO docs on functions
 * @author Miroslav Hýbler <br>
 * created on 25.10.2025
 * @since 1.2.2
 */
fun KSAnnotated.findAnnotationInAnnotationTree(
    target: KClass<*>,
): KSAnnotation? {
    if (this.annotations.count() == 0) {
        return null
    }

    val firstLevelAnnotation = this.annotations
        .find(predicate = { annotation ->
            annotation.isInstanceOf(target = target)
        })

    if (firstLevelAnnotation != null) {
        //This property is annotated by target itself
        return firstLevelAnnotation
    }

    //This is annotation that is annotated by the target
    val annotated: KSAnnotation = this.annotations
        .find(
            predicate = { annotation ->
                annotation.isAnnotatedWith(target = target)
            }
        ) ?: return null

    //Resolved annotation type
    val annotatedType = annotated.annotationType.resolve()
    //Actual declaration of the annotation holding the annotations of the found annotation
    val annotatedDeclaration = annotatedType.declaration

    val targetAnnotation = annotatedDeclaration.findAnnotationInstance(target = target)
    return targetAnnotation
}


/**
 * Tries to find annotation by [target] in [KSAnnotated.annotations].
 * @since 1.2.2
 */
fun KSAnnotated.findAnnotationInstance(
    target: KClass<*>,
): KSAnnotation? {
    return this.annotations.find(predicate = { annotation ->
        annotation.isInstanceOf(target = target)
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
        predicate = { annotation ->
            annotation.isInstanceOf(target = target)
        }
    )
}


/**
 * @since 1.2.2
 */
fun KSAnnotation.isInstanceOf(target: KClass<*>): Boolean {
    val qualifiedName = this.annotationType.resolve()
        .declaration.qualifiedName?.asString()
    return qualifiedName == target.qualifiedName
}
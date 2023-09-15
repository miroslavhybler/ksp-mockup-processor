@file:Suppress("RedundantConstructorKeyword", "unused", "RedundantVisibilityModifier")

package mir.oslav.mockup.annotations


/**
 * Annotates desired class for mockup data generation
 * @param count Number of items generated, keep it low for better performance and less memory consumption
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
public annotation class Mockup constructor(val count: Int = 10) {

}
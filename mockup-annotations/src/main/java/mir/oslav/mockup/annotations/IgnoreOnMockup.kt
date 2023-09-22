package mir.oslav.mockup.annotations


/**
 * Annotates class parameter or data class primary constructor field to be ignored by generator.
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
internal annotation class IgnoreOnMockup {
}
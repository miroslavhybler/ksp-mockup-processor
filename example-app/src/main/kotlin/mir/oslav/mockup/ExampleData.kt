package mir.oslav.mockup

import mir.oslav.mockup.annotations.Mockup


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
@Mockup
data class Article constructor(
    val id: Int,
    val title: String,
    val content: String,
    val contentExtra: String?,
    val user: User?,
) {

}



@Mockup
class User constructor() {
    val id: Int = 0
    val firstName: String = "John"
    val lastName: String = "Doe"
    val dateOfBirth: String = "01-01-1970"
}
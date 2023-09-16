package mir.oslav.mockup.example

import mir.oslav.mockup.annotations.Mockup


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
@Mockup
data class Article constructor(
    //val id: Int,
    val title: String,
    val content: String,
    val contentExtra: String?,
   // val user: User?,
) {

}

@Mockup(isDataClass = false)
class User constructor() {
  //  val id: Int = 0
    var firstName: String = "John"
    var lastName: String = "Doe"
    var dateOfBirth: String = "01-01-1970"
}
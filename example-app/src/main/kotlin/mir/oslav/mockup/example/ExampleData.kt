package mir.oslav.mockup.example

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
    val author: User,
    val category: Category,
      val tags: List<String>,
    val isSpecialEdition: Boolean
) {

  //  var isNew: Boolean = true
 //   val isPremium: Boolean = false

}

@Mockup
data class Category constructor(
    val id: Int,
    val name: String,
)

@Mockup
data class UserRank constructor(
    val id: Int,
    val name: String
)


@Mockup(isDataClass = false)
class User constructor() {
    val id: Int = 0
    var firstName: String = "John"
    var lastName: String = "Doe"
    var dateOfBirth: String = "01-01-1970"

    var description: String = ""

    var rank: UserRank? = null


    val fullName: String get() = "$firstName $lastName"
}
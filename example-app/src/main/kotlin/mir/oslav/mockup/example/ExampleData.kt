package mir.oslav.mockup.example

import androidx.annotation.ColorInt
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
    val tags: List<String>,
    val categories: List<Category>,
    val isSpecialEdition: Boolean,
    val headerImageUrl: String,
) {

    //TODO class for images gallery

}

@Mockup
data class Category constructor(
    val id: Int,
    val name: String,
    @ColorInt
    val color: Int
) {

    fun getNameFormatted(): String {
        return if (name.length > 11) name.substring(
            startIndex = 0,
            endIndex = 11
        ) else name
    }
}

@Mockup
data class UserRank constructor(
    val id: Int,
    val name: String
)


@Mockup(isDataClass = false)
class User constructor() {
    var firstName: String = "John"
    var lastName: String = "Doe"
    var dateOfBirth: String = "01-01-1970"

    var description: String = ""
    var imageUrl: String = ""
    var rank: UserRank? = null


    val fullName: String get() = "$firstName $lastName"
}
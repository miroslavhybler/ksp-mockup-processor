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
    val tags: Array<String>,
    val categories: List<Category>,
    val isSpecialEdition: Boolean,
    val headerImageUrl: String,
    val gallery: List<GalleryPhoto>
) {

    @Mockup
    data class GalleryPhoto constructor(
        val imageUrl: String
    ) {

    }

}

@Mockup
data class Category constructor(
    val id: Int,
    val name: String,
    @ColorInt
    val color: Int
) {

    val formattedName: String
        get() {
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


@Mockup
class User constructor() {
    var id:Int = 0
    var firstName: String = "John"
    var lastName: String = "Doe"
    var dateOfBirth: String = "01-01-1970"

    var description: String = ""
    var imageUrl: String = ""
    var rank: UserRank? = null


    val fullName: String get() = "$firstName $lastName"
}
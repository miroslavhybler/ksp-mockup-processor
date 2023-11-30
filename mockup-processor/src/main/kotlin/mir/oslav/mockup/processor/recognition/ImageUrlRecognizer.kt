package mir.oslav.mockup.processor.recognition

import mir.oslav.mockup.processor.data.ResolvedProperty


/**
 * Recognizer for image urls. Mockup data are not meant to be used outside of the compose previews, for
 * authenticity real working image urls are generated.
 * Images are taken from <a href="https://www.pixabay.com/">Pixabay</a>.
 * @since 1.1.0
 * @author Miroslav Hýbler <br>
 * created on 20.10.2023
 */
class ImageUrlRecognizer constructor() : BaseRecognizer() {


    companion object {

        /**
         * @since 1.1.0
         */
        private val recognizableNames: List<String> = listOf(
            "image", "image_url", "imageUrl",
            "picture", "picture_url", "pictureUrl",
            "photo", "photo_url", "photoUrl",
            "avatar", "avatar_url", "avatarUrl",
            "thumbnail", "thumbnail_url", "thumbnailUrl",
            "profilePhotoUrl", "profile_photo_url",
            "profilePicUrl", "profile_pic_url",
        )

        /**
         * @since 1.1.0
         */
        private val imageUrlList: Sequence<String> = sequenceOf(
            "https://cdn.pixabay.com/photo/2023/10/14/09/20/mountains-8314422_1280.png",
            "https://cdn.pixabay.com/photo/2023/10/06/07/14/plant-8297610_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/09/04/19/10/butterfly-8233505_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/10/09/16/54/childrens-book-8304585_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/08/31/18/18/purple-coneflower-8225677_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/08/15/09/21/camera-8191564_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/11/02/19/25/woman-8361440_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/10/31/23/06/tiger-8356190_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/11/04/07/57/owl-8364426_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/10/10/07/59/lake-8305673_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/10/26/18/18/coneflower-8343278_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/11/02/11/32/woman-8360355_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/10/12/12/54/woman-8310743_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/09/14/15/48/woman-8253239_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/09/26/06/45/bride-8276620_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/08/30/04/16/man-8222531_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/07/31/17/06/couple-8161451_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/07/20/04/45/leva-8138344_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/04/21/15/42/portrait-7942151_1280.jpg",
            "https://cdn.pixabay.com/photo/2023/04/28/07/16/man-7956041_1280.jpg"


        )

        /**
         * @since 1.1.0
         */
        var iterator: Iterator<String> = imageUrlList.iterator()
    }


    /**
     * @return True when [property] is considered being image url.
     * @since 1.1.0
     */
    override fun recognize(property: ResolvedProperty): Boolean {
        return recognizableNames.contains(element = property.name)
    }


    /**
     * @return Code, and actual image url wrapped in "".
     * @since 1.1.0
     */
    override tailrec fun generateCodeValueForProperty(property: ResolvedProperty): String {
        if (iterator.hasNext()) {
            val imageUrl = iterator.next()
            return "\"$imageUrl\""
        }

        iterator = imageUrlList.iterator()
        return generateCodeValueForProperty(property = property)
    }

}
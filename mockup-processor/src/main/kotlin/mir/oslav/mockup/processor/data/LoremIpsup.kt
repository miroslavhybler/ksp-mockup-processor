package mir.oslav.mockup.processor.data

import kotlin.random.Random


/**
 * Lorem ipsum text to be used in string generated values
 * @since 1.0.0
 * @author Miroslav Hýbler <br>
 * created on 16.09.2023
 */
private val loremIpsum: String
    get() = """
    Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Maecenas lorem. Nunc dapibus tortor vel mi dapibus sollicitudin. Maecenas lorem. Maecenas aliquet accumsan leo. Aenean id metus id velit ullamcorper pulvinar. Sed convallis magna eu sem. Maecenas sollicitudin. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat. Maecenas aliquet accumsan leo. Nullam dapibus fermentum ipsum. Fusce aliquam vestibulum ipsum. Nullam rhoncus aliquam metus. Maecenas ipsum velit, consectetuer eu lobortis ut, dictum at dui. Nulla pulvinar eleifend sem. Integer malesuada. Integer lacinia. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Nullam feugiat, turpis at pulvinar vulputate, erat libero tristique tellus, nec bibendum odio risus sit amet ante. Mauris metus.

    Duis viverra diam non justo. Etiam bibendum elit eget erat. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Etiam neque. Nulla non arcu lacinia neque faucibus fringilla. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas sollicitudin. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Nunc auctor. Proin pede metus, vulputate nec, fermentum fringilla, vehicula vitae, justo. Suspendisse sagittis ultrices augue. Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit mauris vel metus.

    Fusce nibh. In enim a arcu imperdiet malesuada. Etiam sapien elit, consequat eget, tristique non, venenatis quis, ante. Nulla non lectus sed nisl molestie malesuada. Aliquam erat volutpat. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Fusce tellus odio, dapibus id fermentum quis, suscipit id erat. Nullam at arcu a est sollicitudin euismod. Fusce aliquam vestibulum ipsum.

    Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam erat volutpat. Mauris elementum mauris vitae tortor. Sed vel lectus. Donec odio tempus molestie, porttitor ut, iaculis quis, sem. Aenean vel massa quis mauris vehicula lacinia. Mauris dolor felis, sagittis at, luctus sed, aliquam non, tellus. Nunc dapibus tortor vel mi dapibus sollicitudin. Etiam sapien elit, consequat eget, tristique non, venenatis quis, ante. Duis viverra diam non justo. Nulla non arcu lacinia neque faucibus fringilla. Donec vitae arcu. Phasellus et lorem id felis nonummy placerat. Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur? Praesent vitae arcu tempor neque lacinia pretium. Duis viverra diam non justo. Praesent id justo in neque elementum ultrices.

    Pellentesque arcu. Maecenas lorem. Suspendisse nisl. Duis viverra diam non justo. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Aenean placerat. Praesent in mauris eu tortor porttitor accumsan. Phasellus et lorem id felis nonummy placerat. Morbi scelerisque luctus velit. Praesent id justo in neque elementum ultrices. Sed elit dui, pellentesque a, faucibus vel, interdum nec, diam. Morbi leo mi, nonummy eget tristique non, rhoncus non leo. Duis ante orci, molestie vitae vehicula venenatis, tincidunt ac pede. Curabitur sagittis hendrerit ante. Cras pede libero, dapibus nec, pretium sit amet, tempor quis. Duis viverra diam non justo. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Phasellus faucibus molestie nisl. Phasellus enim erat, vestibulum vel, aliquam a, posuere eu, velit.

    Cras elementum. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Nunc tincidunt ante vitae massa. Sed elit dui, pellentesque a, faucibus vel, interdum nec, diam. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Donec iaculis gravida nulla. In laoreet, magna id viverra tincidunt, sem odio bibendum justo, vel imperdiet sapien wisi sed libero. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Etiam sapien elit, consequat eget, tristique non, venenatis quis, ante. Nullam faucibus mi quis velit. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Nullam dapibus fermentum ipsum. Curabitur vitae diam non enim vestibulum interdum. Fusce aliquam vestibulum ipsum. Vestibulum fermentum tortor id mi. Duis viverra diam non justo. Aliquam in lorem sit amet leo accumsan lacinia. Nunc auctor. Praesent dapibus. Lorem ipsum dolor sit amet, consectetuer adipiscing elit.

    Aliquam erat volutpat. Proin mattis lacinia justo. Aliquam erat volutpat. Aenean vel massa quis mauris vehicula lacinia. Donec quis nibh at felis congue commodo. In laoreet, magna id viverra tincidunt, sem odio bibendum justo, vel imperdiet sapien wisi sed libero. Etiam sapien elit, consequat eget, tristique non, venenatis quis, ante. Nullam eget nisl. Integer vulputate sem a nibh rutrum consequat. In laoreet, magna id viverra tincidunt, sem odio bibendum justo, vel imperdiet sapien wisi sed libero. Morbi scelerisque luctus velit. Sed elit dui, pellentesque a, faucibus vel, interdum nec, diam. Integer imperdiet lectus quis justo. Etiam bibendum elit eget erat.

    Quisque porta. Nullam sapien sem, ornare ac, nonummy non, lobortis a enim. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Aliquam erat volutpat. Morbi scelerisque luctus velit. Cras elementum. Cras pede libero, dapibus nec, pretium sit amet, tempor quis. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Fusce tellus odio, dapibus id fermentum quis, suscipit id erat. Proin pede metus, vulputate nec, fermentum fringilla, vehicula vitae, justo. Maecenas libero. Maecenas sollicitudin. Morbi scelerisque luctus velit. Nulla non lectus sed nisl molestie malesuada.

    Maecenas libero. Etiam dictum tincidunt diam. Aliquam ante. Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit mauris vel metus. Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur? Etiam egestas wisi a erat. Pellentesque arcu. Vivamus luctus egestas leo. Quisque tincidunt scelerisque libero. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Fusce suscipit libero eget elit. Nam quis nulla. Pellentesque sapien. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Maecenas aliquet accumsan leo. Pellentesque sapien. Mauris elementum mauris vitae tortor. Cras elementum. Duis bibendum, lectus ut viverra rhoncus, dolor nunc faucibus libero, eget facilisis enim ipsum id lacus. Nulla est.

    Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Duis viverra diam non justo. Duis pulvinar. Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit mauris vel metus. Pellentesque pretium lectus id turpis. In enim a arcu imperdiet malesuada. Maecenas ipsum velit, consectetuer eu lobortis ut, dictum at dui. Morbi scelerisque luctus velit. Etiam dictum tincidunt diam. Donec quis nibh at felis congue commodo. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Vivamus porttitor turpis ac leo. Duis risus. Maecenas aliquet accumsan leo. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Aliquam ante. Aliquam in lorem sit amet leo accumsan lacinia. In convallis. Proin mattis lacinia justo. Fusce wisi.
""".trimIndent()


/**
 * @return String of "Lorem ipsum ..." content [maxLength] characters long
 * @since 1.0.0
 */
fun loremIpsum(
    maxLength: Int = loremIpsum.length
): String {
    val length: Int = Random.nextInt(from = 1, until = maxLength)
    val outRaw = if (length < loremIpsum.length) {
        loremIpsum.substring(startIndex = 0, endIndex = length)
    } else loremIpsum


    return outRaw.replace("\n", "")
}


/**
 * @param wordCount Number of words of output string. [loremIpsum] whole will be returned when
 * [wordCount] would be bigger then count of words.
 * @return String of "Lorem ipsum ..." content. Text will have [wordCount] words.
 * @since 1.1.0
 */
fun loremIpsumWords(wordCount: Int): String {
    val split = loremIpsum.split(" ")

    if (wordCount >= split.size) {
        return loremIpsum
    }


    return buildString {
        for (i in 0 until wordCount) {
            append(split[0])
        }
    }
}
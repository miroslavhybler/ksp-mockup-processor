@file:OptIn(ExperimentalMaterial3Api::class)

package mir.oslav.mockup.example.ui.article

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import mir.oslav.mockup.Mockup
import mir.oslav.mockup.example.Article
import mir.oslav.mockup.example.R
import mir.oslav.mockup.example.ui.Photo


/**
 * @author Miroslav Hýbler <br>
 * created on 23.09.2023
 */
@Composable
fun ArticleDetailScreen(
    navHostController: NavHostController,
    articleId: Int,
) {

    val article = remember {
        Mockup.article.list.find { article -> article.id == articleId }!!
    }

    ArticleDetailScreenContent(article = article, navHostController = navHostController)
}

@Composable
private fun ArticleDetailScreenContent(
    article: Article,
    navHostController: NavHostController
) {

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text(
                        text = article.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size = 48.dp)
                            .clip(shape = CircleShape)
                            .clickable(onClick = navHostController::popBackStack)
                            .padding(all = 10.dp)
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_favorite),
                        contentDescription = null,
                        modifier = Modifier
                            .size(size = 48.dp)
                            .clip(shape = CircleShape)
                            .clickable(onClick = {
                                //TODO
                            })
                            .padding(all = 10.dp)
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState()),
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 312.dp)
                ) {
                    //TODO replace with generated url
                    Photo(
                        imageUrl = "https://cdn.pixabay.com/photo/2023/07/13/20/39/coffee-beans-8125757_1280.jpg",
                        modifier = Modifier.matchParentSize()
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp, end = 12.dp)
                            .align(alignment = Alignment.TopEnd)
                    ) {
                        article.categories.forEach { category ->
                            Text(
                                text = category.formattedName,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier
                                    .padding(all = 2.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(size = 8.dp)
                                    )
                                    .padding(vertical = 4.dp, horizontal = 4.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }


                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(height = 4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Photo(
                        imageUrl = "https://cdn.pixabay.com/photo/2023/08/30/04/16/man-8222531_1280.jpg",
                        modifier = Modifier
                            .size(size = 32.dp)
                            .clip(shape = CircleShape)
                    )

                    Spacer(modifier = Modifier.width(width = 24.dp))

                    Text(
                        text = article.author.fullName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(height = 12.dp))

                Text(
                    text = article.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    )
}


@Composable
@Preview
private fun ArticleDetailScreenPreview() {

    ArticleDetailScreenContent(
        article = Mockup.article.singe,
        navHostController = rememberNavController()
    )

}
@file:OptIn(ExperimentalMaterial3Api::class)

package mir.oslav.mockup.example.ui.author

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import mir.oslav.mockup.Mockup
import mir.oslav.mockup.example.Article
import mir.oslav.mockup.example.User
import mir.oslav.mockup.example.ui.DetailAppBar
import mir.oslav.mockup.example.ui.Photo


/**
 * @author Miroslav Hýbler <br>
 * created on 23.09.2023
 */
@Composable
fun AuthorDetailScreen(
    navHostController: NavHostController,
    authorId: Int
) {

    AuthorDetailScreenContent(
        author = remember { Mockup.user.list.find { it.id == authorId }!! },
        navHostController = navHostController,
        articles = remember { Mockup.article.list.take(n = 5) }
    )

}


@Composable
private fun AuthorDetailScreenContent(
    modifier: Modifier = Modifier,
    author: User,
    articles: List<Article>,
    navHostController: NavHostController
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        modifier = modifier,
        topBar = {
            DetailAppBar(
                title = author.fullName,
                navHostController = navHostController,
                onFavoriteButton = {
                    coroutineScope.launch {
                      snackBarHostState.showSnackbar(
                            message = "Saved to favorites \uD83D\uDE0A",
                            actionLabel = "Dismiss",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                content = {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height = 256.dp)
                            ) {
                                //TODO replace with generated url
                                Photo(
                                    imageUrl = "https://cdn.pixabay.com/photo/2023/07/13/20/39/coffee-beans-8125757_1280.jpg",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(height = 226.dp)
                                )
                                Photo(
                                    imageUrl = "https://cdn.pixabay.com/photo/2023/08/30/04/16/man-8222531_1280.jpg",
                                    modifier = Modifier
                                        .align(alignment = Alignment.BottomStart)
                                        .padding(start = 32.dp, top = 22.dp)
                                        .size(size = 96.dp)
                                        .clip(shape = CircleShape)
                                )
                            }


                            Text(
                                text = author.fullName,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(horizontal = 12.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(height = 4.dp))

                            Text(
                                text = author.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(horizontal = 12.dp),
                                maxLines = 10
                            )
                        }

                        Spacer(modifier = Modifier.height(height = 32.dp))

                        Text(
                            text = "Most popular",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 18.dp)
                        )

                        Spacer(modifier = Modifier.height(height = 8.dp))

                    }

                    items(items = articles) { article ->
                        ArticleItem(
                            article = article,
                            onClick = {
                                navHostController.navigate(route = "article/${article.id}")
                            }
                        )
                    }
                },
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackBarData ->
                Snackbar(snackbarData = snackBarData)
            }
        }
    )


}


@Composable
private fun ArticleItem(
    modifier: Modifier = Modifier,
    article: Article,
    onClick: () -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //TODO replace with generated image url
        Photo(
            imageUrl = "https://cdn.pixabay.com/photo/2023/07/13/20/39/coffee-beans-8125757_1280.jpg",
            modifier = Modifier
                .size(size = 64.dp)
                .clip(shape = RoundedCornerShape(size = 16.dp))
        )

        Spacer(modifier = Modifier.width(width = 16.dp))
        Column(modifier = Modifier.weight(weight = 1f)) {
            Text(
                text = article.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(height = 2.dp))

            Text(
                text = article.content,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}


@Composable
@Preview
private fun AuthorDetailScreenPreview() {
    AuthorDetailScreenContent(
        author = Mockup.user.singe,
        navHostController = rememberNavController(),
        articles = Mockup.article.list
    )
}
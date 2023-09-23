package mir.oslav.mockup.example

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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


/**
 * @author Miroslav Hýbler <br>
 * created on 18.09.2023
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(
    navHostController: NavHostController,
    articles: List<Article> = Mockup.article.list
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Articles") },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(onClick = navHostController::popBackStack)
                    )
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                content = {
                    itemsIndexed(items = articles) { index, article ->
                        ArticleItem(
                            article = article,
                            navHostController = navHostController
                        )
                    }
                },
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
            )
        }
    )

}


@Composable
private fun ArticleItem(
    modifier: Modifier = Modifier,
    article: Article,
    navHostController: NavHostController
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    //TODO article detail
                }
            )
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 228.dp)
        ) {
            Photo(
                imageUrl = "https://cdn.pixabay.com/photo/2023/07/13/20/39/coffee-beans-8125757_1280.jpg",
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape = RoundedCornerShape(size = 16.dp))
            )

            Row(
                modifier = Modifier
                    .padding(top = 12.dp, end = 12.dp)
                    .align(alignment = Alignment.TopEnd)
            ) {
                article.categories.forEach { category ->
                    Text(
                        text = category.getNameFormatted(),
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
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = article.author.fullName,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
@Preview
private fun ArticleScreenPreview() {
    ArticlesScreen(
        navHostController = rememberNavController(),
        articles = Mockup.article.list
    )
}
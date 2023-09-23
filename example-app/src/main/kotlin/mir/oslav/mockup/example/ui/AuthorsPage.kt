package mir.oslav.mockup.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import mir.oslav.mockup.example.Photo
import mir.oslav.mockup.example.R
import mir.oslav.mockup.example.User


/**
 * @author Miroslav Hýbler <br>
 * created on 18.09.2023
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//Mockup data can be used like that too, but it's not purpose
fun AuthorsScreen(
    navHostController: NavHostController,
    users: List<User> = Mockup.user.list,
    paddingValues: PaddingValues,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Authors") },
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
                    itemsIndexed(items = users) { index, user ->
                        AuthorItem(user = user)
                    }
                },
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
            )
        }
    )

}


@Composable
private fun AuthorItem(user: User) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Photo(
            imageUrl = "https://cdn.pixabay.com/photo/2023/08/30/04/16/man-8222531_1280.jpg",
            modifier = Modifier
                .size(size = 56.dp)
                .clip(shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(width = 12.dp))

        Column(modifier = Modifier.weight(weight = 1f)) {
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
            Text(
                text = user.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
@Preview
private fun AuthorsScreenPreview() {
    //Preview for screen using Mockup instead of @PreviewParameterProvider
    AuthorsScreen(
        navHostController = rememberNavController(),
        users = Mockup.user.list,
        paddingValues = remember { PaddingValues() }
    )
}
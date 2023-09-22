package mir.oslav.mockup.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
fun AuthorsScreen(
    navHostController: NavHostController
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
//                    itemsIndexed(items = Mockup.user.list) { index, user ->
//                        AuthorItem(user = user)
//                    }
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
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {

        Spacer(modifier = Modifier.width(width = 10.dp))

        Column(modifier = Modifier.weight(weight = 1f)) {
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = user.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
@Preview
private fun AuthorsScreenPreview() {
    AuthorsScreen(navHostController = rememberNavController())
}
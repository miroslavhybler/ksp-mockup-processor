package mir.oslav.mockup.example.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mir.oslav.mockup.example.ui.article.ArticlesScreen
import mir.oslav.mockup.example.ui.author.AuthorsScreen


/**
 * @author Miroslav Hýbler <br>
 * created on 23.09.2023
 */
@Composable
fun BottomMenuNavHost(
    globalNavHostController: NavHostController,
    bottomMenuNavHostController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = bottomMenuNavHostController,
        startDestination = "articles"
    ) {
        composable(route = "articles") {
            ArticlesScreen(
                navHostController = globalNavHostController,
            )
        }

        composable(route = "authors") {
            AuthorsScreen(
                navHostController = globalNavHostController,
            )
        }
    }
}
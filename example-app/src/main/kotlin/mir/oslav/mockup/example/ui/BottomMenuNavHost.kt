package mir.oslav.mockup.example.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


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
                paddingValues = paddingValues
            )
        }

        composable(route = "authors") {
            AuthorsScreen(
                navHostController = globalNavHostController,
                paddingValues = paddingValues
            )
        }
    }
}
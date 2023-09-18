package mir.oslav.mockup.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mir.oslav.mockup.Mockup


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KspmockupTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navHostController = rememberNavController()
                    NavHost(navController = navHostController, startDestination = "home") {


                        composable(route = "home") {
                            HomeScreen(navHostController = navHostController)
                        }

                        composable(route = "articles") {
                            ArticlesScreen(navHostController = navHostController)
                        }

                        composable(route = "users") {
                            AuthorsScreen(navHostController = navHostController)
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun HomeScreen(navHostController: NavHostController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = { navHostController.navigate(route = "users") }) {
            Text(text = "Authors")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { navHostController.navigate(route = "articles") }) {
            Text(text = "Articles")
        }

    }
}
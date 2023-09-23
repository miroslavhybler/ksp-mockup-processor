package mir.oslav.mockup.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import mir.oslav.mockup.example.ui.BottomMenuNavHost


/**
 * @author Miroslav Hýbler <br>
 * created on 15.09.2023
 */
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KspmockupTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var selectedIndex by remember { mutableIntStateOf(value = 0) }
                    val bottomMenuNavHostController = rememberNavController()

                    Scaffold(
                        content = { paddingValues ->
                            BottomMenuNavHost(
                                globalNavHostController = rememberNavController(),
                                bottomMenuNavHostController = bottomMenuNavHostController,
                                paddingValues = paddingValues
                            )
                        },
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = selectedIndex == 0,
                                    onClick = {
                                        selectedIndex = 0
                                        bottomMenuNavHostController.navigate(route = "articles")
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_article),
                                            contentDescription = null
                                        )
                                    }
                                )
                                NavigationBarItem(
                                    selected = selectedIndex == 1,
                                    onClick = {
                                        selectedIndex = 1
                                        bottomMenuNavHostController.navigate(route = "authors")
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_users),
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
package mir.oslav.mockup.example.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import mir.oslav.mockup.example.R


/**
 * @author Miroslav Hýbler <br>
 * created on 23.09.2023
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
) {


    var selectedIndex by rememberSaveable { mutableIntStateOf(value = 0) }
    val bottomMenuNavHostController = rememberNavController()

    Scaffold(
        content = { paddingValues ->
            BottomMenuNavHost(
                globalNavHostController = navHostController,
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
                        bottomMenuNavHostController.popBackStack()
                        bottomMenuNavHostController.navigate(route = "articles")
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_article),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Articles")
                    }
                )
                NavigationBarItem(
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        bottomMenuNavHostController.popBackStack()
                        bottomMenuNavHostController.navigate(route = "authors")
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_users),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Authors")
                    }
                )
            }
        }
    )
}
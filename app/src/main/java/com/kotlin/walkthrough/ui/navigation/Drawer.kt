package com.kotlin.walkthrough.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kotlin.walkthrough.NavigationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Drawer(
    configs: List<NavigationConfig>,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column {
        DrawerHeader()
        configs.forEach {
            DrawerItem(
                config = it,
                selected = currentRoute == it.route,
                onItemClick = { config ->
                    navController.navigate(config.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun DrawerPreview() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Drawer(NavigationConfig.toList(), scope, scaffoldState, navController)
        }
    ) {
        Column(Modifier.padding(it)) {}
    }
}

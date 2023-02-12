package com.kotlin.walkthrough.ui.hrlimit

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kotlin.walkthrough.ArchType
import com.kotlin.walkthrough.ui.hrlimit.mvi.HeartRateLimit as HeartRateLimit_MVI
import com.kotlin.walkthrough.ui.hrlimit.mvvm.HeartRateLimit as HeartRateLimit_MVVM

@Composable
fun HeartRateLimitFragment() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NavigationType(navController) }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = ArchType.MVI.route
        ) {
            composable(ArchType.MVI.route) { HeartRateLimit_MVI() }
            composable(ArchType.MVVM.route) { HeartRateLimit_MVVM() }
        }
    }
}

@Composable
private fun NavigationType(navController: NavController) {
    var selected by remember { mutableStateOf(0) }

    BottomNavigation {
        ArchType.toList().forEachIndexed { index, it ->
            BottomNavigationItem(
                selected = selected == index,
                onClick = {
                    selected = index
                    navController.navigate(it.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(it.icon),
                        contentDescription = it.title
                    )
                },
                label = { Text(it.title) }
            )
        }
    }
}

@Preview
@Composable
private fun HeartRateLimitFragmentPreview() {
    HeartRateLimitFragment()
}

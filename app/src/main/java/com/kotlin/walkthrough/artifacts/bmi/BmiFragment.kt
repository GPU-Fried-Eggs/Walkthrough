package com.kotlin.walkthrough.artifacts.bmi

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
import com.kotlin.walkthrough.artifacts.bmi.mvi.Bmi as Bmi_MVI
import com.kotlin.walkthrough.artifacts.bmi.mvvm.Bmi as Bmi_MVVM

@Composable
fun BmiFragment() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NavigationType(navController) }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = ArchType.MVI.route
        ) {
            composable(ArchType.MVI.route) { Bmi_MVI() }
            composable(ArchType.MVVM.route) { Bmi_MVVM() }
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
private fun BmiFragmentPreview() {
    BmiFragment()
}

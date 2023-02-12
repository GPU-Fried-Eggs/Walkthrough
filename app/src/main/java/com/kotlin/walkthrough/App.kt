package com.kotlin.walkthrough

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kotlin.walkthrough.ui.alcometer.AlcometerFragment
import com.kotlin.walkthrough.ui.bmi.BmiFragment
import com.kotlin.walkthrough.ui.calories.CaloriesFragment
import com.kotlin.walkthrough.ui.electricity.ElectricityFragment
import com.kotlin.walkthrough.ui.home.HomeFragment
import com.kotlin.walkthrough.ui.login.LoginFragment
import com.kotlin.walkthrough.ui.navigation.Drawer
import com.kotlin.walkthrough.ui.navigation.TopBar
import com.kotlin.walkthrough.ui.sensor.SensorsFragment
import com.kotlin.walkthrough.ui.theme.ThemeFragment

sealed class NavigationConfig(var route: String, @DrawableRes var icon: Int, var title: Int) {
    object Home : NavigationConfig("home", R.drawable.ic_nav_home, R.string.nav_home)
    object Bmi: NavigationConfig("bmi", R.drawable.ic_nav_bmi, R.string.nav_bmi)
    object Login: NavigationConfig("login", R.drawable.ic_nav_login, R.string.nav_login)
    object Sensors: NavigationConfig("sensors", R.drawable.ic_nav_sensors, R.string.nav_sensors)
    object Theme: NavigationConfig("theme", R.drawable.ic_nav_theme, R.string.nav_theme)
    object Electricity: NavigationConfig("electricity", R.drawable.ic_nav_electricity, R.string.nav_electricity)
    object Calories: NavigationConfig("calories", R.drawable.ic_nav_calories, R.string.nav_calories)
    object Alcometer: NavigationConfig("alcometer", R.drawable.ic_nav_alcometer, R.string.nav_alcometer)

    companion object {
        @JvmStatic fun toList() : List<NavigationConfig> {
            return listOf(Home, Bmi, Login, Sensors, Theme, Electricity, Calories, Alcometer)
        }
    }
}

@Composable
fun App() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState) },
        drawerContent = {
            Drawer(NavigationConfig.toList(), scope, scaffoldState, navController)
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = NavigationConfig.Home.route
        ) {
            composable(NavigationConfig.Home.route) { HomeFragment() }
            composable(NavigationConfig.Bmi.route) { BmiFragment() }
            composable(NavigationConfig.Login.route) { LoginFragment() }
            composable(NavigationConfig.Sensors.route) { SensorsFragment() }
            composable(NavigationConfig.Theme.route) { ThemeFragment() }
            composable(NavigationConfig.Electricity.route) { ElectricityFragment() }
            composable(NavigationConfig.Calories.route) { CaloriesFragment() }
            composable(NavigationConfig.Alcometer.route) { AlcometerFragment() }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}

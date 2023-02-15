package com.kotlin.walkthrough

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kotlin.walkthrough.artifacts.alcometer.AlcometerFragment
import com.kotlin.walkthrough.artifacts.bmi.BmiFragment
import com.kotlin.walkthrough.artifacts.calories.CaloriesFragment
import com.kotlin.walkthrough.artifacts.electricity.ElectricityFragment
import com.kotlin.walkthrough.artifacts.home.HomeFragment
import com.kotlin.walkthrough.artifacts.hrlimit.HeartRateLimitFragment
import com.kotlin.walkthrough.artifacts.location.LocationFragment
import com.kotlin.walkthrough.artifacts.login.LoginFragment
import com.kotlin.walkthrough.artifacts.navigation.Drawer
import com.kotlin.walkthrough.artifacts.navigation.TopBar
import com.kotlin.walkthrough.artifacts.sensor.SensorsFragment
import com.kotlin.walkthrough.artifacts.theme.ThemeFragment

sealed class NavigationConfig(var route: String, @DrawableRes var icon: Int, var title: Int) {
    object Home : NavigationConfig("home", R.drawable.ic_nav_home, R.string.nav_home)
    object HRLimit: NavigationConfig("hr", R.drawable.ic_nav_hr, R.string.nav_hr)
    object Bmi: NavigationConfig("bmi", R.drawable.ic_nav_bmi, R.string.nav_bmi)
    object Login: NavigationConfig("login", R.drawable.ic_nav_login, R.string.nav_login)
    object Sensors: NavigationConfig("sensors", R.drawable.ic_nav_sensors, R.string.nav_sensors)
    object Theme: NavigationConfig("theme", R.drawable.ic_nav_theme, R.string.nav_theme)
    object Electricity: NavigationConfig("electricity", R.drawable.ic_nav_electricity, R.string.nav_electricity)
    object Calories: NavigationConfig("calories", R.drawable.ic_nav_calories, R.string.nav_calories)
    object Alcometer: NavigationConfig("alcometer", R.drawable.ic_nav_alcometer, R.string.nav_alcometer)
    object Location: NavigationConfig("location", R.drawable.ic_nav_location, R.string.nav_location)

    companion object {
        @JvmStatic fun toList(): List<NavigationConfig> {
            return listOf(
                Home, HRLimit, Bmi, Login, Sensors, Theme, Electricity,
                Calories, Alcometer, Location
            )
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
        topBar = {
            TopBar(scope = scope, scaffoldState = scaffoldState)
        },
        drawerContent = {
            Drawer(NavigationConfig.toList(), scope, scaffoldState, navController)
        },
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = NavigationConfig.Home.route
        ) {
            composable(NavigationConfig.Home.route) { HomeFragment() }
            composable(NavigationConfig.HRLimit.route) { HeartRateLimitFragment() }
            composable(NavigationConfig.Bmi.route) { BmiFragment() }
            composable(NavigationConfig.Login.route) { LoginFragment() }
            composable(NavigationConfig.Sensors.route) { SensorsFragment() }
            composable(NavigationConfig.Theme.route) { ThemeFragment() }
            composable(NavigationConfig.Electricity.route) { ElectricityFragment() }
            composable(NavigationConfig.Calories.route) { CaloriesFragment() }
            composable(NavigationConfig.Alcometer.route) { AlcometerFragment() }
            composable(NavigationConfig.Location.route) { LocationFragment() }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}

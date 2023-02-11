package com.kotlin.walkthrough.ui.navigation

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kotlin.walkthrough.NavigationConfig
import com.kotlin.walkthrough.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Drawer(configs: List<NavigationConfig>, scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column {
        DrawerHeader()
        configs.forEach {
            DrawerItem(
                config = it,
                selected = currentRoute == it.route,
                onItemClick = {
                    navController.navigate(it.route) {
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

@Composable
fun DrawerHeader() {
    val bitmap = ResourcesCompat.getDrawable(LocalContext.current.resources, R.mipmap.ic_launcher, LocalContext.current.theme)?.let {
        val bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        it.setBounds(0, 0, canvas.width, canvas.height)
        it.draw(canvas)
        bitmap.asImageBitmap()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(176.dp)
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF00695C),
                        Color(0xFF009688),
                        Color(0xFF4DB6AC)
                    )
                )
            ),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ){
            Image(
                bitmap = bitmap!!,
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(CircleShape)
            )
            Text(
                text = stringResource(R.string.nav_header_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = stringResource(R.string.nav_header_subtitle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Preview
@Composable
fun DrawerHeaderPreview() {
    DrawerHeader()
}

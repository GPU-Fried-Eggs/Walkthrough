package com.kotlin.walkthrough.ui.navigation

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kotlin.walkthrough.R
import com.kotlin.walkthrough.NavigationConfig

@Composable
fun DrawerItem(config: NavigationConfig, selected: Boolean, onItemClick: (NavigationConfig) -> Unit) {
    val target = colorResource(if (MaterialTheme.colors.isLight) R.color.purple_100 else R.color.purple_700)
    val color = remember { Animatable(Color.Transparent) }

    LaunchedEffect(selected) {
        color.animateTo(if (selected) target else Color.Transparent)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(config) }
            .height(64.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 4.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(color.value),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(config.icon),
                contentDescription = stringResource(config.title),
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .padding(start = ButtonDefaults.IconSpacing),
                tint = if (selected) MaterialTheme.colors.primary else Color.Black
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = stringResource(config.title),
                color = if (selected) MaterialTheme.colors.primary else Color.Black
            )
        }
    }
}

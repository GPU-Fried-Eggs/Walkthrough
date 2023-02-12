package com.kotlin.walkthrough

import androidx.annotation.DrawableRes

sealed class ArchType(var route: String, @DrawableRes var icon: Int, var title: String) {
    object MVI: ArchType("mvi", R.drawable.ic_nav_mvi, "MVI Design")
    object MVVM: ArchType("mvvm", R.drawable.ic_nav_mvvm, "MVVM Design")

    companion object {
        @JvmStatic fun toList(): List<ArchType> {
            return listOf(MVI, MVVM)
        }
    }
}

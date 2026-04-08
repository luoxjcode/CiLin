package com.example.yulin.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yulin.ui.home.HomeScreen
import com.example.yulin.ui.categories.CategoriesScreen
import com.example.yulin.ui.search.SearchScreen
import com.example.yulin.ui.profile.ProfileScreen
import com.example.yulin.ui.theme.OrangePrimary
import com.example.yulin.ui.theme.SlatePrimary

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "每日", Icons.Filled.Home)
    object Categories : Screen("categories", "分类", Icons.Filled.Category)
    object Search : Screen("search", "搜索", Icons.Filled.Search)
    object Profile : Screen("profile", "我的", Icons.Filled.Person)
}

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White.copy(alpha = 0.9f),
                tonalElevation = 0.dp
            ) {
                val items = listOf(
                    Screen.Home,
                    Screen.Categories,
                    Screen.Search,
                    Screen.Profile
                )
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label, fontSize = 10.sp) },
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = OrangePrimary,
                            selectedTextColor = OrangePrimary,
                            unselectedIconColor = Color.LightGray,
                            unselectedTextColor = Color.LightGray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (currentScreen) {
            Screen.Home -> HomeScreen(modifier)
            Screen.Categories -> CategoriesScreen(modifier)
            Screen.Search -> SearchScreen(modifier)
            Screen.Profile -> ProfileScreen(modifier)
        }
    }
}

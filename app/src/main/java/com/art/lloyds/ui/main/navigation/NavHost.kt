package com.art.lloyds.ui.main.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.art.domain.common.GlobalConst.Companion.NAVIGATION_OPTIONS
import com.art.domain.navigation.ScreensEnum
import com.art.lloyds.ui.screens.about.FormAboutScreen
import com.art.lloyds.ui.screens.detail.DetailScreen
import com.art.lloyds.ui.screens.detail.DetailViewModel
import com.art.lloyds.ui.screens.home.HomeScreen
import com.art.lloyds.ui.screens.home.HomeViewModel

@Composable
fun LocalNavHost(navController: NavHostController) {
    val optionsString = "/{$NAVIGATION_OPTIONS}"
    NavHost(
        navController = navController,
        startDestination = ScreensEnum.HOME.name,
    ) {
        composable(ScreensEnum.ABOUT.name) {
            FormAboutScreen()
        }
        composable(ScreensEnum.HOME.name) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(viewModel)
        }
        composable(ScreensEnum.DETAIL.name + optionsString) {
            val viewModel = hiltViewModel<DetailViewModel>()
            DetailScreen(viewModel)
        }
    }
}


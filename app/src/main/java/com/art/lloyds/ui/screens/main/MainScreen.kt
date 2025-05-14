package com.art.lloyds.ui.screens.main

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.art.domain.navigation.NavigationEnum
import com.art.lloyds.R
import com.art.lloyds.ui.main.scaffold.DrawerBody
import com.art.lloyds.ui.main.scaffold.DrawerScaffold

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is MainEffect.ControllerNavigate -> {
                    when (effect.navigationDto.command) {
                        NavigationEnum.NAVIGATE -> navController.navigate(effect.navigationDto.route)
                        NavigationEnum.POP_BACK -> navController.popBackStack()
                    }
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    DrawerBody(
                        scope = scope,
                        drawerState = drawerState,
                        drawerItemList = state.drawerItemList,
                    ) { drawerItemData ->
                        viewModel.setEvent(MainEvent.Navigate(drawerItemData))
                    }
                }
            )
        },
        gesturesEnabled = drawerState.isOpen,
        content = {
            DrawerScaffold(
                topBarTitle = stringResource(id = R.string.app_name),
                scope = scope,
                drawerState = drawerState,
                navController = navController,
            )
        }
    )
}


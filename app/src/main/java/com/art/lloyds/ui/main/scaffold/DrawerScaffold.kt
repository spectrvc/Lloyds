package com.art.lloyds.ui.main.scaffold

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.art.lloyds.ui.main.navigation.LocalNavHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerScaffold(
    topBarTitle: String,
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = topBarTitle,
                openDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            LocalNavHost(navController)
        }
    }
}



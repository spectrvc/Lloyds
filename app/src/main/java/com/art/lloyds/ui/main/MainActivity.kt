package com.art.lloyds.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.art.lloyds.ui.screens.main.MainScreen
import com.art.lloyds.ui.screens.main.MainViewModel
import com.art.lloyds.ui.main.theme.LloydTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LloydTheme {
                MainScreen(viewModel)
            }
        }
    }
}






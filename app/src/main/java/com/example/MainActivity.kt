package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.SplashScreen
import com.example.ui.screens.TekeMainContainer
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.TekeAppViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val viewModel: TekeAppViewModel = viewModel()
      
      // Resolve theme setting dynamically
      val isDarkTheme = when (viewModel.currentThemeSetting) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
      }

      MyApplicationTheme(darkTheme = isDarkTheme) {
        if (viewModel.isSplashVisible) {
          SplashScreen(
            language = viewModel.currentLanguage,
            onFinished = { viewModel.dismissSplash() }
          )
        } else {
          TekeMainContainer(viewModel = viewModel)
        }
      }
    }
  }
}

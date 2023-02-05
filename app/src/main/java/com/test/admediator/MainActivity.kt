package com.test.admediator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.test.admediator.ui.theme.AdMediatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels<MainViewModel>()

        setContent {
            AdMediatorTheme {
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            if (!viewModel.isLoading) {
                                viewModel.requestAd()
                            }
                        },
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(color = Color.White)
                        } else {
                            Text(text = "start")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Content(name: String) {
    Text(text = "Hello $name!")
}

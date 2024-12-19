package com.example.danddapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    var currentScreen by remember { mutableStateOf("Home") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compagnon de jeu") }
            )
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when (currentScreen) {
                    "Home" -> HomeScreen()
                    "DiceRoll" -> DiceRollScreen()
                    "Lore" -> LoreScreen()
                }
            }
        },
        bottomBar = {
            BottomAppBar {
                Button(onClick = { currentScreen = "Home" }) { Text("Home") }
                Button(onClick = { currentScreen = "DiceRoll" }) { Text("Dice Roll") }
                Button(onClick = { currentScreen = "Lore" }) { Text("Lore") }
            }
        }
    )
}

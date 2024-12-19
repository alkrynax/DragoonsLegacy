package com.example.danddapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*

@Composable
fun LoreScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Lore",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Affichage de la carte jdr.png
        Image(
            painter = painterResource(id = R.drawable.jdr), // Chargement de l'image
            contentDescription = "Carte Lore",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Ajuste la hauteur
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Voici la carte pour votre aventure !",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

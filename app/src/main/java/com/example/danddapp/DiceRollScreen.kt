package com.example.danddapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun DiceRollScreen() {
    var diceType by remember { mutableStateOf(6) } // Type de dé sélectionné
    var result by remember { mutableStateOf(1) } // Résultat final
    var bonusMalus by remember { mutableStateOf(0) } // Bonus/Malus
    var isBonus by remember { mutableStateOf(true) } // Type: Bonus ou Malus
    var isRolling by remember { mutableStateOf(false) } // Animation en cours
    var expanded by remember { mutableStateOf(false) } // Dropdown

    val diceOptions = listOf(4, 6, 8, 10, 12, 20) // Dés disponibles

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Lancer de dés", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown pour choisir le type de dé
        Box {
            Button(onClick = { expanded = true }) {
                Text("D$diceType")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                diceOptions.forEach { dice ->
                    DropdownMenuItem(
                        text = { Text("D$dice") },
                        onClick = {
                            diceType = dice
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sélecteur du bonus/malus
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Bonus/Malus : ", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = { isBonus = !isBonus }) {
                Text(if (isBonus) "+" else "-")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = bonusMalus.toString(),
                onValueChange = { value ->
                    bonusMalus = value.toIntOrNull() ?: 0
                },
                modifier = Modifier.width(80.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Affichage du dé
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .background(Color.LightGray, shape = CircleShape)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawDiceShape(this, diceType, result)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Bouton pour lancer le dé
        Button(onClick = {
            isRolling = true
            kotlinx.coroutines.MainScope().launch {
                repeat(15) { // Animation rapide
                    result = Random.nextInt(1, diceType + 1) +
                            (if (isBonus) bonusMalus else -bonusMalus)
                    delay(100)
                }
                isRolling = false
            }
        }) {
            Text("Lancer le dé")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Affichage du résultat
        Text(
            text = "Résultat : $result",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )
    }
}

// Dessiner la forme du dé selon son type
fun drawDiceShape(drawScope: DrawScope, diceType: Int, result: Int) {
    when (diceType) {
        4 -> drawTetrahedron(drawScope, result)
        6 -> drawCube(drawScope, result)
        8 -> drawPolygon(drawScope, 8, result)
        10 -> drawPolygon(drawScope, 10, result)
        12 -> drawPolygon(drawScope, 12, result)
        20 -> drawPolygon(drawScope, 20, result)
    }
}

// Dessiner un triangle pour le D4
fun drawTetrahedron(drawScope: DrawScope, result: Int) {
    val path = Path()
    val centerX = drawScope.size.width / 2
    val centerY = drawScope.size.height / 2
    val radius = drawScope.size.minDimension / 2.5f

    for (i in 0 until 3) {
        val angle = Math.toRadians((i * 120 - 90).toDouble())
        val x = centerX + radius * cos(angle).toFloat()
        val y = centerY + radius * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawScope.drawPath(path, color = Color.Gray)

    drawScope.drawTextAtCenter(result, centerX, centerY)
}

// Dessiner un carré pour le D6
fun drawCube(drawScope: DrawScope, result: Int) {
    drawScope.drawRect(
        color = Color.Gray,
        topLeft = Offset(drawScope.size.width / 4, drawScope.size.height / 4),
        size = drawScope.size / 2F
    )
    drawScope.drawTextAtCenter(result, drawScope.size.width / 2, drawScope.size.height / 2)
}

// Dessiner un polygone pour D8, D10, D12, D20
fun drawPolygon(drawScope: DrawScope, sides: Int, result: Int) {
    val path = Path()
    val centerX = drawScope.size.width / 2
    val centerY = drawScope.size.height / 2
    val radius = drawScope.size.minDimension / 2.5f

    for (i in 0..sides) {
        val angle = Math.toRadians((i * 360.0 / sides - 90).toFloat().toDouble())
        val x = centerX + radius * cos(angle).toFloat()
        val y = centerY + radius * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawScope.drawPath(path, color = Color.Gray)

    drawScope.drawTextAtCenter(result, centerX, centerY)
}

// Fonction utilitaire pour dessiner un texte au centre
fun DrawScope.drawTextAtCenter(result: Int, centerX: Float, centerY: Float) {
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            result.toString(),
            centerX,
            centerY + 20, // Ajustement vertical
            android.graphics.Paint().apply {
                textSize = 64f
                color = android.graphics.Color.BLACK
                textAlign = android.graphics.Paint.Align.CENTER
            }
        )
    }
}

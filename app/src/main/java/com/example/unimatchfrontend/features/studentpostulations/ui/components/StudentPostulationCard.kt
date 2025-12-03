package com.example.unimatchfrontend.features.studentpostulations.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun StudentPostulationCard(
    projectName: String,
    companyName: String,
    status: String,
    date: String
) {
    val statusColor = when (status) {
        "Aceptado" -> Color(0xFF4CAF50) // Verde
        "Rechazada" -> Color(0xFFF44336) // Rojo
        else -> Color(0xFF2196F3) // Azul
    }

    val daysAgo = try {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd")
        val postDate = formatter.parse(date)
        val today = java.util.Date()
        val diffMillis = today.time - postDate.time
        (diffMillis / (1000 * 60 * 60 * 24)) // convierte a días
    } catch (e: Exception) {
        0L
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = projectName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Box(
                    modifier = Modifier
                        .background(statusColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = status,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "by $companyName",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Postulado hace ${daysAgo} días",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

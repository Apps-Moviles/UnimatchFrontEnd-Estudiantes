package com.example.unimatchfrontend.features.students.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unimatchfrontend.features.projects.domain.model.Project

@Composable
fun OpportunityCard(project: Project, companyName: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = project.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "by $companyName")
                Text(text = "Hace 2 días", fontSize = 12.sp, color = Color.Gray)
            }
            Text(
                text = "$ ${project.budget.toInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

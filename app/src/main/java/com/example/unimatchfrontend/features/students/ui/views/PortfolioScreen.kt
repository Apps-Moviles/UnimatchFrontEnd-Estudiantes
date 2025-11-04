package com.example.unimatchfrontend.features.students.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.unimatchfrontend.navigation.Routes
import com.example.unimatchfrontend.shared.ui.BottomNavigationBar

@Composable
fun PortfolioScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute = Routes.PORTFOLIO) },
        containerColor = Color(0xFFF5F0E8)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Portafolio",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            // Aquí puedes añadir contenido del portafolio cuando estés listo
        }
    }
}

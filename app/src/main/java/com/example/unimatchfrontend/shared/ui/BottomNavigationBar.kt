package com.example.unimatchfrontend.shared.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.unimatchfrontend.navigation.Routes

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

val studentNavItems = listOf(
    BottomNavItem(Routes.OPPORTUNITIES, Icons.Default.Home, "Oportunidades"),
    BottomNavItem(Routes.PORTFOLIO, Icons.Default.Star, "Portafolio"),
    BottomNavItem(Routes.POSTULATIONS, Icons.Default.List, "Postulaciones"),
    BottomNavItem(Routes.PROFILE, Icons.Default.Person, "Perfil")
)

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar(
        containerColor = Color(0xFF1C1F2B), // Azul de fondo
    ) {
        studentNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.OPPORTUNITIES) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) Color(0xFFFFD479) else Color.White // Amarillo si seleccionado, blanco si no
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selected) Color(0xFFFFD479) else Color.White
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
